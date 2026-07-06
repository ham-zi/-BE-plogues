package com.iso.plogues.report.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {

	private Long reportNo;
	private String userId;
	@NotBlank(message="분류를 입력해 주세요")
	private String reportCategory;
	private String boardType;
	@NotBlank(message="내용을 입력해 주세요")
	@Size(min=2, max=500, message="내용은 2글자 이상 500글자까지 작성가능합니다")
	private String content;
	private LocalDateTime createDate;
	private String updated;
	private String deleted;
	private Long targetNo;
	private int count;
	private String status;
	
	

}
