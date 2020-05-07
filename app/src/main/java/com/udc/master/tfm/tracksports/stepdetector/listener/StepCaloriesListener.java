package com.udc.master.tfm.tracksports.stepdetector.listener;

import android.content.Context;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.time.TimeListener;
import com.udc.master.tfm.tracksports.utils.ExerciseUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Clase encargada de medir la velocidad a traves de los pasos
 * @author a.oteroc
 *
 */
public class StepCaloriesListener implements StepListener, TimeListener {

	/** Distancia del paso del usuario (en cm) */
	private Short stepLength;
	/** Peso del usuario (kg)*/
	private Short weigth;
	/** Distancia total recorrida (en metros) */
	private float distance = 0;
    /** Tiempo transcurrido de la actividad */
    private long time = 0;
    /** Listener encargado de mostrar la informacion de los pasos */
    private StepDisplayListener listener;
	
	/**
	 * Interfaz implementada por las clases que muestran la informacion del numero de pasos caminados
	 * @author a.oteroc
	 */
	public interface StepDisplayListener {
		/** Metodo invocado al actualizarse el numero de pasos */
		public void onStepChanged(float caloriesBurned, float caloriesBurnedPace);
	}
	
    /**
     * Constructor con parametros
     * @param context
     */
    public StepCaloriesListener(Context context) {
    	Profile profile = PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, context);
    	if (profile != null) {
    		stepLength = profile.getStepLength();
    		weigth = profile.getWeight();
    	} else {
    		stepLength = ExerciseUtils.DEFAULT_STEP_LENGTH;
    	}
    }
    
    /**
     * Constructor con parametros
     * @param context
     * @param listener
     */
    public StepCaloriesListener(Context context, StepDisplayListener listener) {
    	this(context);
    	this.listener = listener;
    }
    
    public void onStep() {
    	distance += ((float)stepLength / 100);
    	this.update();
    }

	@Override
	public void onUpdateTime(long time) {
		this.time = time;
	}
	
	/**
	 * Metodo que actualiza la velocidad actual
	 */
	private void update() {
		//KCal quemadas por minuto
		float kcalMin = 0;
		//Kcal quemadas en total durante el ejercicio
		float kcalBurned = 0; 
		if (weigth != null) {
			if (time != 0) {
				float actualSpeed = (distance *  1000) / ((float)(time));
				float met = ExerciseUtils.getMetFromSpeed(actualSpeed);
				kcalMin = (met  * (float)weigth) / (float)60;
				if (time != 0) {
					kcalBurned = kcalMin * (float)(time / (float)(1000 * 60));
				}
			}
		}
		if (listener != null) {
			listener.onStepChanged(kcalBurned, kcalMin);
		}
	}

	@Override
	public void reset() {
		distance = 0;
		time = 0;
	}

}
