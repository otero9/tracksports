package com.udc.master.tfm.tracksports.map.listener;

import android.location.Location;

/**
 * Listener encargado de actualizar la altitud del recorrido
 * @author a.oteroc
 *
 */
public class AltitudeListener implements MapTrackerListener {
	/** Altitud minima del recorrido */
	private Double altitudeMin = null;
	/** Altitud maxima del recorrido */
	private Double altitudeMax = null;
	
    /** Listener encargado de mostrar la informacion de la altitud del recorrido */
    private AltitudDisplayListener listener;
	
	/**
	 * Interfaz implementada por las clases que muestran la informacion de la altitud de recorrido
	 * @author a.oteroc
	 */
	public interface AltitudDisplayListener {
		/** Metodo invocado al actualizarse la altitud del recorrido */
		public void onPositionChanged(double altitude, double altitudeMin, double altitudeMax);
	}
	
	/**
	 * Constructor vacio
	 */
	public AltitudeListener() {}
	
	/**
	 * Constructor de la clase
	 * @param listener
	 */
	public AltitudeListener(AltitudDisplayListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onUpdatePosition(Location oldLocation, long oldTime, Location newLocation, long newTime) {
		//Se actualiza la altitud
		double altitude = newLocation.getAltitude();
		//Se actualiza la altitud minima y maxima
		if (altitudeMin == null) {
			altitudeMin = altitude;
		}
		if (altitudeMax == null) {
			altitudeMax = altitude;
		}
		if (altitude < altitudeMin) {
			altitudeMin = altitude;
		} else if (altitude > altitudeMax) {
			altitudeMax = altitude;
		}
		//Se notifica el cambio al listener para mostrar la informacion
		if (listener != null) {
			listener.onPositionChanged(altitude, altitudeMin, altitudeMax);
		}
	}

	@Override
	public void reset() {
		altitudeMin = null;
		altitudeMax = null;
	}
}
