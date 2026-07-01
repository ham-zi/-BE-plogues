package com.iso.plogues.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
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
