package com.iso.plogues.auth.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.iso.plogues.user.model.dto.UserDto;

@Mapper	
public interface AuthMapper {
	
	@Select("""
			   SELECT  
					  USER_ID
					, USER_PWD
					, USER_NAME
					, EMAIL
					, PHONE
					, ADDRESS
					, INFO
					, ROLE
					, DELETED
				 FROM 
				 	  PLOGUES_USER
			    WHERE 
				      DELETED = 'N'
				  AND
				      USER_ID = #{username}
			
			""")
	UserDto loadUser(String username);

}
