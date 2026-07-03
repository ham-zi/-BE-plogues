package com.iso.plogues.tree.model.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import com.iso.plogues.tree.model.vo.Tree;

@Mapper
public interface TreeMapper {
	@Insert("INSERT INTO TREE_ENVIRONMENTS VALUES(SEQ_TREE_ENVIRONMENTS.NEXTVAL, #{zoneName}, SYSDATE, #{temperature}, #{humidity}, #{soilMoisture})")
	void saveTreeEnvironments(Tree tree);
}
