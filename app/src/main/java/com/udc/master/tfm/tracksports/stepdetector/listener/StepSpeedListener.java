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
public class StepSpeedListener implements StepListener, TimeListener {

	/** Distancia del paso del usuario (en cm) */
	private Short stepLength;
	/** Contador con los pasos caminados */
    private long stepCount = 0;
	/** Distancia total recorrida (en metros) */
	private float distance = 0;
    /** Tiempo transcurrido de la actividad */
    private long time = 0;
	/** Velocidad maxima obtenida */
	private float speedMax = 0;
	/** Velocidad agregada sumando la velocidad obtenida en todos los puntos */
	private float speedAgregate = 0;
    /** Listener encargado de mostrar la informacion de los pasos */
    private StepDisplayListener listener;
	
	/**
	 * Interfaz implementada por las clases que muestran la informacion del numero de pasos caminados
	 * @author a.oteroc
	 */
	public interface StepDisplayListener {
		/** Metodo invocado al actualizarse el numero de pasos */
		public void onStepChanged(float speed, float speedPace, float speedMax, float speedAvg);
	}
	
    /**
     * Constructor con parametros
     * @param context
     */
    public StepSpeedListener(Context context) {
    	Profile profile = PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, context);
    	if (profile != null) {
    		stepLength = profile.getStepLength();
    	} else {
    		stepLength = ExerciseUtils.DEFAULT_STEP_LENGTH;
    	}
    }
    
    /**
     * Constructor con parametros
     * @param context
     * @param listener
     */
    public StepSpeedListener(Context context, StepDisplayListener listener) {
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
		//Velocidad actual (en km/h)
		float actualSpeed = 0;
		//Ritmo actual (en min/km)
		float speedPace = 0;
		//Velocidad media (en en km/h)
		float speedAvg = 0;
		
		stepCount++;
		if (time != 0) {
			//Se actualiza la velocidad actual, la media y la maxima
			actualSpeed = (distance * 60 * 60 * 1000) / ((float)(time * 1000));
			speedAgregate += actualSpeed;
			if (actualSpeed > speedMax) {
				speedMax = actualSpeed;
			}
			if (distance != 0) {
				speedAvg = speedAgregate / stepCount;
			}
		}
		if (distance != 0) {
			//Se actualiza el ritmo
			speedPace = (time * 1000) / ((float)(distance * 60 * 1000));
		}
		if (listener != null) {
			listener.onStepChanged(actualSpeed, speedPace, speedMax, speedAvg);
		}
	}

	@Override
	public void reset() {
		stepCount = 0;
		distance = 0;
		time = 0;
		speedMax = 0;
		speedAgregate = 0;
	}
}
