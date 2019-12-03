package com.udc.master.tfm.tracksports.utils;


/**
 * Clase de utilidad para definir las constantes para pasar valores
 * entre fragmentos y actividades
 * @author a.oteroc
 *
 */
public class ConstantsUtils {

	/** Parametro utilizado para comunicar un perfil entre fragmentos */
	public static final String PROFILE_PARAM = "profile";
	/** Parametro utilizado para comunicar si se editar o no un perfil entre fragmentos */
	public static final String EDIT_PROFILE_PARAM = "editProfile";
	/** Parametro utilizado para comunicar si el perfil a crear se establecera como por defecto */
	public static final String SET_DEFAULT_PARAM = "setDefault";
	/** Parametro utilizado para mostrar el detalle de un ejercicio */
	public static final String EXERCISE_PARAM = "exercise";
	/** Parametro utilizado para descartar un ejercicio pendiente */
	public static final String PENDING_EXERCISE_PARAM = "pendingExercise";
	/** Parametro utilizado para cancelar una notificacion de un ejercicio pendiente */
	public static final String NOTIFICATION_ID_PARAM = "notificationid";
	/** Parametro utilizado para iniciar un ejercicio de forma automatica */
	public static final String AUTO_START_PARAM = "autoStart";
	
	private ConstantsUtils() {}
	
}
