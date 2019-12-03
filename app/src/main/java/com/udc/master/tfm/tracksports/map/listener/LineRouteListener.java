package com.udc.master.tfm.tracksports.map.listener;

import android.location.Location;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.utils.MapUtils;

/**
 * Listener encargado de actualizar la lista de puntos recorridos
 * @author a.oteroc
 *
 */
public class LineRouteListener implements MapTrackerListener {

	/** Distancia total recorrida */
	private float distance = 0;
    /** Listener encargado de mostrar la informacion de la distancia recorrida */
    private LineRouteDisplayListener listener;
	
	/**
	 * Interfaz implementada por las clases que muestran la informacion de la distancia recorrida
	 * @author a.oteroc
	 */
	public interface LineRouteDisplayListener {
		/** Metodo invocado al actualizarse la distancia recorrida */
		public void onPositionChanged(MapPosition mapPosition);
	}
	
	/**
	 * Constructor vacio
	 */
	public LineRouteListener() {}
	
	/**
	 * Constructor de la clase
	 * @param listener
	 */
	public LineRouteListener(LineRouteDisplayListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onUpdatePosition(Location oldLocation, long oldTime, Location newLocation, long newTime) {
		//Se actualiza la distancia recorrida (en metros)
		float distanceBetweenPoints = MapUtils.distanceBetweenLocations(oldLocation, newLocation);
		distance += distanceBetweenPoints;
		//Se actualiza la velocidad actual (se pasa a km/h)
		float actualSpeed = newLocation.getSpeed() * 3.6F;
		//Se actualiza la el ritmo actual (min/km)
		float speedPace = 0;
		if (actualSpeed != 0) {
			speedPace = (3600 / actualSpeed)/ 60;
		}
		
		//Se almacena la posicion actual
		MapPosition mapPosition = new MapPosition(
				newLocation.getLatitude(), 
				newLocation.getLongitude(), 
				newLocation.getAltitude(), 
				actualSpeed, 
				speedPace,
				distance,
				newTime);
		//Se notifica el cambio al listener para mostrar la informacion
		if (listener != null) {
			listener.onPositionChanged(mapPosition);
		}
	}

	@Override
	public void reset() {
		distance = 0;
	}
}
