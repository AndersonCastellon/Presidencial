package com.papaprogramador.presidenciales2019.model;

public class Usuario {
	private String username;
	private String email;
	private String departamento;
	private String IMEI;

	public Usuario() {
	}

	public Usuario(String username, String email, String departamento, String IMEI) {
		this.username = username;
		this.email = email;
		this.departamento = departamento;
		this.IMEI = IMEI;
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

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String IMEI) {
		this.IMEI = IMEI;
	}
}
