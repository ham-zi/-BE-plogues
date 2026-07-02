package com.iso.plogues.user.file;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.util.file.File;

@Mapper
public interface FileMapper {
	
	@Insert("INSERT INTO USER_FILE VALUES (#{username}, #{user.originName}, #{user.changeName}, #{user.filePath}) ")
	int saveFile(@Param(value="username")String username,@Param(value="user")File user);

	@Delete("DELETE FROM USER_FILE WHERE USER_ID = #{username}")
	int deleteFile(@Param(value="username")String username);
}
