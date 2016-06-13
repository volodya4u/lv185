package com.softserve.edu.hb.dto;

public class UserDTO {

	private String loginUser;
	private String roleName;

	public UserDTO(String loginUser, String roleName) {
		super();
		this.loginUser = loginUser;
		this.roleName = roleName;
	}

	// setters
	
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	// getters
	
	public String getLoginUser() {
		return loginUser;
	}

	public String getRoleName() {
		return roleName;
	}

}