package com.iso.plogues.board.comment.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.board.comment.model.dao.BoardCommentMapper;
import com.iso.plogues.board.comment.model.dto.BoardCommentDto;
import com.iso.plogues.board.model.dao.BoardMapper;
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedFindByNoException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.exception.FailedUpdateException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardCommentService {
	
		private final BoardMapper boardMapper;
	    private final BoardCommentMapper commentMapper;

	    @Transactional
	    public void insertComment(CustomUserDetails user, Long boardNo, BoardCommentDto commentDto) {

	        BoardDto board = boardMapper.selectBoardDetail(boardNo);
	        if (board == null) {
	            throw new FailedFindByNoException("존재하지 않거나 삭제된 게시글입니다.");
	        }

	        commentDto.setBoardNo(boardNo);
	        commentDto.setUserId(user.getUsername());
	        int result = commentMapper.insertComment(commentDto);
	        if (result != 1) {
	            throw new FailedInsertException("댓글 작성에 실패했습니다.");
	        }
	    }

	    @Transactional
	    public void updateComment(CustomUserDetails user, Long commentNo, BoardCommentDto commentDto) {
	        commentDto.setCommentNo(commentNo);
	        commentDto.setUserId(user.getUsername());
	        int result = commentMapper.updateComment(commentDto);
	        if (result != 1) {
	            throw new FailedUpdateException("댓글 수정에 실패했습니다.");
	        }
	    }

	    @Transactional
	    public void deleteComment(CustomUserDetails user, Long commentNo) {
	        int result = commentMapper.deleteComment(user.getUsername(), commentNo);
	        if (result != 1) {
	            throw new FailedDeleteException("댓글 삭제에 실패했습니다.");
	        }
	    }
	}

