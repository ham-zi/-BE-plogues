package com.iso.plogues.board.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {
	private Long boardNo;
    private String title;
    private String writer;
    private String createDate;
    private int views;
}
