package com.iso.plogues.question.comment.model.vo;

import java.sql.Date;
import java.time.LocalDateTime;

import com.iso.plogues.question.model.vo.Question;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Answer {
	private Long answerNo;
	private String userId;
	private Long boardNo;
	private LocalDateTime createDate;
	private String content;
	private String updated;
	private String deleted;

}
