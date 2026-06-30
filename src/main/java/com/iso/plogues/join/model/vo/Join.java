package com.iso.plogues.join.model.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Join {
	private Long joinNo;
	private String userId;
	private String category;
	private int participants;
	private String region;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String title;
	private String content;
	private String deleted;
	private String updated;
	private LocalDateTime createDate;

}
