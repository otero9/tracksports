package com.udc.master.tfm.tracksports.fragments.map;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.fragments.home.UpdatableRunningFragment;
import com.udc.master.tfm.tracksports.locationtracker.LocationTracker;
import com.udc.master.tfm.tracksports.map.GoogleMapTrackerFragment;

/**
 * Fragmento con el mapa de la aplicacion para mostrar 
 * la ruta por donde se realiza la actividad
 * @author a.oteroc
 *
 */
public class MapFragment extends UpdatableRunningFragment {

	/** Fragmento con el mapa */
	private GoogleMapTrackerFragment mapFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		return rootView;
	}
	
	/**
	 * Constructor vacio
	 */
	public MapFragment() {}
	
	/**
	 * Constructor del fragmento
	 * @param locationTracker
	 */
	public MapFragment(LocationTracker locationTracker) {
		setLocationTracker(locationTracker);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Se crea el fragmento del mapa solo si no instancia previamente para 
		//evitar problemas con fragmento dentro de otros fragmentos y problemas
		//de rendimiento
		FragmentManager fm = getChildFragmentManager();
		mapFragment = (GoogleMapTrackerFragment) fm.findFragmentById(R.id.map_layout);
		if (mapFragment == null) {
			mapFragment = new GoogleMapTrackerFragment();
			mapFragment.setLocationTracker(getLocationTracker());
			mapFragment.addMapTrackerListener(getDistanceListener());
			mapFragment.addMapTrackerListener(getLineRouteListener());
			mapFragment.addMapTrackerListener(getSpeedListener());
			mapFragment.addMapTrackerListener(getAltitudeListener());
			mapFragment.addMapTrackerListener(getCaloriesListener());
			fm.beginTransaction().replace(R.id.map_layout, mapFragment).commit();
			//Se anade el fragmento como listener para actualizar los valores recogidos por el GPS
			setLocationListener(mapFragment);
			setGoogleMap(mapFragment);
		}
	}
}
