package com.iso.plogues.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.board.model.dto.BoardDto;

@Mapper
public interface BoardMapper {
	
	int countBoardList();

	List<BoardDto> selectBoardList(
	        @Param("offset") int offset,
	        @Param("limit") int limit
	);
}
