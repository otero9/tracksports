package com.udc.master.tfm.tracksports.time;

/**
 * Listener para saber el tiempo trascurrido con el cronometro
 * @author a.oteroc
 *
 */
public interface TimeListener {

	/**
	 * Metodo invocado cuando cambia el tiempo
	 * @param time
	 */
	public void onUpdateTime(long time);
}
