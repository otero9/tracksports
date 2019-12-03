package com.udc.master.tfm.tracksports.map.listener;

import com.udc.master.tfm.tracksports.utils.MapUtils;
import android.location.Location;

/**
 * Listener encargado de actualizar la distancia total recorrida
 * @author a.oteroc
 *
 */
public class DistanceListener implements MapTrackerListener {

	/** Distancia total recorrida */
	private float distance = 0;
	/** Distancia total especificada a recorrer */
	private float totalDistance = 0;
	/** Distancia restante a recorrer */
	private float remainingDistance = 0;
    /** Listener encargado de mostrar la informacion de la distancia recorrida */
    private DistanceDisplayListener distanceDisplayListener;
	/** Listener encargado de notificar si se ha completada la distancia a recorrer */
    private DistanceReaminingComplete distanceReaminingComplete;
	/**
	 * Interfaz implementada por las clases que muestran la informacion de la distancia recorrida
	 * @author a.oteroc
	 */
	public interface DistanceDisplayListener {
		/** Metodo invocado al actualizarse la distancia recorrida */
		public void onPositionChanged(float distance, float remainingDistance);
	}
	
	/**
	 * Interfaz implementada por las clases que reciben la notificacion de distancia recorrida completada
	 * @author a.oteroc
	 *
	 */
	public interface DistanceReaminingComplete {
		/** Metodo invocado al completarse la distancia recorrida */
		public void onCompleteDistance();
	}
	
	/**
	 * Constructor vacio
	 */
	public DistanceListener() {}
	
	/**
	 * Constructor de la clase
	 * @param distanceDisplayListener
	 * @param distanceReaminingComplete
	 * @param totalDistance
	 */
	public DistanceListener(DistanceDisplayListener distanceDisplayListener, DistanceReaminingComplete distanceReaminingComplete, float totalDistance) {
		this.distanceDisplayListener = distanceDisplayListener;
		this.distanceReaminingComplete = distanceReaminingComplete;
		this.totalDistance = totalDistance;
		this.remainingDistance = totalDistance;
	}
	
	/**
	 * Constructor de la clase
	 * @param distanceDisplayListener
	 */
	public DistanceListener(DistanceDisplayListener distanceDisplayListener) {
		this.distanceDisplayListener = distanceDisplayListener;
	}
	
	@Override
	public void onUpdatePosition(Location oldLocation, long oldTime, Location newLocation, long newTime) {
		//Se actualiza la distancia recorrida (en metros)
		float distanceBetweenPoints = MapUtils.distanceBetweenLocations(oldLocation, newLocation);
		distance += distanceBetweenPoints;
		if (remainingDistance > 0) {
			remainingDistance -= distanceBetweenPoints;
		} else {
			remainingDistance = 0;
		}

		if (distanceDisplayListener != null) {
			distanceDisplayListener.onPositionChanged(distance, remainingDistance);
		}
		
		if (distanceReaminingComplete != null && totalDistance > 0 && remainingDistance <= 0) {
			distanceReaminingComplete.onCompleteDistance();
		}
	}

	@Override
	public void reset() {
		distance = 0;
		totalDistance = 0;
		remainingDistance = 0;
	}
}
