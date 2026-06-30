package com.iso.plogues.question.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.question.model.dto.QuestionDto;
import com.iso.plogues.question.model.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {
	private final QuestionService questionService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> save(@Valid @RequestBody QuestionDto question, 
									 @AuthenticationPrincipal CustomUserDetails user){
		questionService.save(question, user);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(null));
	}
	

}
