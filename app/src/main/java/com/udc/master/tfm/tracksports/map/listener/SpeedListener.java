package com.udc.master.tfm.tracksports.map.listener;

import android.location.Location;

/**
 * Listener encargado de actualizar la velocidad del usuario
 * @author a.oteroc
 *
 */
public class SpeedListener implements MapTrackerListener {

	/** Velocidad maxima obtenida */
	private float speedMax = 0;
	/** Velocidad agregada sumando la velocidad obtenida en todos los puntos */
	private float speedAgregate = 0;
	/** Numero de posiciones de la ruta obtenidas */
	private int routePoints = 0;
	
    /** Listener encargado de mostrar la informacion de la velocidad del usuario */
    private SpeedDisplayListener listener;
	
	/**
	 * Interfaz implementada por las clases que muestran la informacion de la velocidad del usuario
	 * @author a.oteroc
	 */
	public interface SpeedDisplayListener {
		/** Metodo invocado al actualizarse la velocidad del usuario */
		public void onPositionChanged(float actualSpeed, float speedPace, float speedMax, float speedAvg);
	}
	
	/**
	 * Constructor vacio
	 */
	public SpeedListener() {}
	
	/**
	 * Constructor de la clase
	 * @param listener
	 */
	public SpeedListener(SpeedDisplayListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onUpdatePosition(Location oldLocation, long oldTime, Location newLocation, long newTime) {
		//Se actualiza la velocidad actual (se pasa a km/h)
		float actualSpeed = newLocation.getSpeed() * 3.6F;
		//Se actualiza la el ritmo actual (min/km)
		float speedPace = 0;
		if (actualSpeed != 0) {
			speedPace = (3600 / actualSpeed)/ 60;
		}
		
		//Se actualiza la velocidad maxima
		if (actualSpeed > speedMax) {
			speedMax = actualSpeed;
		}
		//Se actualiza la velocidad media
		routePoints++;
		speedAgregate += actualSpeed;
		float speedAvg = speedAgregate / routePoints;
		//Se notifica el cambio al listener para mostrar la informacion
		if (listener != null) {
			listener.onPositionChanged(actualSpeed, speedPace, speedMax, speedAvg);
		}
	}

	@Override
	public void reset() {
		speedMax = 0;
		speedAgregate = 0;
		routePoints = 0;
	}
}
