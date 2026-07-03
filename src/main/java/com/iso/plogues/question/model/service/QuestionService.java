package com.iso.plogues.question.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.exception.user.NotPermissionException;
import com.iso.plogues.question.model.dao.QuestionMapper;
import com.iso.plogues.question.model.dto.QuestionDto;
import com.iso.plogues.question.model.vo.Question;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.page.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionMapper questionMapper;
	
	
	@Transactional
	public void save(QuestionDto question, CustomUserDetails user) {
		Question q = Question.builder()
							 .boardNo(question.getBoardNo())
							 .userId(user.getUsername())
							 .title(question.getTitle())
							 .content(question.getContent())
							 .category(question.getCategory())
							 .build();
		int result = questionMapper.save(q);
		
		if(result !=1 ) {
			throw new FailedInsertException("게시글 작성에 실패하였습니다. 다시 작성해주세요.");
		}

	}
	
	private PageInfo newPageInfo(int listCount, int page) {
		return PageInfo.of(listCount, page, 10, 5);
	}

	  @Transactional
	    public BoardResponse<QuestionDto> findByAll(int page, String category, CustomUserDetails user) {
		  

		    boolean isAdmin = user.getAuthorities().stream()
		            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
		    
		    

		    // count
		    int totalCount = isAdmin
		            ? questionMapper.listCount(category)
		            : questionMapper.listCountByUser(category, user.getUsername());

		    // pageInfo
		    PageInfo pageInfo = newPageInfo(totalCount, page);
		    
		    int limit = 10;
		    int offset = (page - 1) * limit; 
		    RowBounds rowBounds = new RowBounds(offset, limit);

		    // list
		    List<QuestionDto> list = isAdmin
		            ? questionMapper.findByAll(rowBounds, category)
		            : questionMapper.findByUser(rowBounds, category, user.getUsername());


		    // response
		    BoardResponse<QuestionDto> br = new BoardResponse<>();
		    br.setPage(pageInfo);
		    br.setBoard(list);
		    br.setMessage(list.isEmpty() ? "작성한 게시글이 존재하지 않습니다." : null);

		    return br;
	}

	  public QuestionDto findByOne(Long boardNo, CustomUserDetails user) {
	      QuestionDto result = questionMapper.findByOne(boardNo);

	      if (result == null) {
	          throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
	      }

	      if ("Y".equals(result.getDeleted())) {
	          throw new IllegalStateException("삭제된 게시글입니다.");
	      }

	      boolean isAdmin = user.getAuthorities().stream()
	              .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
	              
	      if (!isAdmin && !result.getUserId().equals(user.getUsername())) {
	          throw new NotPermissionException("본인이 작성한 문의글만 조회할 수 있습니다.");
	      }

	      return result;
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


