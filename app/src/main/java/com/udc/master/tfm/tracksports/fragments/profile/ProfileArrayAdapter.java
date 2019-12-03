package com.udc.master.tfm.tracksports.fragments.profile;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.utils.DateUtils;
import com.udc.master.tfm.tracksports.utils.FileUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Adaptador para mostrar la informacion de los perfiles en un ListView
 * @author a.oteroc
 *
 */
public class ProfileArrayAdapter extends ArrayAdapter<Profile> {

	/** Contexto de la aplicacion */
	private final Context context;
	/** Lista de perfiles */
	private List<Profile> profiles;
	
	/**
	 * Constructor del adaptador
	 * @param context
	 * @param profiles
	 */
	public ProfileArrayAdapter (Context context, List<Profile> profiles) {
		super(context, R.layout.profile_view, profiles);
		this.context = context;
		this.profiles = profiles;
	}
	
    @Override
    public int getCount() {
        return this.profiles.size();
    }
 
    @Override
    public Profile getItem(int position) {
        return this.profiles.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View rowView = convertView;

    	//Se crea la nueva vista para la fila dentro de la lista
    	if (convertView == null) {
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		rowView = inflater.inflate(R.layout.profile_view, parent, false);
    	}
        
    	//Se setea la fila con la informacion del perfil
    	ImageView profileImageView = (ImageView) rowView.findViewById(R.id.imageView_profile);
    	TextView nameTextView = (TextView) rowView.findViewById(R.id.textView_name);
    	TextView yearsTextView = (TextView) rowView.findViewById(R.id.textView_years);
    	TextView heightTextView = (TextView) rowView.findViewById(R.id.textView_height);
    	TextView weightTextView = (TextView) rowView.findViewById(R.id.textView_weigth);
    	TextView statusSyncTextView = (TextView) rowView.findViewById(R.id.textView_status_sync);
    	ImageView statusSyncImageView = (ImageView) rowView.findViewById(R.id.imageView_status_sync);
    	
    	Profile profile = profiles.get(position);
    	if (profile.getImagePath() != null) {
    		Bitmap bitmap = BitmapFactory.decodeFile(profile.getImagePath());
    		profileImageView.setImageBitmap(FileUtils.getResizedBitmap(bitmap, 80, 80));
    	}
    	nameTextView.setText(profile.getName());
		yearsTextView.setText(DateUtils.getAge(profile.getBirthday()).toString() + " " + getContext().getString(R.string.years_measure));
    	heightTextView.setText(profile.getHeight().toString() + " " + getContext().getString(R.string.height_measure));
    	weightTextView.setText(profile.getWeight().toString() + " " + getContext().getString(R.string.weight_measure));

    	if (HttpStatusType.STATUSES_OK.contains(profile.getSyncStatus())) {
    		statusSyncTextView.setText(context.getString(R.string.profile_sync));
    		statusSyncImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.sync_ok));
    	} else if (HttpStatusType.STATUSES_KO.contains(profile.getSyncStatus())) {
    		statusSyncTextView.setText(context.getString(R.string.profile_sync_problem));
    		statusSyncImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.sync_problem));
    	} else { //No sincronizado
    		statusSyncTextView.setText(context.getString(R.string.profile_not_sync));
   			statusSyncImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.not_sync));
    	}
    	
    	//Se modifica el fondo del perfil selecionado como por defecto
		Profile defaultProfile = PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, context);
		if (defaultProfile != null && profile.getUser().equals(defaultProfile.getUser())) {
			rowView.setBackgroundColor(context.getResources().getColor(R.color.dark_green));
			nameTextView.setTextColor(context.getResources().getColor(R.color.white));
			yearsTextView.setTextColor(context.getResources().getColor(R.color.white));
			heightTextView.setTextColor(context.getResources().getColor(R.color.white));
			weightTextView.setTextColor(context.getResources().getColor(R.color.white));
			statusSyncTextView.setTextColor(context.getResources().getColor(R.color.white));
		}
    	
    	return rowView;
    }

	/**
	 * @return the profiles
	 */
	public List<Profile> getProfiles() {
		return profiles;
	}

	/**
	 * @param profiles the profiles to set
	 */
	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}
}
