package com.lamar.box.file.service;

import java.io.IOException;
import java.util.List;

import com.lamar.box.file.entity.File;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	public String getUserIDFromToken(String token);

	public List<File> getAllUserFiles(String userID);
	
	public File saveFileDatabase(MultipartFile multipartFile, String userID) throws IOException, Exception;
	
	public void saveFileDisk(MultipartFile multipartFile, File file) throws IOException;
	
	public void updateFile(File file);
	
	public Pair<File, ByteArrayResource> getFile(String fileID) throws IOException;
	
	public File deleteFileDatabase(String fileID) throws IOException, Exception;
	
	public void deleteFileDisk(File file) throws IOException;
	
}
