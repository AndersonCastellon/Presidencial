package com.papaprogramador.presidenciales2019.Objetos;


public class Candidato {
	String nombreCandidato;
	String partidoCandidato;
	int votosCandidato;
	String urlImagen;
	String id;

	public Candidato() {
	}

	public Candidato(String nombreCandidato, String partidoCandidato, int votosCandidato, String urlImagen, String id) {
		this.nombreCandidato = nombreCandidato;
		this.partidoCandidato = partidoCandidato;
		this.votosCandidato = votosCandidato;
		this.urlImagen = urlImagen;
		this.id = id;
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

	public String getStringImagen() {
		return urlImagen;
	}

	public void setStringImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
}
