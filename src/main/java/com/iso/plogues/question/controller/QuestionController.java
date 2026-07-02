package com.iso.plogues.question.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.question.model.dto.QuestionDto;
import com.iso.plogues.question.model.service.QuestionService;
import com.iso.plogues.util.dto.BoardResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {
	private final QuestionService questionService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> save(@Valid @RequestBody QuestionDto question, 
									 			  @AuthenticationPrincipal CustomUserDetails user){
		questionService.save(question, user);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("게시글 작성에 성공하였습니다.", null));
	}
	
    @GetMapping
    public ResponseEntity<ApiResponse<BoardResponse<QuestionDto>>> findByAll(
            @RequestParam(name="page", defaultValue = "1") int page,
            @RequestParam(name="category", required = false) String category,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

    	  return ResponseEntity.ok(
                 ApiResponse.success(questionService.findByAll(page, category, user)));
	}
	
	@GetMapping("/{boardNo}")
	public ResponseEntity<ApiResponse<QuestionDto>> findByOne(@PathVariable(name="boardNo")Long boardNo) {
		QuestionDto question = questionService.findByOne(boardNo);
		return ResponseEntity.status(200).body(ApiResponse.success(question));
	}


	
	@DeleteMapping("/{boardNo}")
	public ResponseEntity<ApiResponse<Void>> deleteByQuestion(
	        @AuthenticationPrincipal CustomUserDetails user,
	        @PathVariable(name="boardNo") Long boardNo) {
	    
	    boolean isAdmin = user.getAuthorities().stream()
	            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")); 

	    questionService.deleteByQeustion(boardNo, user.getUsername(), isAdmin);
	    return ResponseEntity.ok().build();
	}
	

}
