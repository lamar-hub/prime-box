package com.lamar.box.shared.dto;

import java.io.Serializable;

public class SharedDTO implements Serializable {

	private static final long serialVersionUID = 7973255189958552242L;
	
	private String sharedFileFileID;
	
	private String sharedFileFilename;
	
	private long sharedFileSize;
	
	private long sharedFileLastModified;
	
	private String sharedFileUserEmail;
	
	private String sharedFileUserName;
	
	private String sharedFileUserSurname;
	
	private String message;
	
	private long date;

	public String getSharedFileFileID() {
		return sharedFileFileID;
	}

	public void setSharedFileFileID(String sharedFileFileID) {
		this.sharedFileFileID = sharedFileFileID;
	}

	public String getSharedFileFilename() {
		return sharedFileFilename;
	}

	public void setSharedFileFilename(String sharedFileFilename) {
		this.sharedFileFilename = sharedFileFilename;
	}

	public long getSharedFileSize() {
		return sharedFileSize;
	}

	public void setSharedFileSize(long sharedFileSize) {
		this.sharedFileSize = sharedFileSize;
	}

	public long getSharedFileLastModified() {
		return sharedFileLastModified;
	}

	public void setSharedFileLastModified(long sharedFileLastModified) {
		this.sharedFileLastModified = sharedFileLastModified;
	}

	public String getSharedFileUserEmail() {
		return sharedFileUserEmail;
	}

	public void setSharedFileUserEmail(String sharedFileUserEmail) {
		this.sharedFileUserEmail = sharedFileUserEmail;
	}

	public String getSharedFileUserName() {
		return sharedFileUserName;
	}

	public void setSharedFileUserName(String sharedFileUserName) {
		this.sharedFileUserName = sharedFileUserName;
	}

	public String getSharedFileUserSurname() {
		return sharedFileUserSurname;
	}

	public void setSharedFileUserSurname(String sharedFileUserSurname) {
		this.sharedFileUserSurname = sharedFileUserSurname;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
