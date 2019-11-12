package com.lamar.drive.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lamar.drive.auth.dao.UserDAO;
import com.lamar.drive.auth.entity.User;
import com.lamar.drive.auth.util.JwtUtil;
import com.lamar.drive.file.dao.FileDAO;
import com.lamar.drive.file.entity.File;

@Service
public class FileServiceImpl implements FileService {

	private final String ROOT_PATH = "/home/lazar/cloud";

	@Autowired
	private FileDAO fileDAO;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	@Transactional
	public List<File> getAllUserFiles(String userID) {

		List<File> files = fileDAO.getAllUserFiles(userID);
		
		return files;
	}

	@Override
	@Transactional
	public File saveFileDatabase(MultipartFile multipartFile, String userID) throws Exception {

		File file = new File(multipartFile.getOriginalFilename(), multipartFile.getContentType(),
				(int) multipartFile.getSize(), new Date().getTime());

		User user = userDAO.getUser(userID);

		if (user.getStored() + file.getSize() > user.getLimit()) {
			throw new Exception("There are no space anymore!");
		}

		file.setUser(user);

		fileDAO.saveFile(file);

		user.setStored(user.getStored() + file.getSize());

		return file;
	}

	@Override
	public void saveFileDisk(MultipartFile multipartFile, File file) throws IOException {
		Path filePath = Path.of(ROOT_PATH, file.getFileID());
		Files.copy(multipartFile.getInputStream(), filePath);
	}

	@Override
	@Transactional
	public void updateFile(File file) {
		
		file = fileDAO.updateFile(file);
	}

	@Override
	@Transactional
	public Pair<File, ByteArrayResource> getFile(String fileID) throws IOException {

		File file = fileDAO.getFile(fileID);

		Path filePath = Path.of(ROOT_PATH, file.getFileID());

		ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(filePath));

		Pair<File, ByteArrayResource> pair = Pair.of(file, byteArrayResource);

		return pair;
	}

	@Override
	@Transactional
	public File deleteFileDatabase(String userID) throws IOException, Exception {

		File file = fileDAO.getFile(userID);

		if (file == null) {
			throw new Exception("There is no such user!");
		}

		file.getUser().setStored(file.getUser().getStored() - file.getSize());

		fileDAO.deleteFile(file);

		return file;
	}

	@Override
	public void deleteFileDisk(File file) throws IOException {
		Path filePath = Path.of(ROOT_PATH, file.getFileID());
		Files.delete(filePath);
	}

	@Override
	public String getUserIDFromToken(String token) {

		return this.jwtUtil.getUserIDFromToken(token.split(" ")[1]);
	}

}
