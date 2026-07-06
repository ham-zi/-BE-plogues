package com.iso.plogues.tree.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.iso.plogues.tree.model.dto.TreeDto;
import com.iso.plogues.tree.model.vo.Tree;

@Mapper
public interface TreeMapper {
	@Insert("INSERT INTO TREE_ENVIRONMENTS VALUES(SEQ_TREE_ENVIRONMENTS.NEXTVAL, #{zoneName}, SYSDATE, #{temperature}, #{humidity}, #{soilMoisture})")
	void saveTreeEnvironments(Tree tree);
	
	@Select("""
				SELECT 
				       TEMPERATURE
				     , HUMIDITY
				     , SOILMOISTURE
				     , MEASURE_TIME
				  FROM
				       TREE_ENVIRONMENTS
				 WHERE
				       ZONE_NAME = 'A01'
				   AND
				       MEASURE_TIME BETWEEN SYSDATE - 7 AND SYSDATE
				 ORDER
				    BY
				       MEASURE_TIME ASC
			""")
	List<TreeDto> findDataByWeek();
}
