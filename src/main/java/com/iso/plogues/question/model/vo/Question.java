package com.iso.plogues.question.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Question {
	private Long boardNo;
	private String userId;
	private String title;
	private String category;
	private Date createDate;
	private String content;
	private String updated;
	private String deleted;

}
