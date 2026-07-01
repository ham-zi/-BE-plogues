package com.iso.plogues.proof.model.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.iso.plogues.proof.model.vo.Proof;

@Mapper
public interface ProofMapper {

    int save(Proof p);

}
