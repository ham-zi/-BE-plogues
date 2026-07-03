package com.iso.plogues.notice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.notice.model.dto.NoticeDto;
import com.iso.plogues.notice.model.service.NoticeService;
import com.iso.plogues.util.dto.BoardResponse;

import jakarta.validation.Valid;
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
	
	@PatchMapping("/{noticeNo}")
	public ResponseEntity<ApiResponse<Void>> updateNotice(
	        @AuthenticationPrincipal CustomUserDetails userDetails,
	        @PathVariable(name = "noticeNo") Long noticeNo,
	        @Valid NoticeDto noticeDto,
	        @RequestParam(name = "files", required = false) List<MultipartFile> files) {
	    noticeService.updateNotice(userDetails, noticeNo, noticeDto, files);
	    return ResponseEntity.ok(ApiResponse.success("공지사항, 이벤트 수정 성공", null));
	}

	@DeleteMapping("/{noticeNo}")
	public ResponseEntity<ApiResponse<Void>> deleteNotice(
	        @AuthenticationPrincipal CustomUserDetails user,
	        @PathVariable(name = "noticeNo") Long noticeNo) {
	    noticeService.deleteNotice(user, noticeNo);
	    return ResponseEntity.ok(ApiResponse.success("공지사항, 이벤트 삭제 성공", null));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> insertNotice(
	        @AuthenticationPrincipal CustomUserDetails user,
	        @Valid NoticeDto noticeDto,
	        @RequestParam(name = "files", required = false) List<MultipartFile> files) {
	    noticeService.insertNotice(user, noticeDto, files);
	    return ResponseEntity.ok(ApiResponse.created("공지사항, 이벤트 작성 성공", null));
	}
}
