package com.iso.plogues.report.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {

	// 검증하기
	private Long reportNo;
	private String userId;
	private String reportCategory;
	private String boardType;
	private String content;
	private LocalDateTime creatDate;
	private String update;
	private String delete;
	private Long targetNo;
	
	

}
