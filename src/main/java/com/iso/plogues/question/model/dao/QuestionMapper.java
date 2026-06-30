package com.iso.plogues.question.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.question.model.vo.Question;

@Mapper
public interface QuestionMapper {

	int save(Question q);
	

}
