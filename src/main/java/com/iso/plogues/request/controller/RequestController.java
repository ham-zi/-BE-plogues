package com.iso.plogues.request.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.request.model.dto.RequestDto;
import com.iso.plogues.request.model.service.RequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class RequestController {

	private final RequestService requestService;
	
	@PostMapping("/api/request/{joinNo}")
	public ResponseEntity<ApiResponse<Void>> requestJoin(@AuthenticationPrincipal CustomUserDetails user, @PathVariable(value="joinNo")Long joinNo, @Valid @RequestBody RequestDto requestDto){
		requestDto.setUserId(user.getUsername());
		requestDto.setJoinNo(joinNo);
		requestService.requestJoin(requestDto);
		return ResponseEntity.ok().body(ApiResponse.created("요청에 성공했습니다.", null));
	}
}
