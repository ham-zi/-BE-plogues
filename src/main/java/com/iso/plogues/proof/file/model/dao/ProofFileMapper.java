package com.iso.plogues.proof.file.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.proof.file.model.dto.ProofFileDto;
import com.iso.plogues.file.FileDto;

@Mapper
public interface ProofFileMapper {

	@Insert("""
	        INSERT INTO PROOF_FILE
	        (
	            FILE_NO,
	            PROOF_NO,
	            ORIGIN_NAME,
	            CHANGE_NAME,
	            FILE_PATH,
	            FILE_LEVEL,
	            DELETED
	        )
	        VALUES
	        (
	            SEQ_PLG_PROOF_FILE.NEXTVAL,
	            #{proofNo},
	            #{originName},
	            #{changeName},
	            #{filePath},
	            #{fileLevel},
	            'N'
	        )
	    """)
	    int saveFile(ProofFileDto file);

	}


