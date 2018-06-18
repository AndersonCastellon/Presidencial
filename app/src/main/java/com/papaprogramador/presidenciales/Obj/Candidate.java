package com.papaprogramador.presidenciales.Obj;


public class Candidate {
	private String nombreCandidato;
	private String partidoCandidato;
	private int votosCandidato;
	private String urlImagen;
	private String urlHtml;
	private String id;

	public Candidate() {
	}

	public Candidate(String nombreCandidato, String partidoCandidato, int votosCandidato,
	                 String urlImagen, String urlHtml, String id) {
		this.nombreCandidato = nombreCandidato;
		this.partidoCandidato = partidoCandidato;
		this.votosCandidato = votosCandidato;
		this.urlImagen = urlImagen;
		this.urlHtml = urlHtml;
		this.id = id;
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

	public String getNombreCandidato() {
		return nombreCandidato;
	}

	public void setNombreCandidato(String nombreCandidato) {
		this.nombreCandidato = nombreCandidato;
	}

	public String getPartidoCandidato() {
		return partidoCandidato;
	}

	public void setPartidoCandidato(String partidoCandidato) {
		this.partidoCandidato = partidoCandidato;
	}

	public int getVotosCandidato() {
		return votosCandidato;
	}

	public void setVotosCandidato(int votosCandidato) {
		this.votosCandidato = votosCandidato;
	}
}
