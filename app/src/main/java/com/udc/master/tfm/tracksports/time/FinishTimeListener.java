package com.udc.master.tfm.tracksports.time;

/**
 * Listener para saber cuanto termina un cronometro su ejecucion
 * @author a.oteroc
 *
 */
public interface FinishTimeListener {

	/**
	 * Metodo invocado cuando finaliza el tiempo especificado a un cronometro
	 * @param time
	 */
	public void onFinishTime();
}
