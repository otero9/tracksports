package com.udc.master.tfm.tracksports.fragments.profile.add;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.bbdd.profiles.ProfileDAO;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.directcom.rest.async.EditProfileWSAsync;
import com.udc.master.tfm.tracksports.directcom.rest.async.RegisterProfileWSAsync;
import com.udc.master.tfm.tracksports.map.GoogleMapFragment;
import com.udc.master.tfm.tracksports.map.GoogleMapPositionFragment;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Fragmento para recoger las coordenadas de la casa del usuario
 * @author a.oteroc
 *
 */
public class MapPositionFragment extends AddProfileAbstractFragment {

	/** Fragmento con el mapa */
	private GoogleMapFragment mapFragment;
	
	public MapPositionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mapPositionView = inflater.inflate(R.layout.fragment_map_position, container, false);
		
		setPreviousButton((ImageButton) mapPositionView.findViewById(R.id.button_map_position_previous));
		setFinishButton((ImageButton) mapPositionView.findViewById(R.id.button_map_position_finish));
		
		//Se navega a la pagina de seleccion de imagen
		getPreviousButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setParams();
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, new ImageSelectFragment()).commit();
			}
		});	
		
		//Se almacena el perfil y se cierra la actividad
		getFinishButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setParams();
				//Se comprueba si la accion que hay que realizar es guardar un nuevo perfil o editarlo
				boolean editProfile = getActivity().getIntent().getExtras().getBoolean(ConstantsUtils.EDIT_PROFILE_PARAM, false);
				Profile profile = saveProfile(editProfile);
				saveWSProfile(profile);
			}
		});
		
		return mapPositionView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Se crea el fragmento del mapa solo si no instancia previamente para 
		//evitar problemas con fragmento dentro de otros fragmentos y problemas
		//de rendimiento
		FragmentManager fm = getChildFragmentManager();
		mapFragment = (GoogleMapFragment) fm.findFragmentById(R.id.map_position_layout);
		if (mapFragment == null) {
			mapFragment = new GoogleMapPositionFragment();
			fm.beginTransaction().replace(R.id.map_position_layout, mapFragment).commit();
		}
		
		fillParams();
	}
	
	/**
	 * Metodo que almacena el perfil en BBDD con los datos introducidos.
	 * Si <code>editProfile</code> es true se actualizaran los datos del perfil. 
	 * @param editProfile
	 */
	private Profile saveProfile(boolean editProfile) {
		Profile profile = (Profile)getActivity().getIntent().getSerializableExtra(ConstantsUtils.PROFILE_PARAM);
		if (profile != null) {
			ProfileDAO profileHelper = DatabaseFactory.getInstance().getProfileDAO(getActivity());
			if (editProfile) {
				profileHelper.update(profile);
				//Si se edita el perfil, se actualiza el perfil por defecto en caso de existir alguno
				PreferencesUtils.savePreferences(PreferencesTypes.DEFAULT_PROFILE, profile, getActivity());
			} else {
				int id = profileHelper.insert(profile);
				profile.setId(id);
				//Se establece el usuario como por defecto si corresponde
				boolean setDefaultProfile = getActivity().getIntent().getExtras().getBoolean(ConstantsUtils.SET_DEFAULT_PARAM, false);
				if (setDefaultProfile) {
					PreferencesUtils.savePreferences(PreferencesTypes.DEFAULT_PROFILE, profile, getActivity());
				}
			}
		}
		return profile;
	}

	/**
	 * Metodo que registra el usuario contra la plataforma Web
	 * @param profile
	 */
	private void saveWSProfile(Profile profile) {
		boolean syncAuto = Boolean.TRUE.equals(PreferencesUtils.getPreferences(PreferencesTypes.SYNC_AUTO, Boolean.class, getActivity()));
		//Si el usuario no existe se registra
		//Si la autenciacion fue satisfactoria se edita el perfil
		//En otro caso no se envia peticion y se guarda el ejercicio como no sincronizado
		if (HttpStatusType.USER_NOT_VALID.equals(profile.getSyncStatus()) && syncAuto) {
			RegisterProfileWSAsync registerWSAsync = new RegisterProfileWSAsync(getActivity(), profile, false);
			registerWSAsync.execute();	
		} else if (HttpStatusType.STATUSES_OK.contains(profile.getSyncStatus()) && syncAuto) {
			EditProfileWSAsync editProfileWSAsync = new EditProfileWSAsync(getActivity(), profile, false);
			editProfileWSAsync.execute();
		} else {
			//Si se edita el perfil o se registra se finaliza la actividad dentro de la actividad asincrona
			getActivity().finish();
		}
	}
	
	@Override
	protected void fillParams() {
		Profile profile = (Profile)getActivity().getIntent().getSerializableExtra(ConstantsUtils.PROFILE_PARAM);
		if (profile != null && profile.getMapPosition() != null) {
			try {
				mapFragment.setMapPosition(profile.getMapPosition());
			} catch (Exception e) {
				//TODO: Anadir validaciones de forma correcta
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void setParams() {
		Profile profile = (Profile)getActivity().getIntent().getSerializableExtra(ConstantsUtils.PROFILE_PARAM);
		try {
			profile.setMapPosition(mapFragment.getMapPosition());
		} catch (Exception e) {
			//TODO: Anadir validaciones de forma correcta
			e.printStackTrace();
		}
	}
}
