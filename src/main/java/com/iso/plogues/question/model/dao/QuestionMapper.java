package com.iso.plogues.question.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.question.model.dto.QuestionDto;
import com.iso.plogues.question.model.vo.Question;
import com.iso.plogues.util.page.PageInfo;

@Mapper
public interface QuestionMapper {

	int save(Question q);
    int listCount(@Param("category") String category);
    List<QuestionDto> findByAll(@Param("pageInfo") PageInfo pageInfo, @Param("category") String category);
	QuestionDto findByOne(Long boardNo);
	void deleteByQuestion(Long boardNo);
	String findAuthorByBoardNo(Long boardNo);

	

}
