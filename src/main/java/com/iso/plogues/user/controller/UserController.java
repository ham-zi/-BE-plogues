package com.iso.plogues.user.controller;

import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.request.model.dto.RequestDto;
import com.iso.plogues.user.model.dto.MyInfoDto;
import com.iso.plogues.user.model.dto.UserDto;
import com.iso.plogues.user.model.service.UserService;
import com.iso.plogues.user.model.vo.MyPageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<MyInfoDto>> selectMyInfo(@AuthenticationPrincipal CustomUserDetails user) {
		return ResponseEntity.ok().body(ApiResponse.success("회원정보 조회 성공", userService.selectMyInfo(user)));
	}
	
	@GetMapping("/requests")
	public ResponseEntity<ApiResponse<MyPageResponse<RequestDto>>> findAllMyRequest(@AuthenticationPrincipal CustomUserDetails user, @RequestParam(value="page", defaultValue="1")int page, @RequestParam(value="status", defaultValue="all")String status) {
		return ResponseEntity.ok().body(ApiResponse.success("참여 요청 목록 조회 성공", userService.findAllMyRequest(user,page,status)));
	}

	@GetMapping("/boards")
	public ResponseEntity<ApiResponse<MyPageResponse<BoardDto>>> findAllMyBoard(@AuthenticationPrincipal CustomUserDetails user, @RequestParam(value="page", defaultValue="1")int page) {
		return ResponseEntity.ok().body(ApiResponse.success("참여 요청 목록 조회 성공", userService.findAllMyBoards(user,page)));
	}

	@GetMapping("/groups")
	public ResponseEntity<ApiResponse<MyPageResponse<JoinDto>>> findAllMyGroups(@AuthenticationPrincipal CustomUserDetails user, @RequestParam(value="page", defaultValue="1")int page) {
		return ResponseEntity.ok().body(ApiResponse.success("참여 요청 목록 조회 성공", userService.findAllMyGroups(user,page)));
	}

	@GetMapping("/joins")
	public ResponseEntity<ApiResponse<MyPageResponse<RequestDto>>> findAllMyJoins(@AuthenticationPrincipal CustomUserDetails user, @RequestParam(value="page", defaultValue="1")int page, @RequestParam(value="status", defaultValue="all")String status) {
		return ResponseEntity.ok().body(ApiResponse.success("참여 요청 목록 조회 성공", userService.findAllMyJoins(user,page, status)));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody @Valid UserDto user){
		userService.signUp(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("계정 생성 성공", null));
	}
	
	@PatchMapping
	public ResponseEntity<ApiResponse<Void>> patchMyInfo(@AuthenticationPrincipal CustomUserDetails user, @ModelAttribute UserDto userInfo, @RequestParam(value="file")MultipartFile file) {
		userService.patchMyInfo(user, userInfo, file);
		return ResponseEntity.ok().body(ApiResponse.success("회원정보 수정 성공", null));
	}
	
	
	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteAccount(@AuthenticationPrincipal CustomUserDetails user, @RequestBody Map<String,String>body){
		userService.deleteAccount(user,body);
		return ResponseEntity.ok().body(ApiResponse.success("계정 삭제 성공", null));
	}
	

}
