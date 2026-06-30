package com.iso.plogues.user.model.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iso.plogues.user.model.vo.User;

@Mapper
public interface UserMapper {
	
	@Insert("INSERT INTO PLOGUES_USER VALUES(#{userId}, #{userPwd}, #{userName}, #{email}, #{phone}, #{address}, #{info}, #{role}, 'N')")
	int signUp(User user);
	
	@Select("SELECT COUNT(*) FROM PLOGUES_USER WHERE USER_ID = #{userId}")
	int countByUserId(String userId);

	@Update("UPDATE PLOGUES_USER SET DELETED = 'Y' WHERE USER_ID = #{username}")
	void deleteAccount(@Param(value="username")String username);

}
