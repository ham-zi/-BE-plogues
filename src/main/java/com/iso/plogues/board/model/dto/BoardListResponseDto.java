package com.iso.plogues.board.model.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoardListResponseDto {
	private int totalElements;
    private int totalPages;
    private int currentPage;
    private List<BoardDto> reviewList;
   
}
