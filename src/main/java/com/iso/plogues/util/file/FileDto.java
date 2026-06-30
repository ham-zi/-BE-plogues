package com.iso.plogues.util.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
	private Long fileNo; 
	private Long refBoardNo;
	private String originName;
	private String changeName;
	private String filePath;
	private String deleted;
}
