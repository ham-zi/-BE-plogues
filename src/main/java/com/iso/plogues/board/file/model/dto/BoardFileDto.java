package com.iso.plogues.board.file.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardFileDto {
	private Long fileNo;
    private String originName;
    private String changeName;
    private String filePath;
}
