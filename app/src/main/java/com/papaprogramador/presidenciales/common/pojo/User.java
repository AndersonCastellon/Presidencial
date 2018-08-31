package com.papaprogramador.presidenciales.common.pojo;


public class User {
	private String userId;
	private String username;
	private String email;
	private String departamento;
	private String votopor;
	private String uriPhotoProfile;
	private String politicalFlag;

	public User() {
	}

	public User(String username, String email, String departamento, String votopor, String uriPhotoProfile) {
		this.username = username;
		this.email = email;
		this.departamento = departamento;
		this.votopor = votopor;
		this.uriPhotoProfile = uriPhotoProfile;
	}

	public String getUriPhotoProfile() {
		return uriPhotoProfile;
	}

	public void setUriPhotoProfile(String uriPhotoProfile) {
		this.uriPhotoProfile = uriPhotoProfile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPoliticalFlag() {
		return politicalFlag;
	}

	public void setPoliticalFlag(String politicalFlag) {
		this.politicalFlag = politicalFlag;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getVotopor() {
		return votopor;
	}

	public void setVotopor(String votopor) {
		this.votopor = votopor;
	}
}
