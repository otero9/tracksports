package com.udc.master.tfm.tracksports.fragments.diary;


/**
 * Lista de acciones que se pueden hacer sobre un ejercicio
 * @author a.oteroc
 *
 */
public enum ExerciseOptionType {
	SYNC_EXERCISE(0),
	REMOVE_EXERCISE(1);
	
	/** Identificador */
	private Integer id;
	
	private ExerciseOptionType(int id) {
		this.id = id;
	}

	/**
	 * Get the id
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set the id
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * Metodo que obtiene el genero a partir del id
	 * @param id
	 * @return
	 */
	public static ExerciseOptionType valueOf(Integer id) {
		ExerciseOptionType[] options = values();
		for (int i = 0; i < options.length; i++) {
			if (options[i].getId().equals(id)) {
				return options[i]; 
			}
		}
		return null;
	}
}
