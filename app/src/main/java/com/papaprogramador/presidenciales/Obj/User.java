package com.papaprogramador.presidenciales.Obj;

public class User {
	private String username;
	private String email;
	private String departamento;
	private String UIDDispositivo;
	private String votopor;

	public User() {
	}

	public User(String username, String email, String departamento, String UIDDispositivo, String votopor) {
		this.username = username;
		this.email = email;
		this.departamento = departamento;
		this.UIDDispositivo = UIDDispositivo;
		this.votopor = votopor;
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
