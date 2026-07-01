package com.iso.plogues.proof.file.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.proof.file.model.dto.ProofFileDto;
import com.iso.plogues.proof.file.model.dao.ProofFileMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProofFileService {

	private final ProofFileMapper proofFileMapper;
	private Path fileLocation;

	@PostConstruct
	public void init() {
		this.fileLocation = Paths.get("uploads").toAbsolutePath().normalize();
	}

	// 파일 저장 (로컬)
	public void uploadFile(MultipartFile file, String changeName, String boardType) {

		Path targetLocation = this.fileLocation.resolve(boardType).resolve(changeName);

		try {
			Files.createDirectories(targetLocation.getParent());
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}
	}

	// 파일명 생성
	private String generateMyChangeName(String originName, int order) {

		return "iso_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ((int) (Math.random() * 900))
				+ 100 + "_" + order + originName.substring(originName.lastIndexOf("."));
	}

	@Transactional
	public void saveProofFiles(List<MultipartFile> files, Long proofNo) {

		if (files == null || files.isEmpty()) {
			return;
		}

		int uploadCount = Math.min(files.size(), 2);

		for (int i = 0; i < uploadCount; i++) {

			MultipartFile file = files.get(i);

			if (file == null || file.isEmpty()) {
				continue;
			}

			String boardType = "proof";
			String originName = file.getOriginalFilename();
			String changeName = generateMyChangeName(originName, i + 1);

			// 1. 로컬 저장
			uploadFile(file, changeName, boardType);

			// 2. DB 저장 객체 생성
			String filePath =
				    "http://localhost/uploads/" + boardType + "/" + changeName;


				ProofFileDto fileEntity = new ProofFileDto(
				    proofNo,
				    originName,
				    changeName,
				    filePath,
				    String.valueOf(i + 1)
				);


				proofFileMapper.saveFile(fileEntity);
		}
	}
}
