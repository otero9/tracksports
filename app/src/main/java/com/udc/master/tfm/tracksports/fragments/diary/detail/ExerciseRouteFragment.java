package com.udc.master.tfm.tracksports.fragments.diary.detail;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.map.GoogleMapFragment;
import com.udc.master.tfm.tracksports.map.GoogleMapRouteFragment;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;

/**
 * Fragmento con la ruta realizada en un ejercicio
 * @author a.oteroc
 *
 */
public class ExerciseRouteFragment extends Fragment {

	/** Fragmento con el mapa */
	private GoogleMapFragment mapFragment;
	/** Ejercicio que realiza el usuario */
	private Exercise exercise;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_exercise_route, container, false);
		exercise = (Exercise) getActivity().getIntent().getSerializableExtra(ConstantsUtils.EXERCISE_PARAM);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Se crea el fragmento del mapa solo si no instancia previamente para 
		//evitar problemas con fragmento dentro de otros fragmentos y problemas
		//de rendimiento
		FragmentManager fm = getChildFragmentManager();
		mapFragment = (GoogleMapFragment) fm.findFragmentById(R.id.map_route_layout);
		if (mapFragment == null) {
			mapFragment = new GoogleMapRouteFragment(exercise);
			fm.beginTransaction().replace(R.id.map_route_layout, mapFragment).commit();
		}
	}
}
