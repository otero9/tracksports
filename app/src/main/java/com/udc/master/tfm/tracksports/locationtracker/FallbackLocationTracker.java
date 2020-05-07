package com.udc.master.tfm.tracksports.locationtracker;

import android.content.Context;
import android.location.Location;
import com.udc.master.tfm.tracksports.utils.MapUtils;

/**
 * Clase encargada de obtener la posicion del dispositivo a partir
 * de los servicios de red y del GPS
 * @author a.oteroc
 *
 */
public class FallbackLocationTracker implements LocationTracker, LocationUpdateListener {

	/** Proveedor de localizacion mediante GPS*/
	private ProviderLocationTracker gps;
	/** Proveedor de localizacion mediante servicios de red*/
	private ProviderLocationTracker network;
	/** Listener encagado de manejar la posicion actual */
	private LocationUpdateListener listener;
	/** Indica si los proveedores estan en funcionamiento */
	private boolean isRunning;
	/** Ultima posicion valida obtenida por alguno de los proveedores */
	private Location lastLocation;
	/** Hora a la que se obtiene la ultima posicion valida por alguno de los proveedores */
	private long lastTime;
	
	/**
	 * Constructor de la clase
	 * @param context
	 */
	public FallbackLocationTracker(Context context) {
		gps = new ProviderLocationTracker(context, ProviderType.GPS);
		network = new ProviderLocationTracker(context, ProviderType.NETWORK);
	}
	
	@Override
	public void start() {
		//Se inician ambos proveedores
		if (!isRunning) {
			gps.start(this);
			network.start(this);
			isRunning = true;
		}
	}

	@Override
	public void start(LocationUpdateListener listener) {
		start();
		this.listener = listener; 
	}

	@Override
	public void stop() {
		//Se detienen ambos proveedores
		if (isRunning) {
			gps.stop();
			network.stop();
			isRunning = false;
			listener = null;
		}
	}

	@Override
	public boolean hasLocation() {
		//Si uno de los dos tiene una localizacion valida se devuelve
		return gps.hasLocation() || network.hasLocation();
	}

	@Override
	public boolean hasPossiblyStaleLocation() {
		//Si uno de los dos tiene una localizacion se devuelve
		return gps.hasPossiblyStaleLocation() || network.hasPossiblyStaleLocation();
	}
	
	@Override
	public boolean isEnabled() {
		if ((gps!=null && gps.isEnabled()) || (network!=null && network.isEnabled())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Location getLocation() {
		//Se devuelva la localizacion preferentemente del GPS
		Location location = gps.getLocation();
		if (location == null) {
			location = network.getLocation();
		}
		return location;
	}

	@Override
	public Location getPossiblyStaleLocation() {
		//Se devuelva la localizacion preferentemente del GPS
		Location location = gps.getPossiblyStaleLocation();
		if (location == null) {
			location = network.getPossiblyStaleLocation();
		}
		return location;
	}

	@Override
	public void onUpdate(Location oldLocation, long oldTime, Location newLocation, long newTime) {
		boolean update = false;
		/*
		 * Se actualizara la posicion si:
		 * - La localizacion previa es nula o
		 * - El proveedor de la localizacion es el mismo o
		 * - El proveedor de la localizacion es de mayor precision o
		 * - Si la localizacion antigua esta desfasada
		 */
		if (lastLocation == null
				|| lastLocation.getProvider().equals(newLocation.getProvider())
					|| newLocation.getProvider().equals(MapUtils.ACCURATE_PROVIDER)
					|| newTime - lastTime > MapUtils.MIN_TIME_BECOME_STALE)  {
			update = true;
		}
		if (update) {
			lastLocation = newLocation;
			lastTime = newTime;
			if (listener != null) {
				listener.onUpdate(oldLocation, oldTime, newLocation, newTime);
			}
		}
	}
}
