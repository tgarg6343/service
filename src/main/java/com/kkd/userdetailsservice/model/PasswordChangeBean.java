package com.kkd.userdetailsservice.model;

public class PasswordChangeBean {

	private String userId;
	private String currentPassword;
	private String newPassword;
	public PasswordChangeBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PasswordChangeBean(String userId, String currentPassword, String newPassword) {
		super();
		this.userId = userId;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	@Override
	public String toString() {
		return "PasswordChangeBean [userId=" + userId + ", currentPassword=" + currentPassword + ", newPassword="
				+ newPassword + "]";
	}
	
}
