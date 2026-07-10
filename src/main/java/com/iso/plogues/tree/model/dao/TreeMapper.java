package com.iso.plogues.tree.model.dao;

import java.util.List;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.iso.plogues.tree.model.dto.TreeResponse;
import com.iso.plogues.tree.model.vo.Tree;

@Mapper
public interface TreeMapper {
	@Insert("INSERT INTO TREE_ENVIRONMENTS VALUES(SEQ_TREE_ENVIRONMENTS.NEXTVAL, #{zoneName}, SYSDATE, #{temperature}, #{humidity}, #{soilMoisture})")
	void saveTreeEnvironments(Tree tree);
	
	@Select("""
	        SELECT
	               ROUND(AVG(TEMPERATURE), 1) AS TEMPERATURE
	             , ROUND(AVG(HUMIDITY), 1) AS HUMIDITY
	             , ROUND(AVG(SOILMOISTURE), 1) AS SOILMOISTURE
	             , TO_CHAR(HOUR_TIME, 'YYYY-MM-DD HH24:MI:SS') AS MEASURE_TIME
	          FROM (
	                SELECT
	                       TEMPERATURE
	                     , HUMIDITY
	                     , SOILMOISTURE
	                     , TRUNC(CAST(MEASURE_TIME AS DATE), 'HH24') AS HOUR_TIME
	                  FROM TREE_ENVIRONMENTS
	                 WHERE ZONE_NAME = 'A01'
	                   AND MEASURE_TIME BETWEEN SYSDATE - 1 AND SYSDATE
	               )
	         GROUP BY HOUR_TIME
	         ORDER BY HOUR_TIME ASC
	        """)
	List<TreeResponse> findDataByDay();
}
