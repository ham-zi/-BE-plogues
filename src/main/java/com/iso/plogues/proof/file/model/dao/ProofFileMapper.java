package com.iso.plogues.proof.file.model.dao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
			        #{file.refBoardNo},
			        #{file.originName},
			        #{file.changeName},
			        #{file.filePath},
			        #{fileLevel},
			        'N'
			    )
			""")
	int saveFile(
		    @Param("file") File file,
		    @Param("fileLevel") int fileLevel
		    );

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
	
	@Update("""
		    UPDATE PROOF_FILE
		       SET DELETED = 'Y'
		     WHERE PROOF_NO = #{proofNo}
		""")
		int deleteFile(Long proofNo);
	
	
	
	

}
