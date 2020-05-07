package com.udc.master.tfm.tracksports.stepdetector.listener;

/**
 * Clase que cuenta el numero de pasos mediante el StepDetector
 * @author a.oteroc
 *
 */
public class StepCountListener implements StepListener {
	
	/** Contador con los pasos caminados */
    private long stepCount = 0;
    /** Listener encargado de mostrar la informacion de los pasos */
    private StepDisplayListener listener;
	
	/**
	 * Interfaz implementada por las clases que muestran la informacion del numero de pasos caminados
	 * @author a.oteroc
	 */
	public interface StepDisplayListener {
		/** Metodo invocado al actualizarse el numero de pasos */
		public void onStepChanged(long value);
	}
	
    /**
     * Constructor vacio
     */
    public StepCountListener() {}
    
    /**
     * Constructor con parametros
     * @param listener
     */
    public StepCountListener(StepDisplayListener listener) {
    	this.listener = listener;
    }
    
    public void onStep() {
        stepCount ++;
        if (listener != null) {
        	listener.onStepChanged(stepCount);
        }
    }

	@Override
	public void reset() {
		stepCount = 0;
	}
}
