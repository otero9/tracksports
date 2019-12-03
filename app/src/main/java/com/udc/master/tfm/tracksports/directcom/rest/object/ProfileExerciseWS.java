package com.udc.master.tfm.tracksports.directcom.rest.object;

import java.io.Serializable;
import java.util.List;

/**
 * Clase que representa a un conjunto de ejercicios de un usuario
 * @author a.oteroc
 *
 */
public class ProfileExerciseWS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Id de usuario */
	private String idUsuario;
	/** Lista de ejercicios */
	private List<ExerciseWS> ejercicios;
	
	public ProfileExerciseWS() {}

	/**
	 * @return the idUsuario
	 */
	public String getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the ejercicios
	 */
	public List<ExerciseWS> getEjercicios() {
		return ejercicios;
	}

	/**
	 * @param ejercicios the ejercicios to set
	 */
	public void setEjercicios(List<ExerciseWS> ejercicios) {
		this.ejercicios = ejercicios;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProfileExerciseWS [idUsuario=" + idUsuario + ", ejercicios="
				+ ejercicios + "]";
	}
}
