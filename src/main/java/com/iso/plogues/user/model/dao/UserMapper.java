package com.iso.plogues.user.model.dao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.user.model.dto.MyInfoDto;
import com.iso.plogues.user.model.dto.UserDto;
import com.iso.plogues.user.model.vo.User;

@Mapper
public interface UserMapper {
	
	@Select("""
				SELECT
				       U.USER_ID
				     , USER_NAME
				     , EMAIL
				     , PHONE
				     , ADDRESS
				     , INFO
				     , (SELECT COUNT(*) FROM PLOGUES_USER U LEFT JOIN JOIN_BOARD J USING(USER_ID) WHERE U.DELETED = 'N' AND J.DELETED = 'N' AND USER_ID = #{username} ) AS JOIN_COUNT
				     , (SELECT NVL(SUM(PB.QUANTITY),0) FROM PLOGUES_USER U JOIN JOIN_REQUEST JR ON(U.USER_ID =JR.USER_ID) JOIN PROOF_BOARD PB ON(JR.JOIN_NO = PB.JOIN_NO) WHERE U.DELETED = 'N' AND U.USER_ID = #{username} AND CATEGORY = 'plog') AS PLOG_WEIGHT
				     , (SELECT NVL(SUM(PB.QUANTITY),0) FROM PLOGUES_USER U JOIN JOIN_REQUEST JR ON(U.USER_ID =JR.USER_ID) JOIN PROOF_BOARD PB ON(JR.JOIN_NO = PB.JOIN_NO) WHERE U.DELETED = 'N' AND U.USER_ID = #{username} AND CATEGORY = 'tree') AS TREE_COUNT
				     , FILE_PATH || CHANGE_NAME AS FILE_URL
				  FROM
				       PLOGUES_USER U
				  LEFT
				  JOIN
				       USER_FILE F ON(F.USER_ID = U.USER_ID)
				 WHERE
				       U.DELETED = 'N'
				   AND
				       U.USER_ID = #{username}
			""")
	MyInfoDto selectMyInfo(CustomUserDetails user);

	@Insert("INSERT INTO PLOGUES_USER VALUES(#{userId}, #{userPwd}, #{userName}, #{email}, #{phone}, #{address}, #{info}, #{role}, 'N')")
	int signUp(User user);
	
	@Select("SELECT COUNT(*) FROM PLOGUES_USER WHERE USER_ID = #{userId}")
	int countByUserId(String userId);

	@Update("UPDATE PLOGUES_USER SET DELETED = 'Y' WHERE USER_ID = #{username}")
	void deleteAccount(@Param(value="username")String username);

	@Update("UPDATE PLOGUES_USER SET EMAIL = #{email}, INFO = #{info} WHERE USER_ID = #{userId} AND DELETED = 'N'")
	int patchMyInfo(UserDto userInfo);


}
