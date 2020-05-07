package com.udc.master.tfm.tracksports.locationtracker;

import android.location.Location;

/**
 * Interfaz para obtener la localizacion actual
 * @author a.oteroc
 *
 */
public interface LocationTracker {

	/**
	 * Metodo que inicia el servicio de localizacion
	 */
	public void start();
	
	/**
	 * Metodo que inicia el servicio de localizacion
	 * @param listener Listener encargador de manejar la localizacion
	 */
	public void start(LocationUpdateListener listener);
	
	/**
	 * Metodo que detiene el servicio de localizacion
	 */
	public void stop();
	
	/**
	 * Metodo que comprueba si el proveedor esta activado
	 * @return
	 */
	public boolean isEnabled();
	
	/**
	 * Metodo que comprueba si se puede obtener una localizacion valida
	 * @return
	 */
	public boolean hasLocation();
	
	/**
	 * Metodo que comprueba si se puede obtener la ultima posicion conociada
	 * del dispositivo (sea fiable o no)
	 * @return
	 */
	public boolean hasPossiblyStaleLocation();
	
	/**
	 * Metodo que devuelva la ultima posicion valida conocida
	 * en caso de haber alguna
	 * @return
	 */
	public Location getLocation();
	
	/**
	 * Metodo que devuelva la ultima posicion conocida del dispositivo
	 * (sea fiable o no)
	 */
	public Location getPossiblyStaleLocation();
}
