package com.iso.plogues.report.model.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Report {
	private Long reportNo;
    private String userId;
    private String reportCategory;
    private String boardType;
    private String content;
    private LocalDateTime createDate;
    private String updated;
    private String deleted;
    private Long targetNo;

}
