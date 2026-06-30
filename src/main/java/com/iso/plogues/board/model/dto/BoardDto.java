package com.iso.plogues.board.model.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {
	private Long boardNo;
    private String title;
    private String writer;
    private Date createDate;
    private int views;
}
