package com.iso.plogues.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.page.PageInfo;

@Mapper
public interface BoardMapper {

    int countBoardList();

    List<BoardDto> selectBoardList(PageInfo pi);
}
