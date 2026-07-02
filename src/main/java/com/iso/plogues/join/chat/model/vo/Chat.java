package com.iso.plogues.join.chat.model.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Chat {
	private Long chatNo;
	private Long requestNo;
	private String content;
	private LocalDateTime createDate;
	private String updated;
	private String deleted;

}
