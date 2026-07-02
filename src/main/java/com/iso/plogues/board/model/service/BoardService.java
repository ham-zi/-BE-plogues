package com.iso.plogues.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import org.springframework.web.multipart.MultipartFile;
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

    public BoardResponse<BoardDto> selectBoardList(int currentPage) {
        int listCount = boardMapper.countBoardList();
        PageInfo page = PageInfo.of(listCount, currentPage, 10, 5);
        List<BoardDto> boardList = boardMapper.selectBoardList(page);
        BoardResponse<BoardDto> response = new BoardResponse<>();
        response.setPage(page);
        response.setBoard(boardList);
        return response;
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
        BoardResponse<BoardDto> response = new BoardResponse<>();
        response.setPage(page);
        response.setBoard(boardList);
        return response;
    }

    public BoardDto selectBoardDetail(Long boardNo) {
        BoardDto board = boardMapper.selectBoardDetail(boardNo);
        if (board == null) throw new FailedFindByNoException("존재하지 않는 게시글입니다.");
        List<FileDto> files = boardMapper.selectFileList(boardNo);
        board.setFileList(files);
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
    public void updateBoard(CustomUserDetails user, Long boardNo, BoardDto boardDto, List<MultipartFile> files) {
        boardDto.setUserId(user.getUsername());
        boardDto.setBoardNo(boardNo);
        int result = boardMapper.updateBoard(boardDto);
        if(result != 1) throw new FailedUpdateException("게시글 수정에 실패했습니다.");
        if(files != null && !files.isEmpty()) {
            for(MultipartFile file : files) {
                boardFileService.updateFile(file, boardNo);
            }
        }
    }
    
    @Transactional
    public void deleteBoard(CustomUserDetails user, Long boardNo) {
        selectBoardDetail(boardNo);
        int result = boardMapper.deleteBoard(user.getUsername(), boardNo);
        if(result != 1) throw new FailedDeleteException("게시글 삭제에 실패했습니다.");
        boardFileService.deleteFile(boardNo);
    }
}
