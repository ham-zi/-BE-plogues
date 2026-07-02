package com.iso.plogues.join.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.join.chat.model.dto.ChatDto;
import com.iso.plogues.join.chat.model.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> saveChat(@AuthenticationPrincipal CustomUserDetails user, @RequestBody ChatDto chat) {
		chatService.saveChat(user, chat);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("채팅 작성 성공", null));
	}

}
