package com.iso.plogues.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.iso.plogues.board.model.dao.BoardMapper;
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.board.model.dto.BoardListResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardMapper boardMapper;

    public BoardListResponseDto selectBoardList(int page) {

        int limit = 5;
        int offset = (page - 1) * limit;

        int totalElements = boardMapper.countBoardList();

        int totalPages =
                (int)Math.ceil((double)totalElements / limit);

        List<BoardDto> reviewList =
                boardMapper.selectBoardList(offset, limit);

        return BoardListResponseDto.builder()
                .totalElements(totalElements)
                .totalPages(totalPages)
                .currentPage(page)
                .reviewList(reviewList)
                .build();
    }
}
