package com.iso.plogues.board.model.service;

import com.iso.plogues.board.comment.controller.BoardCommentController;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.board.comment.model.dao.BoardCommentMapper;
import com.iso.plogues.board.comment.model.dto.BoardCommentDto;
import com.iso.plogues.board.file.model.service.BoardFileService;
import com.iso.plogues.board.model.dao.BoardMapper;
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedFindByNoException;
import com.iso.plogues.exception.FailedUpdateException;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.page.PageInfo;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@Service
public class BoardService {

    private final BoardCommentController boardCommentController;
	private final BoardMapper boardMapper;
    private final BoardFileService boardFileService;
    private final BoardCommentMapper commentMapper;
    private final MeterRegistry registry;
    private final Counter errorCounter;
    
    public BoardService(BoardCommentMapper commentMapper ,BoardMapper boardMapper, BoardFileService boardFileService, BoardCommentMapper boardCommentMapper,MeterRegistry registry, BoardCommentController boardCommentController) {
    	this.boardMapper = boardMapper;
    	this.boardFileService = boardFileService;
    	this.boardCommentController = boardCommentController;
    	this.commentMapper = commentMapper;
    	this.registry = registry;
        this.errorCounter = Counter.builder("board_error_total")
                .description("게시글 에러 횟수")
                .register(registry);
    }

    public BoardResponse<BoardDto> selectBoardList(int currentPage, String keyword) {
        int listCount = boardMapper.countBoardList(keyword);
        PageInfo page = PageInfo.of(listCount, currentPage, 10, 5);
        List<BoardDto> boardList = boardMapper.selectBoardList(page, keyword);
        return new BoardResponse<>(page, boardList);
    }

    
    @Transactional(readOnly=true)
    public BoardResponse<BoardDto> selectMyBoardList(CustomUserDetails user, int currentPage) {
        int listCount = boardMapper.countMyBoardList(user.getUsername());
        PageInfo page = PageInfo.of(
                listCount,
                currentPage,
                10,
                5
        );
        List<BoardDto> boardList = boardMapper.selectMyBoardList(user.getUsername(), page);
        return new BoardResponse<>(page, boardList);
    }
    
    @Transactional
    public BoardDto selectBoardDetail(Long boardNo) {
        BoardDto board = boardMapper.selectBoardDetail(boardNo);
        if (board == null) {
            throw new FailedFindByNoException("존재하지 않는 게시글입니다.");
        }
        boardMapper.increaseViewCount(boardNo);
        List<FileDto> files = boardMapper.selectFileList(boardNo);
        board.setFileList(files);
        
        List<BoardCommentDto> comments = commentMapper.selectCommentList(boardNo);
        board.setCommentList(comments);
        
        return board;
    }
    
    @Transactional
    public void insertBoard(BoardDto boardDto, List<MultipartFile> files) {
        boardMapper.insertBoard(boardDto);
        Long boardNo = boardDto.getBoardNo();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile upfile : files) {
                boardFileService.saveFile(upfile, boardNo);
            }
        }
    }

    @Transactional
    public void updateBoard(CustomUserDetails user, Long boardNo, BoardDto boardDto,
                             List<MultipartFile> files, List<Long> deleteFileNos) {
        boardDto.setUserId(user.getUsername());
        boardDto.setBoardNo(boardNo);
        int result = boardMapper.updateBoard(boardDto);
        if(result != 1) {
        	errorCounter.increment();
            throw new FailedUpdateException("게시글 수정에 실패했습니다.");
        }

        // 사용자가 화면에서 지운 기존 파일만 삭제
        if(deleteFileNos != null && !deleteFileNos.isEmpty()) {
            for(Long fileNo : deleteFileNos) {
                boardFileService.deleteFileByNo(fileNo);
            }
        }

        // 새로 추가된 파일만 저장 (기존 파일 그대로)
        if(files != null && !files.isEmpty()) {
            for(MultipartFile file : files) {
                boardFileService.saveFile(file, boardNo);
            }
        }
    }
    
    @Transactional
    public void deleteBoard(CustomUserDetails user, Long boardNo) {
        selectBoardDetail(boardNo);
        int result = boardMapper.deleteBoard(user.getUsername(), boardNo);
        if(result != 1) {
        	errorCounter.increment();
            throw new FailedDeleteException("게시글 삭제에 실패했습니다.");
        }
        boardFileService.deleteFile(boardNo);
    }
    
    public double countAll(String keyword) {
    	return boardMapper.countBoardList(keyword);
    }
}
