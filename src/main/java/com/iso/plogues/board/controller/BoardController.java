package com.iso.plogues.board.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import org.springframework.http.ResponseEntity;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.board.model.service.BoardService;
import com.iso.plogues.util.dto.BoardResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<ApiResponse<BoardResponse<BoardDto>>> selectBoardList(
            @RequestParam(name = "category") String category,
            @RequestParam(name = "page", defaultValue = "1") int page) {
        return ResponseEntity.ok(ApiResponse.success("게시글 목록 조회 성공", boardService.selectBoardList(page)));
    }

    @GetMapping("/{boardNo}")
    public ResponseEntity<ApiResponse<BoardDto>> selectBoardDetail(
            @PathVariable("boardNo") Long boardNo) {
        return ResponseEntity.ok(ApiResponse.success("게시글 상세 조회 성공", boardService.selectBoardDetail(boardNo)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> insertBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid BoardDto boardDto,
            @RequestParam(name = "files", required = false) List<MultipartFile> files) {
        boardDto.setUserId(userDetails.getUsername());
        boardService.insertBoard(boardDto, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("게시글 작성 성공", null));
    }
    
    @PatchMapping("/{boardNo}")
    public ResponseEntity<ApiResponse<Void>> updateBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "boardNo") Long boardNo,
            @Valid BoardDto boardDto,
            @RequestParam(name = "files", required = false) List<MultipartFile> files) {
        boardService.updateBoard(userDetails, boardNo, boardDto, files);
        return ResponseEntity.ok(ApiResponse.success("게시글 수정 성공", null));
    }
    
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable(name = "boardNo") Long boardNo) {
        boardService.deleteBoard(user, boardNo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.noContent("게시글 삭제 성공", null));
    }
}