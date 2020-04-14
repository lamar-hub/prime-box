package com.lamar.box.shared.service;

import java.util.List;

import com.lamar.box.shared.entity.Shared;

public interface SharedService {
	
	public String getUserIDFromToken(String token);
	
	public List<Shared> getAllUserSharedFiles(String userID);

	public void share(Shared shared);
	
	public void unshare(String userID, String fileID) throws Exception;
	
}
