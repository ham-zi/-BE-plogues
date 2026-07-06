package com.iso.plogues.board.model.dto;

import java.util.Date;
import java.util.List;

import com.iso.plogues.board.comment.model.dto.BoardCommentDto;
import com.iso.plogues.util.file.FileDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {
	private Long boardNo;
	@NotBlank(message = "제목은 필수입니다.")
    private String title;
    private String writer;
    private Date createDate;
    private int views;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    private String userId;    
    private List<FileDto> fileList;
    private List<BoardCommentDto> commentList;
}
