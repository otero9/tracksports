package com.udc.master.tfm.tracksports.bbdd.profiles;

/**
 * Enumerado que representa el Genero de una perfil
 * @author a.oteroc
 *
 */
public enum Gender {

	MALE(0),
	FEMALE(1);
	
	/** Identificador */
	private Short id;
	
	private Gender(int id) {
		this.id = (short)id;
	}

	/**
	 * Get the id
	 * @return
	 */
	public Short getId() {
		return id;
	}

	/**
	 * Set the id
	 * @param id
	 */
	public void setId(Short id) {
		this.id = id;
	}
	
	/**
	 * Metodo que obtiene el genero a partir del id
	 * @param id
	 * @return
	 */
	public static Gender valueOf(Short id) {
		Gender[] genders = values();
		for (int i = 0; i < genders.length; i++) {
			if (genders[i].getId().equals(id)) {
				return genders[i]; 
			}
		}
		return null;
	}
}
