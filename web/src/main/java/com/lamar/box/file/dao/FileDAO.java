package com.lamar.box.file.dao;

import java.util.List;

import com.lamar.box.file.entity.File;

public interface FileDAO {
	
	public List<File> getAllUserFiles(String userID);
	
	public File saveFile(File file);

	public File getFile(String fileID);
	
	public File updateFile(File file);
	
	public void deleteFile(File file);
	
}
