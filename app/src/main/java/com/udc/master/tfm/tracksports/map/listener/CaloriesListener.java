package com.udc.master.tfm.tracksports.map.listener;

import android.content.Context;
import android.location.Location;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.time.TimeListener;
import com.udc.master.tfm.tracksports.utils.ExerciseUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Listener encargado de actualizar las calorias quemadas por el usuario
 * @author a.oteroc
 *
 */
public class CaloriesListener implements MapTrackerListener, TimeListener {

	private Short weigth;
    /** Tiempo transcurrido de la actividad */
    private long time = 0;
	
    private CaloriesDisplayListener listener;
    
	/**
	 * Interfaz implementada por las clases que muestran la informacion de las calorias quemadas por el usuario
	 * @author a.oteroc
	 */
	public interface CaloriesDisplayListener {
		/** Metodo invocado al actualizarse las calorias quemadas por el usuario */
		public void onPositionChanged(float caloriesBurned, float caloriesBurnedPace);
	}
    
	/**
	 * Constructor de la clase
	 * @param context
	 */
	public CaloriesListener(Context context) {
		Profile profile = PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, context);
		if (profile != null) {
			weigth = profile.getWeight();
		}
	}
	
	/**
	 * Constructor de la clase
	 * @param context
	 */
	public CaloriesListener(Context context, CaloriesDisplayListener listener) {
		Profile profile = PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, context);
		if (profile != null) {
			weigth = profile.getWeight();
		}
		this.listener = listener;
	}
	
	@Override
	public void onUpdatePosition(Location oldLocation, long oldTime, Location newLocation, long newTime) {
		if (weigth != null) {
			float met = ExerciseUtils.getMetFromSpeed(newLocation.getSpeed());
			float kcalMin = (met  * (float)weigth) / (float)60;
			float kcalBurned = 0;
			if (time != 0) {
				kcalBurned = kcalMin * (float)(time / (float)(1000 * 60));
			}
			if (listener != null) {
				listener.onPositionChanged(kcalBurned, kcalMin);
			}
		}
	}

	@Override
	public void onUpdateTime(long time) {
		this.time = time;
	}

	@Override
	public void reset() {
		time = 0;
	}
}
