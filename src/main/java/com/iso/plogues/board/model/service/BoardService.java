package com.iso.plogues.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.board.model.dao.BoardMapper;
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.file.FileService;
import com.iso.plogues.util.page.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
    private final BoardMapper boardMapper;
    private final FileService fileService;

    public BoardResponse<BoardDto> selectBoardList(int currentPage) {

        int listCount = boardMapper.countBoardList();

        PageInfo page = PageInfo.of(
                listCount,
                currentPage,
                10,
                5
        );

        List<BoardDto> boardList = boardMapper.selectBoardList(page);

        BoardResponse<BoardDto> response = new BoardResponse<>();

        response.setPage(page);
        response.setBoard(boardList);

        return response;
    }

    public BoardDto selectBoardDetail(Long boardNo) {
        BoardDto board = boardMapper.selectBoardDetail(boardNo);
        if (board == null) throw new RuntimeException("존재하지 않는 게시글입니다.");

        List<FileDto> files = boardMapper.selectFileList(boardNo);
        board.setFileList(files);

        return board;
    }
    
    public void insertBoard(BoardDto boardDto, List<MultipartFile> files) {
        boardMapper.insertBoard(boardDto);
        Long boardNo = boardDto.getBoardNo(); 

        if (files != null && !files.isEmpty()) {
            for (MultipartFile upfile : files) {
                File file = File.of(boardNo, upfile.getOriginalFilename(), "board");
                fileService.fileTransferTo(upfile, file.getChangeName(), file.getBoardType());
                boardMapper.insertFile(file);
            }
        }
    }
}
