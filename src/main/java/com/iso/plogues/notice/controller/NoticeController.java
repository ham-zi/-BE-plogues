package com.iso.plogues.notice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.notice.model.dto.NoticeDto;
import com.iso.plogues.notice.model.service.NoticeService;
import com.iso.plogues.util.dto.BoardResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {
	
	private final NoticeService noticeService;

	@GetMapping
	public ResponseEntity<ApiResponse<BoardResponse<NoticeDto>>> selectNoticeList(
	        @RequestParam(name = "category") String category,
	        @RequestParam(name = "page", defaultValue = "1") int page) {
	    return ResponseEntity.ok(ApiResponse.success("공지사항, 이벤트 목록 조회 성공", noticeService.selectNoticeList(category, page)));
	}

	@GetMapping("/{noticeNo}")
	public ResponseEntity<ApiResponse<NoticeDto>> selectNoticeDetail(
	        @PathVariable("noticeNo") Long noticeNo) {
	    return ResponseEntity.ok(ApiResponse.success("공지사항, 이벤트 상세 조회 성공", noticeService.selectNoticeDetail(noticeNo)));
	}
}
