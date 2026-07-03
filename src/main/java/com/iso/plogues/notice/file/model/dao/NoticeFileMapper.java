package com.iso.plogues.notice.file.model.dao;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;

@Mapper
public interface NoticeFileMapper {

    @Select("SELECT FILE_NO, NOTICE_NO, ORIGIN_NAME, CHANGE_NAME, FILE_PATH FROM NOTICE_FILE WHERE DELETED = 'N' AND NOTICE_NO = #{noticeNo}")
    List<FileDto> findByBno(Long noticeNo);

    @Update("UPDATE NOTICE_FILE SET DELETED = 'Y' WHERE NOTICE_NO = #{noticeNo}")
    int deleteFile(Long noticeNo);

    @Delete("DELETE FROM NOTICE_FILE WHERE NOTICE_NO = #{noticeNo}")
    int hardDeleteFile(Long noticeNo);
    
    @Insert("INSERT INTO NOTICE_FILE VALUES(SEQ_NOTICE_FILE.NEXTVAL, #{originName}, #{changeName}, #{filePath}, 'N', #{refBoardNo})")
    int saveFile(File file);

}
