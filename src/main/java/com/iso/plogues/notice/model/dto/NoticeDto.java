package com.iso.plogues.notice.model.dto;

import java.util.Date;
import java.util.List;

import com.iso.plogues.util.file.FileDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {
	 private Long noticeNo;
	    private String category;
	    private String title;
	    private String writer;
	    private String content;
	    private Date createDate;
	    private String updated;
	    private int views;
	    private String userId;
	    private List<FileDto> fileList;
	    private String thumbnailPath;
	
}
