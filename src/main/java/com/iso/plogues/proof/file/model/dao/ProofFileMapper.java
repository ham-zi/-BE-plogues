package com.iso.plogues.proof.file.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;

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
			        #{refBoardNo},
			        #{originName},
			        #{changeName},
			        #{filePath},
			        1,
			        'N'
			    )
			""")
	int saveFile(File file);

	@Select("""
			    SELECT
			           FILE_NO
			         , PROOF_NO
			         , ORIGIN_NAME
			         , CHANGE_NAME
			         , FILE_PATH
			      FROM
			           PROOF_FILE
			     WHERE
			           PROOF_NO = #{proofNo}
			       AND
			           DELETED = 'N'
			""")

	List<FileDto> findByBno(Long proofNo);

}
