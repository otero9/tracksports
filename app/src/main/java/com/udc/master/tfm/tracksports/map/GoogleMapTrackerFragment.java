package com.udc.master.tfm.tracksports.map;

import java.util.ArrayList;
import java.util.List;
import android.location.Location;
import com.google.android.gms.maps.model.Polyline;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.locationtracker.LocationTracker;
import com.udc.master.tfm.tracksports.locationtracker.LocationUpdateListener;
import com.udc.master.tfm.tracksports.map.listener.MapTrackerListener;
import com.udc.master.tfm.tracksports.utils.MapUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Clase que representa el fragmento que muestra la ruta que se lleva a cabo
 * @author a.oteroc
 *
 */
public class GoogleMapTrackerFragment extends GoogleMapFragment implements LocationUpdateListener {

	/** Linea que representa la ruta tomada */
	private Polyline lineRoute;
	/** Listeners que actualizan la posicion actual */
	private List<MapTrackerListener> listeners = new ArrayList<MapTrackerListener>();
	/** Clase encargada de obtener la posicion actual */
	private LocationTracker locationTracker;
	
	/**
	 * Constructor sin parametros
	 */
	public GoogleMapTrackerFragment() {}
	
	@Override
	public void onActivityCreated() {
		initCameraPosition();
	}
	
	/**
	 * Metodo que actualiza la posicion de la camara del mapa
	 * a partir de la casa especificada por el usuario. En caso
	 * de no existir ninguna se busca la ultima posicion obtenida por el GPS
	 * //@param locationTracker
	 */
	private void initCameraPosition() {
		if (locationTracker != null && getActivity() != null) {
			MapPosition currentLocation = null;
			//Se obtiene la casa especificada por el usuario
			Profile profile = PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, getActivity());
			if (profile != null) {
				currentLocation = profile.getMapPosition();
			}
			//Si el usuario no ha especificada ninguna casa, se obtiene la ultima posicion conocida
			if (currentLocation == null) {
				if (locationTracker.getLocation() != null) {
					currentLocation = new MapPosition(locationTracker.getLocation());
				} else if (locationTracker.getPossiblyStaleLocation() != null) {
					currentLocation = new MapPosition(locationTracker.getPossiblyStaleLocation());
				}
			}
			if (currentLocation != null) {
				Integer mapZoom = PreferencesUtils.getPreferences(PreferencesTypes.MAP_ZOOM, Integer.class, getActivity());
				float zoom = mapZoom.floatValue();
				MapUtils.updateCameraPosition(
						currentLocation.getLatitude(), 
						currentLocation.getLongitude(), 
						zoom,
						MapUtils.DEFAULT_MAP_NAVIGATION_TILT,
						mMap);
				setMapPosition(currentLocation);
			}
		}
	}
	
	/**
	 * Metodo que setea el locationTracker
	 * @param locationTracker
	 */
	public void setLocationTracker(LocationTracker locationTracker) {
		this.locationTracker = locationTracker;
	}
	
	/**
	 * Metodo que anade un listener para monitorizar la actualizacion de la posicion
	 * @param listener
	 */
	public void addMapTrackerListener(MapTrackerListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void onUpdate(Location oldLocation, long oldTime, Location newLocation, long newTime) {
		if (getActivity() != null) {
			//Si se ha establece algun listener se actualizan los cambios
			if (listeners != null && !listeners.isEmpty()) {
				for (MapTrackerListener listener : listeners) {
					listener.onUpdatePosition(oldLocation, oldTime, newLocation, newTime);
				}
			}

			//Se actualiza la linea de ruta
			lineRoute = MapUtils.updateLineRoute(newLocation.getLatitude(), newLocation.getLongitude(), mMap, lineRoute);
			//Se actualiza la posicion de la camara siempre que el usuario no interactue con el mapa
			if (!isUserInteraction()) {
				//Se calcula la posicion de la camara para moverla dinamicamente segun la orientacion
				float bearing = MapUtils.bearingBetweenLocations(oldLocation, newLocation);
				//Si es la primera posicion que se obtiene se actualiza con el zoom y la inclinacion
				if (oldLocation == null) {
					Integer mapZoom = PreferencesUtils.getPreferences(PreferencesTypes.MAP_ZOOM, Integer.class, getActivity());
					float zoom = mapZoom.floatValue();
					MapUtils.updateCameraPosition(
							newLocation.getLatitude(), 
							newLocation.getLongitude(), 
							bearing, 
							zoom,
							MapUtils.DEFAULT_MAP_NAVIGATION_TILT, 
							mMap);
				} else {
					MapUtils.updateCameraPosition(newLocation.getLatitude(), newLocation.getLongitude(), bearing, mMap);
				}
			}
			
			//Se actualiza la posicion actual
			MapPosition mapPosition = new MapPosition(newLocation);
			setMapPosition(mapPosition);			
		}
	}
}
