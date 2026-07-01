package com.iso.plogues.question.model.dto;


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
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
	private Long boardNo;
	private String userId;
	@NotBlank(message = "입력 필수 항목입니다.")
	@Size(max = 200, message = "제목은 최대 200자까지 입력 가능합니다.")
	private String title;
	@NotBlank(message = "카테고리는 필수입니다.")
	private String category;
	private LocalDateTime createDate;
	@NotBlank(message = "입력 필수 항목입니다.")
	@Size(max = 1000, message = "내용은 최대 1000자까지 입력 가능합니다.")
	private String content;
	private String updated;
	private String deleted;


}
