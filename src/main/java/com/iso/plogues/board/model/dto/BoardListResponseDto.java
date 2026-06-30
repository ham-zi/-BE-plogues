package com.iso.plogues.board.model.dto;

import java.util.List;

import com.iso.plogues.page.PageInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardListResponseDto {

    private List<BoardDto> boardList;
    private PageInfo pageInfo;
    
}