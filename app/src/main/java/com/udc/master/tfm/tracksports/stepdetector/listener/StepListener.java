package com.udc.master.tfm.tracksports.stepdetector.listener;

/**
 * Interfaz implementada por clases que gestionan las notificaciones sobre la deteccion de pasos
 * @author a.oteroc
 *
 */
public interface StepListener {
	/**
	 * Metodo invocado cuando se actualizan los pasos realizados
	 */
    public void onStep();
    /**
     * Metodo invocado para resetar los valores del listener
     */
    public void reset();
}

