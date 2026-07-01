package com.iso.plogues.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.dto.LoginRequestDto;
import com.iso.plogues.auth.model.dto.LoginResponse;
import com.iso.plogues.auth.model.service.AuthService;
import com.iso.plogues.auth.model.vo.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequestDto lrd){
		LoginResponse res = authService.login(lrd);
		return ResponseEntity.ok().body(ApiResponse.success("로그인 성공", res));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<Map<String, String>>> refresh(@RequestBody Map<String, String> token) {
		Map<String, String> tokens = authService.refresh(token.get("refreshToken"));
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(tokens));
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Map<String, String>>> logout(@AuthenticationPrincipal CustomUserDetails user, @RequestBody String refreshToken){
		authService.logout(user, refreshToken);
		return ResponseEntity.status(200).body(ApiResponse.success("로그아웃 성공", null));
	}
}

