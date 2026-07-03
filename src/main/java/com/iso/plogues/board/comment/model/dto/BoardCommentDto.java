package com.iso.plogues.board.comment.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDto {
	private Long commentNo;
    private String content;
    private String writer;
    private Date createDate;
    private String updated;
    private Long boardNo;
    private String userId;
}
