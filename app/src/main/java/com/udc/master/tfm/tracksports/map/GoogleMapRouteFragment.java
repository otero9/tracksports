package com.udc.master.tfm.tracksports.map;

import java.util.List;

import com.google.android.gms.maps.model.Polyline;

import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.utils.MapUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Fragmento con el mapa que muestra la ruta recorrida
 * @author a.oteroc
 *
 */
public class GoogleMapRouteFragment extends GoogleMapFragment {

	/** Ejercicio que realiza el usuario */
	private Exercise exercise;
	/** Linea que representa la ruta tomada */
	private Polyline lineRoute;
	
	/**
	 * Constructor por defecto
	 * @param exercise
	 */
	public GoogleMapRouteFragment(Exercise exercise) {
		this.exercise = exercise;
	}
	
	@Override
	public void onActivityCreated() {
		lineRoute = null;
		List<MapPosition> points = exercise.getRoute();
		if (points == null || points.isEmpty()) {
			return;
		}
		//Se muestran todos los puntos, la configuracion se hace a nivel de almacenamiento en BBDD
		int totalSize = points.size();
		
		if (points != null && !points.isEmpty()) {
			for (int i = 0; i < totalSize; i++) {
				MapPosition mapPosition = points.get(i);
				Double latitude = mapPosition.getLatitude();
				Double longitude = mapPosition.getLongitude();
				
				//Se actualiza la posicion de la camara
				if (lineRoute == null) {
					Integer mapZoom = PreferencesUtils.getPreferences(PreferencesTypes.MAP_ZOOM, Integer.class, getActivity());
					float zoom = mapZoom.floatValue();
					MapUtils.updateCameraPosition(latitude, longitude, zoom, MapUtils.DEFAULT_MAP_TILT, mMap);
					setMapPosition(mapPosition);
				}
				
				//Se anade una marca por punto
				//Se anade una marca para el primer y ultimo punto
				if (i == 0 || i + 1 == totalSize) {
					MapUtils.addMarker(latitude, longitude , mMap, null);
				}
				
				//Se actualiza la linea de ruta con todos los puntos almacenados
				lineRoute = MapUtils.updateLineRoute(latitude, longitude, mMap, lineRoute);
			}
		}
	}
}
