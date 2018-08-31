package com.papaprogramador.presidenciales.common.pojo;


public class Candidate {
	private String nombreCandidato;
	private String partidoCandidato;
	private int votosCandidato;
	private String urlImagen;
	private String urlHtml;
	private String id;
	private String politicalFlag;

	public Candidate() {
	}

	public String getPoliticalFlag() {
		return politicalFlag;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public String getUrlHtml() {
		return urlHtml;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombreCandidato() {
		return nombreCandidato;
	}

	public String getPartidoCandidato() {
		return partidoCandidato;
	}

	public int getVotosCandidato() {
		return votosCandidato;
	}
}
