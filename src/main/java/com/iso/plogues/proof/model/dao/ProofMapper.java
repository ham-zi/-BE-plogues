package com.iso.plogues.proof.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.proof.model.dto.ProofDto;
import com.iso.plogues.proof.model.vo.Proof;
import com.iso.plogues.tree.model.dto.TreeCountDto;
import com.iso.plogues.util.page.PageInfo;

@Mapper
public interface ProofMapper {

	int save(Proof p);

	int listCount(String category);

	List<ProofDto> findAll(@Param(value="pi")PageInfo pi,@Param(value="category") String category);

	ProofDto findByProofNo(Long proofNo);

	int deleteProof(@Param("userId") String userId, @Param("proofNo") Long proofNo);

	int updateProof(Proof proof);
	
	List<TreeCountDto>treeCountByYear();
}
