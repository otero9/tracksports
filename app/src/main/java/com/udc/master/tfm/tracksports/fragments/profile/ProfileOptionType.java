package com.udc.master.tfm.tracksports.fragments.profile;


/**
 * Lista de acciones que se pueden hacer sobre un perfil
 * @author a.oteroc
 *
 */
public enum ProfileOptionType {
	EDIT_PROFILE(0),
	REMOVE_PROFILE(1),
	SYNC_PROFILE(2);
	
	/** Identificador */
	private Integer id;
	
	private ProfileOptionType(int id) {
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
	public static ProfileOptionType valueOf(Integer id) {
		ProfileOptionType[] options = values();
		for (int i = 0; i < options.length; i++) {
			if (options[i].getId().equals(id)) {
				return options[i]; 
			}
		}
		return null;
	}
}
