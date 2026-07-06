package com.iso.plogues.token.model.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.token.model.vo.RefreshToken;

@Mapper
public interface TokenMapper {
	
	@Insert("INSERT INTO PLOGUES_TOKEN VALUES(#{userId}, #{token}, #{expiration})")
	void saveToken(RefreshToken token);
	
	@Select("SELECT USER_ID, TOKEN, EXPIRATION FROM PLOGUES_TOKEN WHERE TOKEN = #{token}")
	RefreshToken findByToken(String token);
	
	@Delete("DELETE FROM PLOGUES_TOKEN WHERE USER_ID = #{userId} AND token = #{refreshToken}")
	void deleteToken(@Param(value = "userId") String userId, @Param(value="refreshToken")String refreshToken);
}
