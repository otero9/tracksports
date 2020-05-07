package com.udc.master.tfm.tracksports.fragments.profile;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.bbdd.profiles.ProfileDAO;
import com.udc.master.tfm.tracksports.common.dialog.CustomDialogBuilder;
import com.udc.master.tfm.tracksports.common.dialog.DialogArrayAdapter;
import com.udc.master.tfm.tracksports.common.dialog.DialogItem;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.directcom.rest.async.EditProfileWSAsync;
import com.udc.master.tfm.tracksports.directcom.rest.async.RegisterProfileWSAsync;
import com.udc.master.tfm.tracksports.fragments.diary.DiaryFragment;
import com.udc.master.tfm.tracksports.fragments.profile.add.AddProfileActivity;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Fragmento con la informacion de los perfiles dados de alta en la aplicacion
 * @author a.oteroc
 *
 */
public class ProfileFragment extends Fragment {

	/** Boton para dar de alta un nuevo perfil*/
	private Button addProfileButton;
	/** Lista con todos los perfiles dados de alta */
	private ListView profilesListView;
	/** Adaptador para mostrar los perfiles en la lista */
	private ProfileArrayAdapter profilesAdapter;
	/** Perfil seleccionado de la lista en caso de seleccionar alguno */
	private Profile selectProfile;
	/** Indica si es necesario refrescar la lista de perfiles */
	private boolean refreshProfileList;
	/** Indica si al crear un usuario se establecera como por defecto */
	private boolean setDefaultProfile;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final Resources resources = getActivity().getResources();
		View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
		
		addProfileButton = (Button) profileView.findViewById(R.id.button_add_profile);
		profilesListView = (ListView) profileView.findViewById(R.id.listView_profiles);

		//Se abre el formulario para anadir un perfil
		addProfileButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), AddProfileActivity.class);
				intent.putExtra(ConstantsUtils.SET_DEFAULT_PARAM, setDefaultProfile);
				getActivity().startActivity(intent);
				refreshProfileList = true;
			}
		});
		
		//Se obtiene los perfiles de BBDD y se setean en la lista
		setProfileListAdapter(true);
		
		//Se crea el dialogo para seleccionar el metodo de obtener la imagen de perfil
    	DialogArrayAdapter dialogAdapter = new DialogArrayAdapter(getActivity(), getItems(resources));
    	
		CustomDialogBuilder builder = new CustomDialogBuilder(getActivity());
		builder.setTitle(resources.getString(R.string.image_picker_title_message));
		builder.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	if (which == ProfileOptionType.EDIT_PROFILE.getId()) {
		    		//Editar el perfil
		    		if (selectProfile != null) {
						Intent intent = new Intent(getActivity(), AddProfileActivity.class);
						intent.putExtra(ConstantsUtils.PROFILE_PARAM, selectProfile);
						intent.putExtra(ConstantsUtils.EDIT_PROFILE_PARAM, true);
						getActivity().startActivity(intent);
						refreshProfileList = true;
						//Se refresca la lista de ejercicios
						refreshExercises();
		    		}
		    	} else if (which == ProfileOptionType.REMOVE_PROFILE.getId()) {
		    		//Se elimina el perfil
		    		if (selectProfile != null) {
		    			confirmDeleteProfile();
		    		}
		    	} else {
		    		//Se sincroniza el perfil
		    		if (selectProfile != null) {
		    			//Si el perfil ya esta sincronizado se muestra unicamente un aviso
		    			if (HttpStatusType.STATUSES_OK.contains(selectProfile.getSyncStatus())) {
		    				Toast toast = Toast.makeText(getActivity(), R.string.profile_already_sync, Toast.LENGTH_SHORT);
		    				toast.show();
		    			}
		    			//Si ya se tiene informado el guid se envia una edicion sino un registro
		    			else if (selectProfile.getGuid() != null) {
		    				EditProfileWSAsync editProfileWSAsync = new EditProfileWSAsync(getActivity(), selectProfile, true);
		    				editProfileWSAsync.execute();
		    			} else {
		    				RegisterProfileWSAsync registerWSAsync = new RegisterProfileWSAsync(getActivity(), selectProfile, true);
		    				registerWSAsync.execute();
		    			}
		    		}
		    	}
		    	
		    }
		});
    	final AlertDialog dialog = builder.create();
		
		//Se abre el dialogo de acciones si se selecciona un perfil en concreto
		profilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
	        	//Se marca el perfil seleccionado
	        	Profile profile = (Profile) parent.getItemAtPosition(position);
	        	selectProfile = profile;
	        	dialog.show();
	        }
		});
		
		//Se almacena el perfil selecionado como por defecto para las mediciones
		profilesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Profile profile = (Profile) parent.getItemAtPosition(position);
				//Se almacena el perfil en las preferencias del sistema
				PreferencesUtils.savePreferences(PreferencesTypes.DEFAULT_PROFILE, profile, getActivity());
				//Se refresca la lista de ejercicios
				refreshExercises();
				//Se actualiza la lista de perfiles
				setProfileListAdapter(true);
				Toast toast = Toast.makeText(getActivity(), R.string.profile_option_set_default, Toast.LENGTH_SHORT);
				toast.show();
				return true;
			}
		});
		
		return profileView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (refreshProfileList) {
			//Se actualiza la lista de perfiles
			setProfileListAdapter(true);
		}
		refreshProfileList = false;
	}
	
	/**
	 * Metodo que crea o actualiza el adaptador para gestionar la lista de perfiles
	 * @param forceCreate Fuerza la creacion del adaptador
	 */
	private void setProfileListAdapter(boolean forceCreate) {
		ProfileDAO profileHelper = DatabaseFactory.getInstance().getProfileDAO(getActivity());
		List<Profile> profiles = profileHelper.findAllProfiles();
		
		if (forceCreate) {
			profilesAdapter = null;
		}
		
		//Si no se devuelven resultados se setea la lista vacia
		if (profiles == null) {
			profiles = new ArrayList<Profile>();
		}
		//Si se crea un perfil y no existe ningun se establecera como por defecto
		if (profiles.isEmpty()) {
			setDefaultProfile = true;
		}
		
		if (profilesAdapter != null) {
			profilesAdapter.setProfiles(profiles);
			profilesAdapter.notifyDataSetChanged();
		} else {
			profilesAdapter = new ProfileArrayAdapter(getActivity(), profiles);
			profilesListView.setAdapter(profilesAdapter);
		}
	}
	
    /**
     * Metodo que obtiene la lista de elementos para mostrar
     * en el dialogo de seleccionar imagen
     * @param resources
     * @return
     */
    private List<DialogItem> getItems(Resources resources) {
    	List<DialogItem> items = new ArrayList<DialogItem>();
    	
    	String [] elements = resources.getStringArray(R.array.profileOptions);
    	for (int i = 0; i < elements.length; i++) {
    		Drawable image = null;
    		if (i == ProfileOptionType.EDIT_PROFILE.getId()) {
    			image = resources.getDrawable(R.drawable.edit);
    		} else if (i == ProfileOptionType.REMOVE_PROFILE.getId()) {
    			image = resources.getDrawable(R.drawable.delete);
    		} else {
    			image = resources.getDrawable(R.drawable.sync_ok);
    		}
    		DialogItem item = new DialogItem(image, elements[i]);
    		items.add(item);
		}
    	return items;
    }
    
    /**
     * Metodo para refrescar los ejercicios del TAB Diario
     */
    private void refreshExercises() {
    	List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
    	
    	for (Fragment fragment : fragments) {
			if (fragment instanceof DiaryFragment)  {
				DiaryFragment diaryFragment = (DiaryFragment) fragment;
				diaryFragment.refreshExercises();
				return;
			}
		}
    }
    
    /**
     * Metodo para refrescar la lista de perfiles
     */
    public void refreshProfiles() {
    	this.setProfileListAdapter(false);
    }
    
    /**
     * Metodo que envia una alertar para confirmar la eliminacion de un perfil
     * Si el usuario lo acepta, se elimina
     */
	private void confirmDeleteProfile() {
		CustomDialogBuilder alertDialog = new CustomDialogBuilder(getActivity());
        alertDialog.setTitle(R.string.profile_delete_confirmation_title);
        alertDialog.setMessage(R.string.profile_delete_confirmation_content);
        //Si se presiona el boton de configuracion
        alertDialog.setPositiveButton(R.string.profile_delete_confirmation_ok_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	ProfileDAO profileHelper = DatabaseFactory.getInstance().getProfileDAO(getActivity());
    			profileHelper.delete(selectProfile);
				Toast toast = Toast.makeText(getActivity(), R.string.profile_option_remove_succesful, Toast.LENGTH_SHORT);
				toast.show();
				//Se actualiza la lista de perfiles
				setProfileListAdapter(true);
				//Si el perfil era el establecido por defecto se elimina de las preferencias
				Profile defaultProfile = PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, getActivity());
				if (defaultProfile != null && selectProfile.getUser().equals(defaultProfile.getUser())) {
					PreferencesUtils.deletePreferences(PreferencesTypes.DEFAULT_PROFILE, getActivity());
				}
				//Se refresca la lista de ejercicios
				refreshExercises();
            }
        });
        //Si se presiona el boton de cancelar
        alertDialog.setNegativeButton(R.string.profile_delete_confirmation_cance_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Se muestra el dialogo
        alertDialog.show();
	}
}
