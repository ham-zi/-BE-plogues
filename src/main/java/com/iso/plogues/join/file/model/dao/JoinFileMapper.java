package com.iso.plogues.join.file.model.dao;

import org.apache.ibatis.annotations.Insert;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.util.file.File;



@Mapper
public interface JoinFileMapper {
	@Insert("INSERT INTO JOIN_FILE VALUES(SEQ_PLG_JBF.NEXTVAL, #{refBoardNo}, #{originName}, #{changeName}, #{filePath}, 'N')")
	public int saveFile(File file);

}
