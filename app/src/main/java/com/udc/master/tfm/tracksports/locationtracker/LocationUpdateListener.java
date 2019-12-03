package com.udc.master.tfm.tracksports.locationtracker;

import android.location.Location;

/**
 * Clase encargada de manejar la localizacion actual
 * @author a.oteroc
 *
 */
public interface LocationUpdateListener {

	/**
	 * Evento lanzado cuando varia la posicion actual
	 * @param oldLoc
	 * @param oldTime
	 * @param newLocation
	 * @param newTime
	 */
	public void onUpdate(Location oldLocation, long oldTime, Location newLocation, long newTime);
}
