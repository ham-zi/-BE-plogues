package com.iso.plogues.question.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.question.comment.model.dto.AnswerDto;
import com.iso.plogues.question.comment.model.vo.Answer;

@Mapper
public interface AnswerMapper {
	int saveComment(Answer a);
	List<AnswerDto> findComment(Long boardNo);
	int updateComment(AnswerDto answer);
	int deleteComment(Long boardNo);


}
