package com.iso.plogues.join.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantDto {
	private Long joinRequestNo;
	private String userId;
	private String profile;

}
