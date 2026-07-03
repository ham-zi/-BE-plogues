package com.iso.plogues.join.chat.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.join.chat.model.dto.ChatDto;
import com.iso.plogues.join.chat.model.service.ChatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> saveChat(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody ChatDto chat) {
		chatService.saveChat(user, chat);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("채팅 작성 성공", null));
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<ChatDto>>> findAll(@AuthenticationPrincipal CustomUserDetails user, @RequestParam(name="bno") Long joinNo) {
		List<ChatDto> list = chatService.findAll(user, joinNo);
		return ResponseEntity.status(200).body(ApiResponse.success("채팅 목록 조회 성공", list));
	}
	
	@PatchMapping("/{chatNo}")
	public ResponseEntity<ApiResponse<Void>> updateChat(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody ChatDto chat, @PathVariable(name="chatNo") Long chatNo) {
		chat.setChatNo(chatNo);
		chatService.updateChat(user, chat);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("채팅 수정 성공", null));
	}
	
	@DeleteMapping("/{chatNo}")
	public ResponseEntity<ApiResponse<Void>> DeleteChat(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody Map<String, Long> joinNo, @PathVariable(name="chatNo") Long chatNo) {
		chatService.deleteChat(user, joinNo.get("joinNo"), chatNo);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("채팅 삭제 성공", null));
	}

}
