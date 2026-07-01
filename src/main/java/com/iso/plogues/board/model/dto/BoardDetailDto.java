package com.iso.plogues.board.model.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDetailDto {
	 	private Long boardNo;
	    private String title;
	    private String writer;
	    private Date createDate;
	    private String content;
}
