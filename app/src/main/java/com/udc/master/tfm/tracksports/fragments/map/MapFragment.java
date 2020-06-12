package com.udc.master.tfm.tracksports.fragments.map;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.fragments.home.UpdatableRunningFragment;
import com.udc.master.tfm.tracksports.locationtracker.LocationTracker;
import com.udc.master.tfm.tracksports.map.GoogleMapTrackerFragment;
import com.udc.master.tfm.tracksports.utils.MapUtils;
import com.udc.master.tfm.tracksports.utils.preferences.MapType;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Fragmento con el mapa de la aplicacion para mostrar 
 * la ruta por donde se realiza la actividad
 * @author a.oteroc
 *
 */
public class MapFragment extends UpdatableRunningFragment implements OnMapReadyCallback {

	/** Fragmento con el mapa */
	private GoogleMapTrackerFragment mapFragment;
	private GoogleMap mMap;
	private MapPosition mapPosition;
	/*** Variable que indica si el usuario interactuo con el mapa */
	private boolean userInteraction = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map, container, false);

		SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
				.findFragmentById(R.id.map_layout);
		mapFragment.getMapAsync(this);

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
		SupportMapFragment map = (SupportMapFragment) this.getChildFragmentManager()
				.findFragmentById(R.id.map_layout);

		if (map == null) {
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

	@Override
	public void onMapReady(GoogleMap map) {
		mMap = map;
		if (mMap!=null) {
			mMap.getUiSettings().setCompassEnabled(true);
			mMap.getUiSettings().setMyLocationButtonEnabled(true);
			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setZoomControlsEnabled(true);
			Short mapTypeId = PreferencesUtils.getPreferences(PreferencesTypes.MAP_TYPE, Short.class, getActivity());
			if (mapTypeId!=null) {
				mMap.setMapType(MapType.valueOf(mapTypeId).getMapType());

			}
			mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
				@Override
				public boolean onMyLocationButtonClick() {
					userInteraction = false;
					//Si se pulsa se actualiza la camara a la posicion actual
					if (mapPosition != null) {
						MapUtils.updateCameraPosition(mapPosition.getLatitude(), mapPosition.getLongitude(), mMap);
					} else {
						LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
						Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						double lat = locationGPS.getLatitude();
						double longi = locationGPS.getLongitude();
						mapPosition = new MapPosition();
						mapPosition.setLatitude(lat);
						mapPosition.setLongitude(longi);
						MapUtils.updateCameraPosition(mapPosition.getLatitude(), mapPosition.getLongitude(), mMap);
					}
					return true;
				}
			});
		}
	}
}
