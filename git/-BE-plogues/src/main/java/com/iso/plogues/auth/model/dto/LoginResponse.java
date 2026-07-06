package com.iso.plogues.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class LoginResponse {
	private String userId;
	private String userName;
	private String role;
	private String accessToken;
	private String refreshToken;

}
