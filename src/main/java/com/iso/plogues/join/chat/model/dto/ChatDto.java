package com.iso.plogues.join.chat.model.dto;

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
public class ChatDto {
	private Long chatNo;
	private String userId;
	private Long joinNo;
	@NotBlank
	@Size(min = 2, max = 100, message = "2글자 이상 100글자까지 작성 가능합니다.")
	private String content;
	private LocalDateTime createDate;
	private String updated;
	private String deleted;

}
