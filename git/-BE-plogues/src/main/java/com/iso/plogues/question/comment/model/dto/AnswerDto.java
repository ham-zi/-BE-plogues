package com.iso.plogues.question.comment.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
	private Long answerNo;
	private String userId;
	private Long boardNo;
	private LocalDateTime createDate;
	@NotBlank(message="필수 입력 항목입니다.")
	@Size(max = 1000, message = "내용은 최대 1000자까지 입력 가능합니다.")
	private String content;
	private String updated;
	private String deleted;
	
}
