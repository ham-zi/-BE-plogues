package com.iso.plogues.board.comment.controller;

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
import com.iso.plogues.board.comment.model.dto.BoardCommentDto;
import com.iso.plogues.board.comment.model.service.BoardCommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boards/{boardNo}/comments")
@RequiredArgsConstructor
public class BoardCommentController {
	 private final BoardCommentService commentService;

	    @PostMapping
	    public ResponseEntity<ApiResponse<Void>> insertComment(
	            @AuthenticationPrincipal CustomUserDetails user,
	            @PathVariable("boardNo") Long boardNo,
	            @Valid @RequestBody BoardCommentDto commentDto) {
	        commentService.insertComment(user, boardNo, commentDto);
	        return ResponseEntity.ok(ApiResponse.created("댓글 작성 성공", null));
	    }

	    @PatchMapping("/{commentNo}")
	    public ResponseEntity<ApiResponse<Void>> updateComment(
	            @AuthenticationPrincipal CustomUserDetails user,
	            @PathVariable("commentNo") Long commentNo,
	            @Valid @RequestBody BoardCommentDto commentDto) {
	        commentService.updateComment(user, commentNo, commentDto);
	        return ResponseEntity.ok(ApiResponse.success("댓글 수정 성공", null));
	    }

	    @DeleteMapping("/{commentNo}")
	    public ResponseEntity<ApiResponse<Void>> deleteComment(
	            @AuthenticationPrincipal CustomUserDetails user,
	            @PathVariable("commentNo") Long commentNo) {
	        commentService.deleteComment(user, commentNo);
	        return ResponseEntity.ok(ApiResponse.success("댓글 삭제 성공", null));
	    }
	}

