package com.iso.plogues.proof.file.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProofFileDto {

    private Long proofNo;
    private String originName;
    private String changeName;
    private String filePath;
    private String fileLevel;

}