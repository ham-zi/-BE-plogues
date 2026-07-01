package com.iso.plogues.util.file;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.iso.plogues.exception.FileUploadException;

@Service
public class FileService {

	private final Path fileLocation;
	
	public FileService() {
		this.fileLocation = Paths.get("uploads").toAbsolutePath().normalize();
	}

	public String fileTransferTo(MultipartFile upfile, String changeName, String boardType) {
		Path targetLocation = this.fileLocation.resolve(boardType).resolve(changeName);
		try {
			Files.copy(upfile.getInputStream(), targetLocation);
			return "http://localhost/uploads/"+ boardType + "/"+ changeName;
		} catch (IOException e) {
			throw new FileUploadException("파일 업로드에 실패했습니다.");
		}
	}
	
	public void deleteFile(File file) throws IOException {
		Path path = this.fileLocation.resolve(file.getBoardType()).resolve(file.getChangeName());
		Files.deleteIfExists(path);
	}

}
