package com.lamar.box.sms.model;

public class SenderTemplate {
	
	private String phoneNumber;
	private String message;
	
	public SenderTemplate() {
	}

	public SenderTemplate(String phoneNumber, String message) {
		this.phoneNumber = phoneNumber;
		this.message = message;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
