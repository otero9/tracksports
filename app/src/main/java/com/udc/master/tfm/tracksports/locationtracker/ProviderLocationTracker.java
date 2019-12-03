package com.udc.master.tfm.tracksports.locationtracker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import com.udc.master.tfm.tracksports.utils.MapUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Clase que se encarga de capturar los eventos de localizacion del dispositivo
 * @author a.oteroc
 *
 */
public class ProviderLocationTracker implements LocationListener, LocationTracker {

	/** Servicio encargado de obtener la localiacion */
	private LocationManager locManager;
	/** Proveedor que se usa apra obtener la localzacion */
	private String provider;
	/** Listener encagado de manejar la posicion actual */
	private LocationUpdateListener listener;
	/** Indica si el proveedor esta en funcionamiento */
	private boolean isRunning;
	/** Ultima posicion valida obtenida por el proveedor */
	private Location lastLocation;
	/** Hora a la que se obtiene la ultima posicion valida por el proveedor*/
	private long lastTime;
	/** Variable que indica si el proveedor instancia esta habilitado */
	private boolean isEnabled;
	/** Contexto de la aplicacion */
	private Context context;
	
	/**
	 * Constructor de la clase
	 * @param context
	 * @param providerType
	 */
	public ProviderLocationTracker(Context context, ProviderType providerType) {
		locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (providerType == ProviderType.NETWORK) {
			provider = LocationManager.NETWORK_PROVIDER;
		} else {
			provider = LocationManager.GPS_PROVIDER;
		}
		isEnabled = locManager.isProviderEnabled(provider);
		this.context = context;
	}
	
	@Override
	public void start() {
		//Si ya esta corriendo no se hace nada o si el proveedor no esta activo
		if (!isRunning && isEnabled) {
			isRunning = true;
			Integer updateFreqSeconds = PreferencesUtils.getPreferences(PreferencesTypes.MAP_UPDATE_FREQ_SEC,Integer.class, context);
			Integer updateFreqMeters = PreferencesUtils.getPreferences(PreferencesTypes.MAP_UPDATE_FREQ_METERS, Integer.class, context);
			long minTime = updateFreqSeconds.longValue();
			float minDistance = updateFreqMeters.floatValue();
			locManager.requestLocationUpdates(provider, minTime, minDistance, this);
			lastLocation = null;
			lastTime = 0;
		}
	}

	@Override
	public void start(LocationUpdateListener listener) {
		start();
		this.listener = listener;
	}

	@Override
	public void stop() {
		if (isRunning) {
			locManager.removeUpdates(this);
			isRunning = false;
			listener = null;
		}
	}

	@Override
	public boolean hasLocation() {
		if (lastLocation == null) {
			return false;
		} else if (System.currentTimeMillis() - lastTime > MapUtils.MIN_TIME_BECOME_STALE) {
			return false; //localizacion desfasada
		} else {
			return true;
		}
	}

	@Override
	public boolean hasPossiblyStaleLocation() {
		if (lastLocation != null) {
			return true;
		} else {
			return locManager.getLastKnownLocation(provider) != null;
		}
	}
	
	@Override
	public Location getLocation() {
		if (lastLocation == null) {
			return null;
		} else if (System.currentTimeMillis() - lastTime > MapUtils.MIN_TIME_BECOME_STALE) {
			return null; //localizacion desfasada
		} else {
			return lastLocation;
		}
	}

	@Override
	public Location getPossiblyStaleLocation() {
		if (lastLocation != null) {
			return lastLocation;
		} else {
			return locManager.getLastKnownLocation(provider);
		}
	}

	@Override
	public void onLocationChanged(Location newLocation) {
		long now = System.currentTimeMillis();
		if (listener != null) {
			listener.onUpdate(lastLocation, lastTime, newLocation, now);
		}
		lastLocation = newLocation;
		lastTime = now;
	}
	
	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (LocationProvider.AVAILABLE == status) {
			isEnabled = true;
		} else {
			isEnabled = false;
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		isEnabled = true;
	}

	@Override
	public void onProviderDisabled(String provider) {
		isEnabled = false;
	}
}
