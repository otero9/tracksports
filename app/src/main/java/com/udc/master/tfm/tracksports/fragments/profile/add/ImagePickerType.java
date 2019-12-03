package com.udc.master.tfm.tracksports.fragments.profile.add;

/**
 * Enumerado que representa las posibilidades para obtener
 * una imagen de perfil
 * @author a.oteroc
 *
 */
public enum ImagePickerType {

	PICK_FROM_CAMERA(0),
	PICK_FROM_FILE(1);
	
	/** Identificador */
	private Integer id;
	
	private ImagePickerType(int id) {
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
	public static ImagePickerType valueOf(Integer id) {
		ImagePickerType[] genders = values();
		for (int i = 0; i < genders.length; i++) {
			if (genders[i].getId().equals(id)) {
				return genders[i]; 
			}
		}
		return null;
	}
}
