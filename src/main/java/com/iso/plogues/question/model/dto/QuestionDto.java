package com.iso.plogues.question.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
	private Long boardNo;
	private String userId;
	@NotBlank
	private String title;
	@NotBlank
	private String category;
	private LocalDateTime createDate;
	@NotBlank
	private String content;
	private String updated;
	private String deleted;


}
