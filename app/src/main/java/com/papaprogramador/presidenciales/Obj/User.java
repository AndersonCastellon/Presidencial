package com.papaprogramador.presidenciales.Obj;

public class User {
	private String userId;
	private String username;
	private String email;
	private String departamento;
	private String votopor;
	private String politicalFlag;

	public User() {
	}

	public User(String username, String email, String departamento, String votopor) {
		this.username = username;
		this.email = email;
		this.departamento = departamento;
		this.votopor = votopor;
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
