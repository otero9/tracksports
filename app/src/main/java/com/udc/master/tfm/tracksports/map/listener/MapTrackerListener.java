package com.udc.master.tfm.tracksports.map.listener;

import android.location.Location;

/**
 * Listener para conocer las actualizaciones del mapa
 * @author a.oteroc
 *
 */
public interface MapTrackerListener {

	/**.
	 * Metodo invocado cuando se actualiza la posicion actual
	 * @param oldLocation
	 * @param oldTime
	 * @param newLocation
	 * @param newTime
	 */
	public void onUpdatePosition(Location oldLocation, long oldTime, Location newLocation, long newTime);
	
    /**
     * Metodo invocado para resetar los valores del listener
     */
	public void reset();
}
