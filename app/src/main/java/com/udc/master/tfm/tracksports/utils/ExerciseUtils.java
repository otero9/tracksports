package com.udc.master.tfm.tracksports.utils;

/**
 * Clase con variables y metodos de interes relacionados con el ejercicio
 * @author a.oteroc
 *
 */
public class ExerciseUtils {

	/** Distancia del paso por defecto (en cm) */
	public static final short DEFAULT_STEP_LENGTH = 30;
	
	private ExerciseUtils() {}
	
	/**
	 * Metodo que calcula el numero aproximado de METs
	 * a partir de la velocidad
	 * TODO: No se tiene en cuenta las cuestas.
	 * @param speed Velocidad en m/s
	 * @return
	 */
	public static float getMetFromSpeed(float speed) {
		float speedConverted = speed * 3.6F;
		if (speedConverted < 1f) {
			return 0f;
		} else if (speedConverted >= 1 && speedConverted <= 4.5f) {
			return 1.65f;
		} else if (speedConverted > 4.5f && speedConverted <= 5.3f) {
			return 3.55f;
		} else if (speedConverted > 5.3f && speedConverted <= 6.4f) {
			return 4.4f;
		} else if (speedConverted > 6.4f && speedConverted <= 8.4f) {
			return 7f;
		} else if (speedConverted > 8.4 && speedConverted <= 9.6f) {
			return 9.5f;
		} else if (speedConverted > 9.6f && speedConverted <= 10.8f) {
			return 10.5f;
		} else if (speedConverted > 10.8f && speedConverted <= 11.3f) {
			return 11.25f;
		} else if (speedConverted > 11.3f && speedConverted <= 12.1f) {
			return 12f;
		} else if (speedConverted > 12.1f && speedConverted <= 12.9f) {
			return 13f;
		} else if (speedConverted > 12.9f && speedConverted <= 13.8f) {
			return 13.75f;
		} else if (speedConverted > 13.8f && speedConverted <= 14.5f) {
			return 14.5f;
		} else if (speedConverted > 14.5f && speedConverted <= 16.1f) {
			return 15.5f;
		} else if (speedConverted > 16.1f && speedConverted <= 17.5f) {
			return 17f;
		} else { // > 17.5
			return 18f;
		}
	}
}
