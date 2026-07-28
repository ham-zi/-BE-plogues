package com.iso.plogues.util.file;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class File {
	private Long fileNo; 
	private Long refBoardNo;
	private String refUserId;
	private String originName;
	private String changeName;
	private String filePath;
	private String deleted;
	private List<String>extensions = List.of(".jpg", ".png", ".jpeg", ".svg", ".heif", ".heic", ".gif");
	@Value("${cloud.region.satatic}")
	private String region;
	@Value("${cloud.s3.bucket}")
	private String bucketName;
	
	public static File of(Long refBoardNo, String originName) {
		return new File(refBoardNo,originName);
	}
	
	public static File of(String refUserId, String originName) {
		return new File(refUserId,originName);
	}
	
	private File(Long refBoardNo, String originName) {
		validOriginName(originName);
		validRefBoardNo(refBoardNo);
		validExtension(originName);
		this.refBoardNo = refBoardNo;
		this.originName = originName;
		this.changeName = getChangeName(originName);
		this.filePath = "https://"+ region + ".s3." + bucketName + ".amazonaws.com/";
		this.deleted = "N";
	}
	
	private File(String refUserId, String originName) {
		validOriginName(originName);
		validExtension(originName);
		this.refUserId = refUserId;
		this.originName = originName;
		this.changeName = getChangeName(originName);
		this.filePath = "https://"+ region + ".s3." + bucketName + ".amazonaws.com/";
		this.deleted = "N";
	}
	

	
	private String getChangeName(String originName) {
		StringBuilder sb = new StringBuilder();
		sb.append("iso_");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		sb.append(((int)(Math.random()*900))+100);
		sb.append(originName.substring(originName.lastIndexOf(".")));
		return sb.toString();
	}
	
	private void validRefBoardNo(Long refBoardNo) {
		if(refBoardNo<1) {
			throw new IllegalArgumentException("게시글번호가 잘못입력되었습니다.");
		}
	}
	
	private void validOriginName(String originName) {
		if(originName == null || originName.trim() == "") {
			throw new IllegalArgumentException("잘못된 파일입니다.");
		}
	}
	
	private void validExtension(String originName) {
		String extension = originName.substring(originName.lastIndexOf("."));
		if(!extensions.contains(extension)) {
			throw new IllegalArgumentException("잘못된 확장자의 파일입니다.");
		}
	}
	
}