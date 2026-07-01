package com.iso.plogues.proof.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.proof.model.dto.ProofDto;
import com.iso.plogues.proof.model.vo.Proof;

@Mapper
public interface ProofMapper {

    int save(Proof p);
    ProofDto findByProofNo(Long proofNo);

}
