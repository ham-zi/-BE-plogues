package com.iso.plogues.join.file.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;

@Mapper
public interface JoinFileMapper {
	@Insert("INSERT INTO JOIN_FILE VALUES(SEQ_PLG_JBF.NEXTVAL, #{refBoardNo}, #{originName}, #{changeName}, #{filePath}, 'N')")
	int saveFile(File file);
	
	@Select("SELECT FILE_NO, JOIN_NO, ORIGIN_NAME, CHANGE_NAME, FILE_PATH FROM JOIN_FILE WHERE DELETED = 'N' AND JOIN_NO = #{joinNo}")
	List<FileDto> findByBno(Long joinNo);
	
	@Update("UPDATE JOIN_FILE SET DELETED = 'Y' WHERE JOIN_NO = #{joinNo}")
	int deleteFile(Long joinNo);
	
	@Delete("DELETE FROM JOIN_FILE WHERE JOIN_NO = #{joinNo}")
	int hardDeleteFile(Long joinNo);

}
