package com.udc.master.tfm.tracksports.stepdetector.listener;

import com.udc.master.tfm.tracksports.time.TimeListener;

/**
 * Clase que cuenta el ritmo de pasos por minuto (pasos / minuto)
 * @author a.oteroc
 *
 */
public class StepPaceListener implements StepListener, TimeListener {
	
	/** Contador con los pasos caminados */
    private long stepCount = 0;
    /** Tiempo transcurrido de la actividad */
    private long time = 0;
    /** Ritmo (pasos/minuto) */
    private float stepPace = 0;
    
    /** Listener encargado de mostrar la informacion de los pasos */
    private StepDisplayListener listener;
	
	/**
	 * Interfaz implementada por las clases que muestran la informacion del numero de pasos caminados
	 * @author a.oteroc
	 */
	public interface StepDisplayListener {
		/** Metodo invocado al actualizarse el numero de pasos */
		public void onStepChanged(float value);
	}
	
    /**
     * Constructor vacio
     */
    public StepPaceListener() {}
    
    /**
     * Constructor con parametros
     * @param listener
     */
    public StepPaceListener(StepDisplayListener listener) {
    	this.listener = listener;
    }
    
    public void onStep() {
        stepCount ++;
        this.update();
    }

	@Override
	public void onUpdateTime(long time) {
		this.time = time;
	}
	
	/**
	 * Metodo que actualiza el ritmo actual
	 */
	private void update() {
		if (time != 0) {
        	stepPace = (float)stepCount / (float)time;
        	stepPace = stepPace * 1000 * 60;
		}
		if (listener != null) {
			listener.onStepChanged(stepPace);
		}
	}

	@Override
	public void reset() {
		stepCount = 0;
		time = 0;
		stepPace = 0;
	}
}
