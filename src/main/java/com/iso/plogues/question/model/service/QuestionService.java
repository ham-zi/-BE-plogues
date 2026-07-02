package com.iso.plogues.question.model.service;

import java.util.List;

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

		    // list
		    List<QuestionDto> list = isAdmin
		            ? questionMapper.findByAll(pageInfo, category)
		            : questionMapper.findByUser(pageInfo, category, user.getUsername());


		    // response
		    BoardResponse<QuestionDto> br = new BoardResponse<>();
		    br.setPage(pageInfo);
		    br.setBoard(list);
		    br.setMessage(list.isEmpty() ? "작성한 게시글이 존재하지 않습니다." : null);

		    return br;
	}

	  public QuestionDto findByOne(Long boardNo) {

		    QuestionDto result = questionMapper.findByOne(boardNo);

		    if (result == null) {
		        throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
		    }

		    return result;
		}

	  @Transactional
	  public void deleteByQeustion(Long boardNo, String username, boolean isAdmin) {

	      boolean isAuthor = isAuthor(boardNo, username);

	      if (!isAdmin && !isAuthor) {
	          throw new NotPermissionException("삭제 권한이 없습니다.");
	      }

	      questionMapper.deleteByQuestion(boardNo);
	  }
	  public boolean isAuthor(Long boardNo, String username) {
	    String author = questionMapper.findAuthorByBoardNo(boardNo); // 작성자 ID 조회 매퍼 필요
	    
	    return author != null && author.equals(username);
	}






}
