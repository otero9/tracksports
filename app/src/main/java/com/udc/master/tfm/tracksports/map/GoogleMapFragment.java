package com.udc.master.tfm.tracksports.map;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.common.touch.UserInteractionListener;
import com.udc.master.tfm.tracksports.utils.MapUtils;
import com.udc.master.tfm.tracksports.utils.preferences.MapType;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Clase abstracta que representa el fragmento que muestra GoogleMaps
 * @author a.oteroc
 *
 */
public abstract class GoogleMapFragment extends Fragment implements UserInteractionListener, OnMapReadyCallback {

	GoogleMap mMap;
	//SupportMapFragment mapFragment;
	/** Posicion actual del mapa */
	private MapPosition mapPosition;
	/*** Variable que indica si el usuario interactuo con el mapa */
	private boolean userInteraction = false;

	/**
	 * Constructor del fragmento
	 */
	public GoogleMapFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_map, null, false);
		//View originalContentView = super.onCreateView(inflater, parent, savedInstanceState);

		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
				.findFragmentById(R.id.map_layout);
		mapFragment.getMapAsync(this);

		//Se envuelve la actividad para gestionar las interacciones con el mapa
		//TouchableWrapper touchView = new TouchableWrapper(getActivity(), this);
		//if (originalContentView != null) {
		//	touchView.addView(originalContentView);
		//}

		//return touchView;
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//TODO: Configurable en la aplicacion
		if (mMap != null) {
			mMap.getUiSettings().setCompassEnabled(true);
			mMap.getUiSettings().setMyLocationButtonEnabled(true);
			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setZoomControlsEnabled(true);
		}
		Short mapTypeId = PreferencesUtils.getPreferences(PreferencesTypes.MAP_TYPE, Short.class, getActivity());
		if (mapTypeId != null && mMap!=null) {
			mMap.setMapType(MapType.valueOf(mapTypeId).getMapType());
		}

		if (mMap!=null) {
			//Evento para gestionar las pulsaciones en el boton de Mi Localizacion
			mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
				@Override
				public boolean onMyLocationButtonClick() {
					userInteraction = false;
					//Si se pulsa se actualiza la camara a la posicion actual
					if (mapPosition != null) {
						MapUtils.updateCameraPosition(mapPosition.getLatitude(), mapPosition.getLongitude(), mMap);
					} else {
						mapPosition = new MapPosition();
						MapUtils.updateCameraPosition(mapPosition.getLatitude(), mapPosition.getLongitude(), mMap);
					}
					return true;
				}
			});
		}
		
		this.onActivityCreated();
	}
	
	@Override
	public void onUpdateAfterUserInteraction() {
		userInteraction = true;	
	}
	
	/**
	 * @return the mapPosition
	 */
	public MapPosition getMapPosition() {
		return mapPosition;
	}
	
	/**
	 * @param mapPosition the mapPosition to set
	 */
	public void setMapPosition(MapPosition mapPosition) {
		this.mapPosition = mapPosition;
	}
	
	/**
	 * @return the userInteraction
	 */
	public boolean isUserInteraction() {
		return userInteraction;
	}
	
	/**
	 * Metodo para limpiar la informacion del mapa
	 */
	public void clear() {
		if (mMap != null) {
		  mMap.clear();
		}
	}

	/**
	 * Metodo abstracto que se invoca tras crear la actividad que engloba al fragmento
	 */
	public abstract void onActivityCreated();

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.mMap = googleMap;    //which doesn't seem to work
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
