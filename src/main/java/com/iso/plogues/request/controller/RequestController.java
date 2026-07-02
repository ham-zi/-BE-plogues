package com.iso.plogues.request.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/request")
@Slf4j
public class RequestController {

	private final RequestService requestService;
	
	@PostMapping("/{joinNo}")
	public ResponseEntity<ApiResponse<Void>> saveRequest(@AuthenticationPrincipal CustomUserDetails user, @PathVariable(value="joinNo")Long joinNo, @Valid @RequestBody RequestDto requestDto){
		requestDto.setUserId(user.getUsername());
		requestDto.setJoinNo(joinNo);
		if(!"host".equals(requestDto.getAspiration())) {
			requestDto.setStatus("WAITING");
		}
		requestService.saveRequest(requestDto);
		return ResponseEntity.ok().body(ApiResponse.created("요청에 성공했습니다.", null));
	}
	
	@PatchMapping("/{requestNo}")
	public ResponseEntity<ApiResponse<Void>> requestAccept(@AuthenticationPrincipal CustomUserDetails user, @PathVariable(value="requestNo")Long requestNo){
		requestService.requestAccept(user,requestNo);
		return ResponseEntity.ok().body(ApiResponse.success("모임 신청 수락에 성공하셨습니다.", null));
	}
	
	@DeleteMapping("/{requestNo}")
	public ResponseEntity<ApiResponse<Void>> requestDenied(@AuthenticationPrincipal CustomUserDetails user, @PathVariable(value="requestNo")Long requestNo){
		requestService.requestDenied(user,requestNo);
		return ResponseEntity.ok().body(ApiResponse.success("모임 신청 거절에 성공하셨습니다", null));
	}
	
	
}
