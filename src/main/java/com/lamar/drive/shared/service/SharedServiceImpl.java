package com.lamar.drive.shared.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lamar.drive.auth.dao.UserDAO;
import com.lamar.drive.auth.util.JwtUtil;
import com.lamar.drive.file.dao.FileDAO;
import com.lamar.drive.shared.dao.SharedDAO;
import com.lamar.drive.shared.entity.Shared;

@Service
public class SharedServiceImpl implements SharedService {

	@Autowired
	private SharedDAO sharedDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private FileDAO fileDAO;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	@Transactional
	public List<Shared> getAllUserSharedFiles(String userID) {
		
		List<Shared> shareds = sharedDAO.getAllShared(userID);
		
		return shareds;
	}

	@Override
	@Transactional
	public void share(Shared shared) {

		shared.setSharedFile(fileDAO.getFile(shared.getSharedFile().getFileID()));

		shared.setSharedUser(userDAO.getByUsername(shared.getSharedUser().getEmail()));

		sharedDAO.saveShared(shared);

	}

	@Override
	@Transactional
	public void unshare(String userID, String fileID) throws Exception {

		int deleted = sharedDAO.deleteShared(fileID, userID);
		
		if(deleted != 1) {
			throw new Exception("Shared is not deleted!");
		}

	}

	@Override
	public String getUserIDFromToken(String token) {
		return this.jwtUtil.getUserIDFromToken(token.split(" ")[1]);
	}

}
