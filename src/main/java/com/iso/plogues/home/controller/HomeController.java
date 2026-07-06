package com.iso.plogues.home.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.home.model.service.HomeService;
import com.iso.plogues.home.model.vo.HomeResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
@Slf4j
public class HomeController {
	private final HomeService homeService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<HomeResponse>> getWelcomeInfo(){
		return ResponseEntity.ok().body(ApiResponse.success("웰컴페이지 조회 성공", homeService.getWelcomInfo()));
	}
}
