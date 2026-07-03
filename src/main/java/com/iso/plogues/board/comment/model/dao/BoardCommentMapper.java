package com.iso.plogues.board.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.board.comment.model.dto.BoardCommentDto;

@Mapper
public interface BoardCommentMapper {
	    List<BoardCommentDto> selectCommentList(@Param("boardNo") Long boardNo);
	    int insertComment(BoardCommentDto commentDto);
	    int updateComment(BoardCommentDto commentDto);
	    int deleteComment(@Param("userId") String userId, @Param("commentNo") Long commentNo);
	}

