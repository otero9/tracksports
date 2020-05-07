package com.udc.master.tfm.tracksports.map;

import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.utils.MapUtils;

/**
 * Clase que representa el fragmento en que se selecciona la casa del usuario
 * @author a.oteroc
 *
 */
public class GoogleMapPositionFragment extends GoogleMapFragment {

	/** Marca de la posicion actual del mapa */
	private Marker markerPosition;
	
	@Override
	public void onActivityCreated() {
		if (mMap!=null) {
			mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng selectPos) {
					//Se anade una marca a la posicion seleccionada
					if (getMapPosition() == null) {
						setMapPosition(new MapPosition(selectPos.latitude, selectPos.longitude));
					} else {
						getMapPosition().setLatitude(selectPos.latitude);
						getMapPosition().setLongitude(selectPos.longitude);
					}
					markerPosition = MapUtils.addMarker(selectPos.latitude, selectPos.longitude, mMap, markerPosition);
				}
			});
		}
		
		//Si se ha especificado una posicion se anade una marca en el mapa
		if (getMapPosition() != null) {
			markerPosition = MapUtils.addMarker(getMapPosition().getLatitude(), getMapPosition().getLongitude(), mMap, markerPosition);
		}
	}
}
