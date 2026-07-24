package com.iso.plogues.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.board.model.service.BoardService;
import com.iso.plogues.util.dto.BoardResponse;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;


@RestController
@RequestMapping("/api/boards")
@Validated
public class BoardController {

    private final BoardService boardService;
    private final Counter viewCounter;
    private final Gauge boardCounter;
    private final MeterRegistry registry;
    private String kw;
    
    public BoardController(MeterRegistry registry, BoardService boardService) {
    	this.kw= "";
    	this.boardService = boardService;
    	Timer.Sample sample = Timer.start(registry);
        this.viewCounter = Counter.builder("board_view_total")
                .description("게시글 조회 횟수")
                .register(registry);
        sample.stop(registry.timer("board_view_duration"));
        this.boardCounter = Gauge.builder("board_count_current", boardService, service -> service.countAll(kw)).description("현재 게시글 수").register(registry);
        this.registry= registry;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<BoardResponse<BoardDto>>> selectBoardList(
            @RequestParam(name = "category") String category,
            @RequestParam(name = "page", defaultValue = "1") @Positive(message = "페이지 번호는 1 이상이어야 합니다.") int page,
            @RequestParam(name = "keyword", required = false) String keyword) {
    	kw = keyword;
    	Timer.Sample sample = Timer.start(registry);
    	 BoardResponse br = boardService.selectBoardList(page, keyword);
    	 sample.stop(registry.timer("board_list_duration"));
        return ResponseEntity.ok(ApiResponse.success("게시글 목록 조회 성공",br));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolation(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.badRequest("입력값 검증에 실패했습니다.", messages));
    }
    
    @GetMapping("/{boardNo}")
    public ResponseEntity<ApiResponse<BoardDto>> selectBoardDetail(
            @PathVariable("boardNo") Long boardNo) {
    		viewCounter.increment();
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
            @RequestParam(name = "files", required = false) List<MultipartFile> files,
            @RequestParam(name = "deleteFileNos", required = false) List<Long> deleteFileNos) {
        boardService.updateBoard(userDetails, boardNo, boardDto, files, deleteFileNos);
        return ResponseEntity.ok(ApiResponse.success("게시글 수정 성공", null));
    }
    
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable(name = "boardNo") Long boardNo) {
        boardService.deleteBoard(user, boardNo);
        return ResponseEntity.ok(ApiResponse.success("게시글 삭제 성공", null));
    }
   
}