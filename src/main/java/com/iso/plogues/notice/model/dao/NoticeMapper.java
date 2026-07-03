package com.iso.plogues.notice.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.notice.model.dto.NoticeDto;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.page.PageInfo;

@Mapper
public interface NoticeMapper {
	
	int countNoticeList(@Param("category") String category);
    List<NoticeDto> selectNoticeList(@Param(value="category")String category, @Param(value="pi") PageInfo pi);
    
    NoticeDto selectNoticeDetail(@Param("noticeNo") Long noticeNo);
    List<FileDto> selectFileList(@Param("noticeNo") Long noticeNo);
    
    int updateNotice(@Param("notice") NoticeDto noticeDto);
    int increaseViewCount(Long noticeNo);
    int deleteNotice(@Param("noticeNo") Long noticeNo);
    
    int insertNotice(@Param("notice") NoticeDto noticeDto);
}
