package com.iso.plogues.question.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.question.comment.model.dto.AnswerDto;
import com.iso.plogues.question.comment.model.vo.Answer;
import com.iso.plogues.question.model.dto.QuestionDto;

@Mapper
public interface AnswerMapper {
	int saveComment(Answer a);
	List<AnswerDto> findComment(Long boardNo);
	int updateComment(AnswerDto answer);
	int deleteComment(@Param("boardNo")Long boardNo, @Param("answerNo")Long answerNo);
	List<AnswerDto> selectAnswerList(@Param("boardNo")Long boardNo);
	QuestionDto selectQuestionDetail(Long boardNo);
}
