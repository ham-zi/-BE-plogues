package com.iso.plogues.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

        return ResponseEntity.ok(
                ApiResponse.<BoardResponse<BoardDto>>builder()
                        .code(200)
                        .message("게시글 목록 조회 성공 !")
                        .data(boardService.selectBoardList(page))
                        .build()
        );
    }
    
    @GetMapping("/{boardNo}")
    public ResponseEntity<ApiResponse<BoardDto>> selectBoardDetail(
            @PathVariable("boardNo") Long boardNo) {
    	
        return ResponseEntity.ok(
                ApiResponse.<BoardDto>builder()
                        .code(200)
                        .message("게시글 상세 조회 성공 !")
                        .data(boardService.selectBoardDetail(boardNo))
                        .build()
        );
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> insertBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid BoardDto boardDto,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        
        boardDto.setUserId(userDetails.getUsername()); 
        boardService.insertBoard(boardDto, files);
        
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .message("게시글 작성 성공")
                        .build()
        );
    }
}