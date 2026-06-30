package com.iso.plogues.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.user.model.dto.UserDto;
import com.iso.plogues.user.model.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody @Valid UserDto user){
		userService.signUp(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("계정 생성 성공", null));
	}
	
	
	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteAccount(@AuthenticationPrincipal CustomUserDetails user, @RequestBody Map<String,String>body){
		userService.deleteAccount(user,body);
		return ResponseEntity.ok().body(ApiResponse.success("계정 삭제 성공", null));
	}
	

}
