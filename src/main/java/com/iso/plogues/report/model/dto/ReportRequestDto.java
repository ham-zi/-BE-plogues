package com.iso.plogues.report.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportRequestDto {
	@NotBlank(message="분류를 입력해 주세요")
	private String category;
	@NotBlank(message="게시글 종류를 입력해 주세요")
	private String boardType;
	@NotBlank(message="상태를 입력해 주세요")
	private String status;
	
}
