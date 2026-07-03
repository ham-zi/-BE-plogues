package com.iso.plogues.question.comment.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.question.comment.model.dao.AnswerMapper;
import com.iso.plogues.question.comment.model.dto.AnswerDto;
import com.iso.plogues.question.comment.model.vo.Answer;
import com.iso.plogues.question.model.dao.QuestionMapper;
import com.iso.plogues.question.model.dto.QuestionDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {
	private final AnswerMapper answerMapper;
    private final QuestionMapper questionMapper; // 원본 문의글 상태 변경을 위해 주입


    @Transactional
	public void saveComment(AnswerDto answer, Long boardNo, CustomUserDetails user) {
    	Answer a = Answer.builder()
			    		 .boardNo(boardNo)
    					 .userId(user.getUsername())
    					 .content(answer.getContent())
    					 .build();
    	
    	int result = answerMapper.saveComment(a);
		
		if(result !=1 ) {
			throw new FailedInsertException("답변 작성에 실패하였습니다. 다시 작성해주세요.");
		}
		
	}
    @Transactional(readOnly = true)
    public List<AnswerDto> findComment(Long boardNo, CustomUserDetails user) {

        validateQuestion(boardNo);

        List<AnswerDto> answers = answerMapper.findComment(boardNo);

        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException("등록된 답변이 없습니다.");
        }

        return answers;
    }
    @Transactional
    public void updateComment(AnswerDto answer, Long boardNo) {

        validateQuestion(boardNo);

        answer.setBoardNo(boardNo);

        int result = answerMapper.updateComment(answer);

        if (result != 1) {
            throw new IllegalStateException("답변 수정에 실패했습니다.");
        }
    }

    @Transactional
    public void deleteComment(Long boardNo) {

        validateQuestion(boardNo);

        int result = answerMapper.deleteComment(boardNo);

        if (result != 1) {
            throw new IllegalStateException("답변 삭제에 실패했습니다.");
        }
    }

    private void validateQuestion(Long boardNo) {

        QuestionDto question = questionMapper.findByBoardNo(boardNo);

        if (question == null) {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        }

        if ("Y".equals(question.getDeleted())) {
            throw new IllegalStateException("삭제된 게시글입니다.");
        }
    }
}