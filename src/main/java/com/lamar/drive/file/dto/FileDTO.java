package com.lamar.drive.file.dto;

import java.io.Serializable;

import com.lamar.drive.auth.entity.User;

public class FileDTO implements Serializable {

	private static final long serialVersionUID = -7982422061633140519L;

	private String fileID;

	private String filename;

	private String type;

	private long size;

	private long lastModified;

	private User user;

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
