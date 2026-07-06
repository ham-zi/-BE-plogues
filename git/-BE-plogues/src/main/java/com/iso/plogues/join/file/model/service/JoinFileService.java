package com.iso.plogues.join.file.model.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.join.file.model.dao.JoinFileMapper;
import com.iso.plogues.util.file.File;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinFileService {
	private final JoinFileMapper fileMapper;
	
	@Transactional
	public void saveFile(MultipartFile file, Long refBno, String boardType) {
		File fileEntity = File.of(refBno, file.getOriginalFilename(), boardType);
		fileMapper.saveFile(fileEntity);
	}

}
