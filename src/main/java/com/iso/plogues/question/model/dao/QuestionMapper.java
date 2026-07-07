package com.iso.plogues.question.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.iso.plogues.question.model.dto.QuestionDto;
import com.iso.plogues.question.model.vo.Question;

@Mapper
public interface QuestionMapper {

	int save(Question q);
    int listCount(@Param("category") String category, @Param("updated") String updated);
    List<QuestionDto> findByAll(RowBounds rowBounds, @Param("category") String category, @Param("updated") String updated);
    int listCountByUser(@Param("category") String category, @Param("userId") String user);
    List<QuestionDto> findByUser(RowBounds rowBounds, @Param("category") String category, @Param("userId") String userId);
	QuestionDto findByOne(Long boardNo);
	QuestionDto findBoardStatus(Long boardNo);
    int deleteByQuestion(Long boardNo);
	int restoreByQuestion(Long boardNo);
	int updateStatus(@Param("boardNo") Long boardNo);
	QuestionDto findByBoardNo(Long boardNo);

	

}
