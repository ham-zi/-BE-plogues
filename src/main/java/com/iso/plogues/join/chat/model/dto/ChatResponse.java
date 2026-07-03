package com.iso.plogues.join.chat.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
	private Long chatNo;
	private String userId;
	private String userName;
	private String userFilePath;
	private String title;
	private String content;
	private LocalDateTime createDate;
	private String updated;
	private String deleted;

}
