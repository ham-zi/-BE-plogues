package com.iso.plogues.join.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.join.model.service.JoinService;
import com.iso.plogues.template.board.BoardResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/joins")
@RequiredArgsConstructor
public class JoinController {
	private final JoinService joinService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> saveJoin(@AuthenticationPrincipal CustomUserDetails user, JoinDto join, @RequestParam(name="file", required=false) MultipartFile file) {
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
		return ResponseEntity.status(200).body(ApiResponse.created("게시글 전체 조회 성공", br));
	}

}