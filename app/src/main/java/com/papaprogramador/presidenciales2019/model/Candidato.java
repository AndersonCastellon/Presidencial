package com.papaprogramador.presidenciales2019.model;

public class Candidato {
	String nombreCandidato;
	String PartidoCandidato;
	int votosCandidato;
	String urlImagen;

	public Candidato() {
	}

	public Candidato(String nombreCandidato, String partidoCandidato, int votosCandidato, String urlImagen) {
		this.nombreCandidato = nombreCandidato;
		PartidoCandidato = partidoCandidato;
		this.votosCandidato = votosCandidato;
		this.urlImagen = urlImagen;
	}

	public String getNombreCandidato() {
		return nombreCandidato;
	}

	public void setNombreCandidato(String nombreCandidato) {
		this.nombreCandidato = nombreCandidato;
	}

	public String getPartidoCandidato() {
		return PartidoCandidato;
	}

	public void setPartidoCandidato(String partidoCandidato) {
		PartidoCandidato = partidoCandidato;
	}

	public int getVotosCandidato() {
		return votosCandidato;
	}

	public void setVotosCandidato(int votosCandidato) {
		this.votosCandidato = votosCandidato;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
}
