package com.iso.plogues.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.report.model.dto.ReportDto;
import com.iso.plogues.report.model.dto.ReportRequestDto;
import com.iso.plogues.report.model.service.ReportService;
import com.iso.plogues.util.dto.BoardResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {
	
	private final ReportService reportService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> saveReport(@AuthenticationPrincipal CustomUserDetails user,
														@Valid @RequestBody ReportDto report) {
		reportService.saveReport(user, report);
		
		return ResponseEntity.status(200).body(ApiResponse.success("신고가 접수되었습니다.", null));
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<BoardResponse<ReportDto>>> findAll(
			@RequestParam(name = "page", defaultValue = "1") int page, @Valid @ModelAttribute ReportRequestDto request) {
		BoardResponse<ReportDto> br = reportService.findAll(page, request);
		
		return ResponseEntity.status(200).body(ApiResponse.success("신고게시판 전체 조회 성공", br));
	}
	
	@PatchMapping("/{reportNo}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> completeReport(@PathVariable(value="reportNo")Long reportNo) {
		reportService.completeReport(reportNo);
		return ResponseEntity.ok().body(ApiResponse.success("신고 처리 완료했습니다.", null));
	}
	
	

}
