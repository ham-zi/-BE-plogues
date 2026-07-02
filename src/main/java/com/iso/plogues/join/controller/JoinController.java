package com.iso.plogues.join.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.join.chat.model.dto.ChatDto;
import com.iso.plogues.join.chat.model.service.ChatService;
import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.join.model.service.JoinService;
import com.iso.plogues.util.dto.BoardResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/joins")
@RequiredArgsConstructor
public class JoinController {
	private final JoinService joinService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> saveJoin(@AuthenticationPrincipal CustomUserDetails user, @Valid JoinDto join, @RequestParam(name="file", required=false) MultipartFile file) {
		joinService.saveJoin(user, join, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("게시글 작성 성공", null));
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<BoardResponse<JoinDto>>> findAll(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="category") String category) {
		BoardResponse<JoinDto> br = null;
		if("plant".equals(category)) {
			br = joinService.findAllPlant(page);
		} else if("plogging".equals(category)) {
			br = joinService.findAllPlog(page);
		}
		return ResponseEntity.status(200).body(ApiResponse.success("게시글 전체 조회 성공", br));
	}
	
	@GetMapping("/{joinNo}")
	public ResponseEntity<ApiResponse<JoinDto>> findByJoinNo(@PathVariable(name="joinNo") Long joinNo) {
		JoinDto join = joinService.findByJoinNo(joinNo);
		return ResponseEntity.status(200).body(ApiResponse.success("게시글 조회 성공", join));
	}
	
	@DeleteMapping("/{joinNo}")
	public ResponseEntity<ApiResponse<Void>> deleteJoin(@AuthenticationPrincipal CustomUserDetails user, @PathVariable(name="joinNo") Long joinNo) {
		joinService.deleteJoin(user, joinNo);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.noContent("게시글 삭제 성공", null));
	}
	
	@PatchMapping("/{joinNo}")
	public ResponseEntity<ApiResponse<Void>> updateJoin(@AuthenticationPrincipal CustomUserDetails user, @PathVariable(name="joinNo") Long joinNo, @Valid JoinDto join, @RequestParam(name="file", required=false) MultipartFile file) {
		joinService.updateJoin(user, joinNo, join, file);
		return ResponseEntity.status(200).body(ApiResponse.success("게시글 수정 성공", null));
	}
	
	@PostMapping("/reform")
	public ResponseEntity<ApiResponse<Void>> reformJoin(@AuthenticationPrincipal CustomUserDetails user, @Valid JoinDto join, @RequestParam(name="file", required=false) MultipartFile file) {
		joinService.saveJoin(user, join, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("게시글 작성 성공", null));
	}

}