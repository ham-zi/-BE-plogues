package com.iso.plogues.question.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedFindByNoException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.exception.user.NotPermissionException;
import com.iso.plogues.question.comment.model.dao.AnswerMapper;
import com.iso.plogues.question.comment.model.dto.AnswerDto;
import com.iso.plogues.question.file.service.QuestionFileService;
import com.iso.plogues.question.model.dao.QuestionMapper;
import com.iso.plogues.question.model.dto.QuestionDto;
import com.iso.plogues.question.model.dto.QuestionRequest;
import com.iso.plogues.question.model.vo.Question;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.page.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionMapper questionMapper;
	private final QuestionFileService questionFileService;
	private final AnswerMapper answerMapper;
	
	
	@Transactional
	public void save(QuestionRequest question, List<MultipartFile>files) {
		Question q = Question.builder()
							 .userId(question.getUserId())
							 .title(question.getTitle())
							 .content(question.getContent())
							 .category(changeCategory(question.getCategory()))
							 .build();
		int result = questionMapper.save(q);
		
		if(result !=1 ) {
			throw new FailedInsertException("게시글 작성에 실패하였습니다. 다시 작성해주세요.");
		}
        Long boardNo = q.getBoardNo();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile upfile : files) {
                questionFileService.saveFile(upfile, boardNo);
            }
        }
		

	}
	
	private PageInfo newPageInfo(int listCount, int page) {
		return PageInfo.of(listCount, page, 10, 5);
	}

	  @Transactional
	    public BoardResponse<QuestionDto> findByAll(int page, String category, CustomUserDetails user, String updated) {
		  	String changeUpdated = changeUpdated(updated);
		  	String changeCategory = changeCategory(category);
		    boolean isAdmin = user.getAuthorities().stream()
		            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
		    int totalCount = isAdmin
		            ? questionMapper.listCount(changeCategory, changeUpdated)
		            : questionMapper.listCountByUser(changeCategory, user.getUsername());
		    PageInfo pageInfo = newPageInfo(totalCount, page);
		    int limit = 10;
		    int offset = (page - 1) * limit; 
		    RowBounds rowBounds = new RowBounds(offset, limit);
		    List<QuestionDto> list = isAdmin
		            ? questionMapper.findByAll(rowBounds, changeCategory, changeUpdated)
		            : questionMapper.findByUser(rowBounds, changeCategory, user.getUsername());
		    BoardResponse<QuestionDto> br = new BoardResponse<>();
		    br.setPage(pageInfo);
		    br.setBoard(list);
		    return br;
	}
	  
	  private String changeUpdated(String updated) {
		  if("답변대기".equals(updated)) {
			  updated = "WAITING";
			  return updated;
		  }
		  if("처리완료".equals(updated)) {
			  updated = "COMPLETED";
			  return updated;
		  }
		  if(updated == null || !"ALL".equals(updated)) {
			  updated = "ALL";
		  }
		  return updated;
	  }
	  
	  private String changeCategory(String category) {
		  if("에러".equals(category)) {
			  category = "ERROR";
			  return category;
		  }
		  if("이벤트".equals(category)) {
			  category = "EVENT";
			  return category;
		  }
		  if(category == null || !"ALL".equals(category)) {
			  category = "ALL";
		  }
		  return category;
	  }
	 


	  @Transactional
	    public QuestionDto selectQuestionDetail(Long boardNo, CustomUserDetails user) {
	        QuestionDto question = questionMapper.findByOne(boardNo);
	        log.info("{}",user.getAuthorities());
	        if (question == null) {
	            throw new FailedFindByNoException("존재하지 않는 게시글입니다.");
	        }
	        if(!(question.getUserId().equals(user.getUsername())||"[ROLE_ADMIN]".equals(user.getAuthorities().toString()))) {
	        	throw new NotPermissionException("권한이 없는 게시글입니다.");
	        }
	        List<FileDto> files = questionFileService.findByBno(boardNo);
	        List<AnswerDto> answerList = answerMapper.selectAnswerList(boardNo);
	        question.setAnswerList(answerList); 
	        question.setFiles(files);
	        return question;
	    }
	  
	  	// 유저용 삭제
	    @Transactional
	    public void deleteByUser(Long boardNo, String username) {
	        QuestionDto question = questionMapper.findBoardStatus(boardNo);
	        validateBoard(question);

	        if (!username.equals(question.getUserId())) {
	            throw new NotPermissionException("본인이 작성한 문의글만 삭제할 수 있습니다.");
	        }
	        
	        int result = questionMapper.deleteByQuestion(boardNo); 
	        if (result != 1) {
	            throw new FailedInsertException("게시글 삭제에 실패하였습니다.");
	        }
 
	    }

	    // 관리자용 삭제
	    @Transactional
	    public void deleteByAdmin(Long boardNo) {
	        QuestionDto question = questionMapper.findBoardStatus(boardNo);
	        validateBoard(question);

	        int result = questionMapper.deleteByQuestion(boardNo); 
	        if (result != 1) {
	            throw new FailedInsertException("게시글 삭제에 실패하였습니다.");
	        }
	    }

	    private void validateBoard(QuestionDto question) {
	        if (question == null) {
	            throw new IllegalArgumentException("존재하지 않는 문의글입니다.");
	        }
	        if ("Y".equals(question.getDeleted())) {
	            throw new IllegalStateException("이미 삭제 처리된 게시글입니다.");
	        }
	    }

	    @Transactional
	    public void restoreByAdmin(Long boardNo) {
	        QuestionDto question = questionMapper.findBoardStatus(boardNo);
	        
	        if (question == null) {
	            throw new IllegalArgumentException("존재하지 않는 문의글입니다.");
	        }
	        if ("N".equals(question.getDeleted())) {
	            throw new IllegalStateException("삭제되지 않은 정상 게시글입니다.");
	        }
	        
	        int result = questionMapper.restoreByQuestion(boardNo); 
	        if (result != 1) {
	            throw new FailedInsertException("게시글 삭제에 실패하였습니다.");
	        }
	        
	    }

	}


