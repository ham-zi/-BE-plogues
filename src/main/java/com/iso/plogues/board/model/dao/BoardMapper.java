package com.iso.plogues.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.page.PageInfo;

@Mapper
public interface BoardMapper {

    int countBoardList();
    int countMyBoardList(String userId);
    List<BoardDto> selectMyBoardList(@Param(value="userId")String userId, @Param(value="pi")PageInfo pi);
    List<BoardDto> selectBoardList(PageInfo pi);
    BoardDto selectBoardDetail(Long boardNo);
	List<FileDto> selectFileList(Long boardNo);
	void insertBoard(BoardDto boardDto);
	void insertFile(File file);
	int updateBoard(BoardDto boardDto);
	
	int deleteBoard(@Param(value="userId") String userId, @Param(value="boardNo") Long boardNo);
	
}


