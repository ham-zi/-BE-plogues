package com.iso.plogues.board.model.service;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final BoardFileService boardFileService;
    private final BoardCommentMapper commentMapper;

    public BoardResponse<BoardDto> selectBoardList(int currentPage) {
        int listCount = boardMapper.countBoardList();
        PageInfo page = PageInfo.of(listCount, currentPage, 10, 5);
        List<BoardDto> boardList = boardMapper.selectBoardList(page);
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
            throw new FailedDeleteException("게시글 삭제에 실패했습니다.");
        }
        boardFileService.deleteFile(boardNo);
    }
}
