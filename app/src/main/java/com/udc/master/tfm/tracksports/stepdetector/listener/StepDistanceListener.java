package com.udc.master.tfm.tracksports.stepdetector.listener;

import android.content.Context;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.utils.ExerciseUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Clase encargada de medir la distancia caminada a traves de los pasos
 * @author a.oteroc
 *
 */
public class StepDistanceListener implements StepListener {

	/** Distancia del paso del usuario (en cm) */
	private Short stepLength;
	/** Distancia total recorrida (en metros)*/
	private float distance = 0;
	/** Distancia total especificada a recorrer */
	private float totalDistance = 0;
	/** Distancia restante a recorrer */
	private float remainingDistance = 0;
    /** Listener encargado de mostrar la informacion de los pasos */
    private StepDisplayListener stepDisplayListener;
	/** Listener encargado de notificar si se ha completada la distancia a recorrer */
    private DistanceReaminingComplete distanceReaminingComplete;
    
	/**
	 * Interfaz implementada por las clases que muestran la informacion del numero de pasos caminados
	 * @author a.oteroc
	 */
	public interface StepDisplayListener {
		/** Metodo invocado al actualizarse el numero de pasos */
		public void onStepChanged(float distance, float distanceRemaining);
	}
	
	/**
	 * Interfaz implementada por las clases que reciben la notificacion de distancia recorrida completada
	 * @author a.oteroc
	 *
	 */
	public interface DistanceReaminingComplete {
		/** Metodo invocado al completarse la distancia recorrida */
		public void onCompleteStepDistance();
	}
	
    /**
     * Constructor con parametros
     * @param context
     */
    public StepDistanceListener(Context context) {
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
     * @param stepDisplayListener
     * @param distanceReaminingComplete
     * @param totalDistance
     */
    public StepDistanceListener(Context context, StepDisplayListener stepDisplayListener, DistanceReaminingComplete distanceReaminingComplete, float totalDistance) {
    	this(context);
    	this.stepDisplayListener = stepDisplayListener;
    	this.distanceReaminingComplete = distanceReaminingComplete;
    	this.totalDistance = totalDistance;
    	this.remainingDistance = totalDistance;
    }
    
    /**
     * Constructor con parametros
     * @param context
     * @param stepDisplayListener
     */
    public StepDistanceListener(Context context, StepDisplayListener stepDisplayListener) {
    	this(context);
    	this.stepDisplayListener = stepDisplayListener;
    }
    
    public void onStep() {
    	distance += ((float)stepLength / 100);
    	if (remainingDistance > 0) {
    		remainingDistance -= ((float)stepLength / 100);
    	} else {
    		remainingDistance -= 0;
    	}
        if (stepDisplayListener != null) {
        	stepDisplayListener.onStepChanged(distance, remainingDistance);
        }
		if (distanceReaminingComplete != null && totalDistance > 0 && remainingDistance <= 0) {
			distanceReaminingComplete.onCompleteStepDistance();
		}
    }

	@Override
	public void reset() {
		distance = 0;
		totalDistance = 0;
		remainingDistance = 0;
	}

}
