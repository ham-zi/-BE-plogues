package com.iso.plogues.proof.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.proof.model.dto.ProofDto;
import com.iso.plogues.proof.model.vo.Proof;
import com.iso.plogues.util.page.PageInfo;

@Mapper
public interface ProofMapper {

    int save(Proof p);
    int listCount();
    List<ProofDto> findAll(PageInfo page);
    ProofDto findByProofNo(Long proofNo);

}
