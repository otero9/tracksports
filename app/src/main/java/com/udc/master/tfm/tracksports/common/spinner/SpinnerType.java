package com.udc.master.tfm.tracksports.common.spinner;

import com.udc.master.tfm.tracksports.R;

/**
 * Tipos de valores a mostrar en un spinner
 * @author a.oteroc
 *
 */
public enum SpinnerType {
	
	/** Tiempo recorrido */
	CHRONO(R.string.spinner_chrono_title, R.drawable.chronometer),
	/** Tiempo recorrido */
	CHRONO_TIME_REMAINING(R.string.spinner_chrono_time_remaining_title, R.drawable.chronometer),
	/** Velocidad actual*/
	SPEED(R.string.spinner_speed_title, R.drawable.speed), 
	/** Ritmo de el tiempo que lleva un kilometro */
	SPEED_PACE(R.string.spinner_speed_pace_title, R.drawable.speed), 
	/** Velocidad maxima alcanzada*/
	SPEED_MAX(R.string.spinner_speed_max_title, R.drawable.speed),
	/** Velocidad media */
	SPEED_AVG(R.string.spinner_speed_avg_title, R.drawable.speed),
	/** Distancia total recorrida */
	DISTANCE(R.string.spinner_distance_title, R.drawable.distance),
	/** Distancia restante por recorrer */
	DISTANCE_REMAINING(R.string.spinner_distance_remaining_title, R.drawable.distance),
	/** Pasos caminados */
	STEPS(R.string.spinner_steps_title, R.drawable.step),
	/** Calorias quemadas */
	CALORIES_BURNED(R.string.spinner_calories_burned_title, R.drawable.calories_burned),
	/** Calorias quemadas minuto */
	CALORIES_BURNED_PACE(R.string.spinner_calories_burned_pace_title, R.drawable.calories_burned),
	/** Pasos caminados por minuto */
	STEP_PACE(R.string.spinner_step_pace_title, R.drawable.step),
	/** Altitud actual */
	ALTITUDE(R.string.spinner_altitude_title, R.drawable.altitude),
	/** Altitud minima */
	ALTITUDE_MIN(R.string.spinner_altitude_min_title, R.drawable.altitude),
	/** Altitud maxima */
	ALTITUDE_MAX(R.string.spinner_altitude_max_title, R.drawable.altitude);
	
	/** Identificador del titulo del spinner */
	private int titleId;
	/** Identificador de la imagen del spinner */
	private int imageId;
	
	/**
	 * 
	 * @param titleId
	 * @param imageId
	 */
	private SpinnerType(int titleId, int imageId) {
		this.titleId = titleId;
		this.imageId = imageId;
	}

	/**
	 * @return the titleId
	 */
	public int getTitleId() {
		return titleId;
	}

	/**
	 * @return the imageId
	 */
	public int getImageId() {
		return imageId;
	}
}
