package com.udc.master.tfm.tracksports.utils;

import java.util.List;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Clase de utilidad para el mapa de la aplicacion
 * @author a.oteroc
 *
 */
public class MapUtils {

    /** Tiempo que indica cuando una localizacion se vuelve obsoleta (en ms) */
    public static final long MIN_TIME_BECOME_STALE = 5 * 1000;
    /** Inclinacion de la camara sobre el mapa cuando se esta parado */
    public static final float DEFAULT_MAP_TILT = 0;
    /** Inclinacion de la camara sobre el mapa cuando se esta realizando un recorrido */
    public static final float DEFAULT_MAP_NAVIGATION_TILT = 90;
    /** Tiempo de duracion al mover la camara en el mapa */
    public static final int MAP_ANIMATION_DURATION = 3000;
    /** Proveedor considera el de mayor precision */
    public static final String ACCURATE_PROVIDER = LocationManager.GPS_PROVIDER;
    
    private MapUtils() {}
    
	/**
	 * Metodo que anade una marca al mapa a partir de la latitud y longitud.
	 * En caso de especificarle una por parametro se actualizara su posicion.
	 * @param latitude
	 * @param longitude
	 * @param map
	 * @param markerPosition
	 * @return Marca en donde se actualiza la posicion
	 */
	public static Marker addMarker(double latitude, double longitude, GoogleMap map, Marker markerPosition) {
		LatLng latLng = new LatLng(latitude, longitude);
		if (map != null) {
			//En caso de que no exista la marca se crea. Si ya existe una, se modifica su posicion
			if (markerPosition != null) {
				markerPosition.setPosition(latLng);
			} else {
				markerPosition = map.addMarker(new MarkerOptions().position(latLng));
			}
		}
		return markerPosition;
	}
	
	/**
	 * Metodo que actualiza la posicion de la camara en el mapa con la posicion especificada
	 * @param latitude
	 * @param longitude
	 * @param bearing
	 * @param zoom
	 * @param tilt
	 * @param animation Indica si se realizara una animacion al mover la camara o se movera directamente a la posicion indicada
	 * @param map
	 */
	public static void updateCameraPosition(double latitude, double longitude, float bearing, float zoom, float tilt, boolean animation, GoogleMap map) {
        if (map != null) {
    		LatLng position = new LatLng(latitude, longitude); //Posicion actual en el mapa
            CameraPosition camPos = new CameraPosition.Builder()
            .target(position) //Posicion donde se actualizar la camara
            .zoom(zoom) //Zoom que se hace sobre el mapa
            .bearing(bearing) //Donde debe de estar apuntando la camara
            .tilt(tilt) //Inclinacion
            .build();
            
            // Se situa el mapa en la posicion actual y se muestra una animiacion si se especifica como tal
            CameraUpdate cameraPos = CameraUpdateFactory.newCameraPosition(camPos);
            if (animation) {
                map.animateCamera(cameraPos, MAP_ANIMATION_DURATION, new CancelableCallback() {
        			@Override
        			public void onFinish() {}
        			@Override
        			public void onCancel() {}
        		});	
            } else {
            	map.animateCamera(cameraPos);
            }
        }
	}
	
	/**
	 * Metodo que actualiza la posicion de la camara en el mapa con la posicion especificada.
	 * Se utiliza para inicializar la posicion de la camara.
	 * El valor de bearing sera 0 y no se utilizar animacion para posicionar la camara.
	 * @param latitude
	 * @param longitude
	 * @param zoom
	 * @param tilt
	 * @param map
	 */
	public static void updateCameraPosition(double latitude, double longitude, float zoom, float tilt, GoogleMap map) {
		MapUtils.updateCameraPosition(latitude, longitude, 0, zoom, tilt, false, map);
	}

	/**
	 * Metodo que actualiza la posicion de la camara en el mapa con la posicion especificada.
	 * Se utiliza para actualizar la camara en movimiento durante el recorrido.
	 * El zoom y la inclinacion se mantienen.
	 * @param latitude
	 * @param longitude
	 * @param bearing
	 * @param map
	 */
	public static void updateCameraPosition(double latitude, double longitude, float bearing, GoogleMap map) {
		MapUtils.updateCameraPosition(latitude, longitude, bearing, map.getCameraPosition().zoom, map.getCameraPosition().tilt, true, map);
	}
	
	/**
	 * Metodo que actualiza la posicion de la camara en el mapa con la posicion especificada.
	 * Se utiliza para actualizar la camara la primera vez que se empieza el recorrido.
	 * @param latitude
	 * @param longitude
	 * @param bearing
	 * @param map
	 */
	public static void updateCameraPosition(double latitude, double longitude, float bearing, float zoom, float tilt, GoogleMap map) {
		MapUtils.updateCameraPosition(latitude, longitude, bearing, zoom, tilt, true, map);
	}
	
	/**
	 * Metodo que actualiza la posicion de la camara en el mapa con la posicion especificada.
	 * @param latitude
	 * @param longitude
	 * @param map
	 */
	public static void updateCameraPosition(double latitude, double longitude, GoogleMap map) {
		MapUtils.updateCameraPosition(
				latitude, 
				longitude, 
				map.getCameraPosition().bearing, 
				map.getCameraPosition().zoom, 
				map.getCameraPosition().tilt, 
				false, 
				map);
	}
	
    /**
     * Metodo que actualiza la linea de ruta anadiendo la posicion de la localizacion actual.
     * En caso de que la linea especificada no exista se crea en el mapa especificado.
     * @param mapPosition
     * @param map
     * @param lineRoute
     * @return
     */
    public static Polyline updateLineRoute(Double latitude, Double longitude, GoogleMap map, Polyline lineRoute) {
		if (lineRoute == null) {
			if (map != null) {
				PolylineOptions lineaRouteOptions = new PolylineOptions();
				lineaRouteOptions.add(new LatLng(latitude, longitude));
				lineRoute = map.addPolyline(lineaRouteOptions);
			}
		} else {
			List<LatLng> points = lineRoute.getPoints();
			points.add(new LatLng(latitude, longitude));
			lineRoute.setPoints(points);
		}
		return lineRoute;
    }
    
    /**
     * Metodo que calcula la posicion de la camara (en grados) a partir de dos localizaciones.
     * En caso de no especificar localizacion inicial se devuelve 0.
     * @param beginLocation
     * @param endLocation
     * @return
     */
    public static float bearingBetweenLocations(Location beginLocation, Location endLocation) {
    	if (beginLocation != null) {
    		return beginLocation.bearingTo(endLocation);
    	} else {
    		return 0;
    	}
    }
    
    /**
     * Metodo que calcula la distancia entre dos localizaciones en metros.
     * En caso de no especificar localizacion inicial se devuelve 0.
     * @param beginLocation
     * @param endLocation
     * @return
     */
    public static float distanceBetweenLocations(Location beginLocation, Location endLocation) {
    	if (beginLocation != null) {
    		return beginLocation.distanceTo(endLocation);
    	} else {
    		return 0;
    	}
    }
}
