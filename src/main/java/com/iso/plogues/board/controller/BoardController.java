package com.iso.plogues.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.board.model.service.BoardService;
import com.iso.plogues.template.board.BoardResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ApiResponse<BoardResponse<BoardDto>> selectBoardList(
            @RequestParam(name = "category") String category,
            @RequestParam(name = "page", defaultValue = "1") int page) {

        return ApiResponse.<BoardResponse<BoardDto>>builder()
                .code(200)
                .data(boardService.selectBoardList(page))
                .build();
    }

}
