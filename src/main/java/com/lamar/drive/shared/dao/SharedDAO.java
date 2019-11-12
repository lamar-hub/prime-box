package com.lamar.drive.shared.dao;

import java.util.List;

import com.lamar.drive.shared.entity.Shared;

public interface SharedDAO {
	
	public List<Shared> getAllShared(String userID);
	
	public Shared saveShared(Shared shared);
	
	public int deleteShared(String fileID, String email);

}
