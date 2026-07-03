package com.iso.plogues.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.report.model.dto.ReportDto;
import com.iso.plogues.report.model.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
	
	private final ReportService reportService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> saveReport(@AuthenticationPrincipal CustomUserDetails user,
														@Valid @RequestBody ReportDto report) {
		reportService.saveReport(user, report);
		
		return ResponseEntity.status(200).body(ApiResponse.success("신고가 접수되었습니다.", null));
	}
	

}
