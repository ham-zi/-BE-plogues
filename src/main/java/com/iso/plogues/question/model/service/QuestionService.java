package com.iso.plogues.question.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.question.model.dao.QuestionMapper;
import com.iso.plogues.question.model.dto.QuestionDto;
import com.iso.plogues.question.model.vo.Question;

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
	

}
