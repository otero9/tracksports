package com.udc.master.tfm.tracksports.utils.preferences;


/**
 * Tipos de preferencias almacenados
 * @author a.oteroc
 *
 */
public enum PreferencesTypes {
	/** Perfil por defecto que se utiliza para la realizacion de los ejercicios*/
	DEFAULT_PROFILE(false),
	/** Ejercicio pendiente para su ejecucion */
	PENDING_EXERCISE(false),
	/** Indica si se esta en el intervalor para ejecutar un ejercicio pendiente */
	IS_INTERVAL_EXECUTE_PENDING_EXERCISE(true),
	/** Indica si el sonido esta habilitado en la aplicacion */
	IS_SOUND_ACTIVE(true),
	/** Indica si la vibracion esta activada en la aplicacion */
	IS_VIBRATION_ACTIVE(true),
	/** Frecuencia de ejecucion de la alarma de comprobacion de notificaciones pendientes */
	NOTIFICATION_CHECK_PENDING_EXERCISE_HOURS(true),
	/** Frecuencia de ejecucion de la alarma de comprobacion de notificaciones pendientes cuando queda poco para la siguiente ejecucion*/
	NOTIFICATION_CHECK_PENDING_EXERCISE_CLOSE_MINUTES(true),
	/** Intervalo minimo con el que se notifica un ejercicio pendiente */
	NOTIFICATION_SEND_TIME_MINUTES(true),
	/** Frecuencia de actualizacion del mapa (en metros)*/
	MAP_UPDATE_FREQ_METERS(true),
	/** Frecuenca de actualizacion del mapa (en segundos)*/
	MAP_UPDATE_FREQ_SEC(true),
	/** Zoom por defecto sobre el mapa */
	MAP_ZOOM(true),
	/** Porcentaje de puntos almacenados por defecto */
	MAP_SAVE_POINTS(true),
	/** Tipo de mapa a mostrar */
	MAP_TYPE(true),
	/** Cuenta atras especificada en el reloj */
	CLOCK_COUNTDOWN(true),
	/** Indica si se muestran los milisegundos en el reloj */
	CLOCK_SHOW_MILISECONDS(true),
	/** Url de sincronizacion con el servicio Web */
	SYNC_URL(true),
	/** Timeout con el servicio web */
	SYNC_TIMEOUT(true),
	/** Indica si se usa HTTPS contra el servicio Web */
	SYNC_HTTPS(true),
	/** Indica si se encripta la contrasena contra el servicio Web */
	SYNC_CRYPT_PASS(true),
	/** Indica si se sincroniza automaticamente contra la plataforma */
	SYNC_AUTO(true),
	/** Timeout contra el servicio de Google FIT */
	GOOGLE_FIT_TIMEOUT(true),
	/** Indica si se sincroniza contra el servicio de Google FIT*/
	GOOGLE_FIT_SYNC(true)
	;
	
	/** Indica si el tipado es primitivo o en caso contrario un objeto complejo */
	private boolean primitiveType;
	
	/**
	 * Constructor
	 * @param primitiveType
	 */
	private PreferencesTypes(boolean primitiveType) {
		this.primitiveType = primitiveType;
	}

	/**
	 * @return the primitiveType
	 */
	public boolean isPrimitiveType() {
		return primitiveType;
	}

	/**
	 * @param primitiveType the primitiveType to set
	 */
	public void setPrimitiveType(boolean primitiveType) {
		this.primitiveType = primitiveType;
	}
}
