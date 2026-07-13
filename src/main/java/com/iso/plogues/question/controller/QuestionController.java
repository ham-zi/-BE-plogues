package com.iso.plogues.question.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.question.comment.model.dto.AnswerDto;
import com.iso.plogues.question.comment.model.service.AnswerService;
import com.iso.plogues.question.model.dto.QuestionDto;
import com.iso.plogues.question.model.dto.QuestionRequest;
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
	private final AnswerService answerService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> save(@Valid @ModelAttribute QuestionRequest question, 
									 			  @AuthenticationPrincipal CustomUserDetails user,
									 			  @RequestParam(name="files",required = false) List<MultipartFile>files){
		question.setUserId(user.getUsername());
		questionService.save(question, files);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("게시글 작성에 성공하였습니다.", null));
	}
	
    @GetMapping
    public ResponseEntity<ApiResponse<BoardResponse<QuestionDto>>> findByAll(
            @RequestParam(name="page", defaultValue = "1") int page,
            @RequestParam(name="category", required = false) String category,
            @RequestParam(name="updated", required = false) String updated,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

    	  return ResponseEntity.ok(
                 ApiResponse.success(questionService.findByAll(page, category, user, updated)));
	}

    @GetMapping("/{boardNo}")
    public ResponseEntity<ApiResponse<QuestionDto>> selectQuestionDetail(@PathVariable(name="boardNo")Long boardNo,
    																	 @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(ApiResponse.success("게시글 상세 조회 성공", questionService.selectQuestionDetail(boardNo, user)));
    }
	
	@DeleteMapping("/{boardNo}/user")
    public ResponseEntity<ApiResponse<Void>> deleteQuestionUser(@AuthenticationPrincipal CustomUserDetails user,
            													@PathVariable(name="boardNo") Long boardNo) {
        
        questionService.deleteByUser(boardNo, user.getUsername());
        return ResponseEntity.status(200).body(ApiResponse.created("게시글을 성공적으로 삭제하였습니다.", null));
    }

	@DeleteMapping("/{boardNo}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuestionAdmin(@PathVariable(name="boardNo") Long boardNo) {
        
        questionService.deleteByAdmin(boardNo);
        return ResponseEntity.ok().body(ApiResponse.created("게시글을 성공적으로 삭제하였습니다.", null));
    }
	
	@PatchMapping("/{boardNo}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ApiResponse<Void>> restoreQuestionAdmin(@PathVariable(name="boardNo") Long boardNo) {
	    
	    questionService.restoreByAdmin(boardNo);
	    return ResponseEntity.ok().body(ApiResponse.created("게시글을 성공적으로 복구하였습니다.", null));
	}
	
	
	// 답변 CRUD
	@PostMapping("/{boardNo}/comments")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ApiResponse<Void>> saveComment(@Valid @RequestBody AnswerDto answer,
														 @PathVariable(name="boardNo") Long boardNo,
														 @AuthenticationPrincipal CustomUserDetails user) {
		answerService.saveComment(answer, boardNo, user);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("답변 작성에 성공하였습니다.", null));
	}
	
	/*
	@GetMapping("/{boardNo}/comments")
	public ResponseEntity<ApiResponse<List<AnswerDto>>> findComment(@PathVariable(name="boardNo") Long boardNo,
	        														@AuthenticationPrincipal CustomUserDetails user) {

	    List<AnswerDto> answers = answerService.findComment(boardNo, user);

	    return ResponseEntity.ok(ApiResponse.success(answers));
	}
	*/
	
	
	@PatchMapping("/{boardNo}/comments/{answerNo}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ApiResponse<Void>> updateComment(@Valid @RequestBody AnswerDto answer,
														   @PathVariable(name="boardNo") Long boardNo,
														   @PathVariable(name="answerNo") Long answerNo) {
		answer.setAnswerNo(answerNo);
		answerService.updateComment(answer, boardNo);												   
		return ResponseEntity.ok().body(ApiResponse.success("답변이 성공적으로 수정되었습니다.", null));
	}
	
	
	@DeleteMapping("/{boardNo}/comments/{answerNo}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable(name="boardNo") Long boardNo,
			                                               @PathVariable(name="answerNo") Long answerNo){
	
		answerService.deleteComment(boardNo, answerNo);
		return ResponseEntity.ok().body(ApiResponse.created("답변을 성공적으로 삭제하였습니다.", null));
	}
	

}
