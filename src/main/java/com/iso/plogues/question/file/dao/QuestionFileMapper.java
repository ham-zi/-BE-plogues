package com.iso.plogues.question.file.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;

@Mapper
public interface QuestionFileMapper {

	@Insert("INSERT INTO QUESTION_FILE VALUES(SEQ_REVIEW_FILE.NEXTVAL, #{originName}, #{changeName}, #{filePath}, 'N', #{refBoardNo})")
	int saveFile(File file);
    
    @Select("SELECT FILE_NO AS fileNo, BOARD_NO AS refBoardNo, ORIGIN_NAME AS originName, CHANGE_NAME AS changeName, FILE_PATH AS filePath FROM QUESTION_FILE WHERE DELETED = 'N' AND BOARD_NO = #{boardNo} ORDER BY BOARD_NO ASC")
    List<FileDto> findByBno(Long boardNo);
    
    @Update("UPDATE QUESTION_FILE SET DELETED = 'Y' WHERE BOARD_NO = #{boardNo}")
    int deleteFile(Long boardNo);
    
    @Delete("DELETE FROM QUESTION_FILE WHERE BOARD_NO = #{boardNo}")
    int hardDeleteFile(Long boardNo);

}
