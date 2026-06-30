package com.iso.plogues.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.iso.plogues.board.model.dao.BoardMapper;
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.board.model.dto.BoardListResponseDto;
import com.iso.plogues.page.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardMapper boardMapper;

	public BoardListResponseDto selectBoardList(int currentPage) {

	    int listCount = boardMapper.countBoardList();

	    PageInfo pi = PageInfo.of(
	            listCount,
	            currentPage,
	            10, 
	            5   
	    );

	    List<BoardDto> boardList  = boardMapper.selectBoardList(pi);

	    return BoardListResponseDto.builder()
	            .boardList(boardList)
	            .pageInfo(pi)
	            .build();
	}
}
