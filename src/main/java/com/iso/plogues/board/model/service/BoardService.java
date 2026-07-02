package com.iso.plogues.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.board.model.dao.BoardMapper;
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.page.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

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
}
