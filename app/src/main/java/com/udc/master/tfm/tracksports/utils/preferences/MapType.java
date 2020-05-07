package com.udc.master.tfm.tracksports.utils.preferences;

import com.google.android.gms.maps.GoogleMap;

/**
 * Enumerado que representa los idiomas de la aplicacion
 * @author a.oteroc
 *
 */
public enum MapType {

	NORMAL(0, GoogleMap.MAP_TYPE_NORMAL),
	SATELLITE(1, GoogleMap.MAP_TYPE_SATELLITE),
	HYBRID(2, GoogleMap.MAP_TYPE_HYBRID),
	TERRAIN(3, GoogleMap.MAP_TYPE_TERRAIN);
	
	/** Identificador */
	private Short id;
	
	private int mapType;
	
	private MapType(int id, int mapType) {
		this.id = (short)id;
		this.mapType = mapType;
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
	 * @return the mapType
	 */
	public int getMapType() {
		return mapType;
	}

	/**
	 * @param mapType the mapType to set
	 */
	public void setMapType(int mapType) {
		this.mapType = mapType;
	}

	/**
	 * Metodo que obtiene el genero a partir del id
	 * @param id
	 * @return
	 */
	public static MapType valueOf(Short id) {
		MapType[] genders = values();
		for (int i = 0; i < genders.length; i++) {
			if (genders[i].getId().equals(id)) {
				return genders[i]; 
			}
		}
		return null;
	}
}
