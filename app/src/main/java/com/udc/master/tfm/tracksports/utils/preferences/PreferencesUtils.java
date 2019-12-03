package com.udc.master.tfm.tracksports.utils.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Clase de utilidad sobre las preferencias de la aplicacion
 * @author a.oteroc
 *
 */
public class PreferencesUtils {
	
	/** Nombre de las preferencias de la aplicacion */
	private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
	/** Indica las configuraciones por defecto en caso de no estar especificadas */
	private static final Boolean DEFAULT_SOUND_ACTIVE = Boolean.TRUE;
	private static final Boolean DEFAULT_VIBRATION_ACTIVE = Boolean.TRUE;
	private static final Integer DEFAULT_MAP_UPDATE_FREQ_METERS = 10;
	private static final Integer DEFAULT_NOTIFICATION_CHECK_PENDING_EXERCISE_HOURS = 2;
	private static final Integer DEFAULT_NOTIFICATION_CHECK_PENDING_EXERCISE_CLOSE_MINUTES = 15;
	private static final Integer DEFAULT_NOTIFICATION_SEND_TIME_MINUTES = 60;
	private static final Integer DEFAULT_MAP_UPDATE_FREQ_SEC = 1;
	private static final Integer DEFAULT_MAP_ZOOM = 18;
	private static final Integer DEFAULT_MAP_SAVE_POINTS = 100;
	private static final MapType DEFAULT_MAP_TYPE = MapType.NORMAL;
	private static final Integer DEFAULT_CLOCK_COUNTDOWN = 5;
	private static final Boolean DEFAULT_CLOCK_SHOW_MILISECONDS = Boolean.FALSE;
	private static final String DEFAULT_SYNC_URL = "";
	private static final Integer DEFAULT_SYNC_TIMEOUT = 1;
	private static final Boolean DEFAULT_SYNC_HTTPS = Boolean.FALSE;
	private static final Boolean DEFAULT_SYNC_CRYPT_PASS = Boolean.FALSE;
	private static final Boolean DEFAULT_SYNC_AUTO = Boolean.FALSE;
	private static final Integer DEFAULT_GOOGLE_FIT_TIMEOUT = 10;
	private static final Boolean DEFAULT_GOOGLE_FIT_SYNC = Boolean.FALSE;
	
	/**
	 * Metodo que almacena una preferencia
	 * @param preference
	 * @param param
	 * @param context
	 */
	public static <T> void savePreferences(PreferencesTypes preference, T param, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		//Se almacena como un json si es un objeto completo
		if (!preference.isPrimitiveType()) {
			String json = null;
			if (param != null) {
				Gson gson = new Gson();
				json = gson.toJson(param);
			}
			editor.putString(preference.toString(), json);
		} else {
			editor.putString(preference.toString(), param.toString());
		}

		editor.commit();
	}
	
	/**
	 * Metodo que elimina una preferencia
	 * @param preference
	 * @param context
	 */
	public static void deletePreferences(PreferencesTypes preference, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(preference.toString(), null);
		editor.commit();
	}
	
	/**
	 * Metodo que obtiene una preferencia almacenada
	 * @param preference
	 * @param param
	 * @param context
	 * @return
	 */
	public static <T> T getPreferences(PreferencesTypes preference, Class<T> param, Context context) {
		if (context != null) {
			SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
			String json = sharedPref.getString(preference.toString(), null);
			if (json == null) {
				return null;
			}
			Gson gson = new Gson();
			return gson.fromJson(json, param);			
		} else {
			return null;
		}
	}
	
	/**
	 * Metodo que inicializa las configuraciones
	 * @param context
	 */
	public static void initDefaultPreferences(Context context) {
		Boolean isSoundActive = getPreferences(PreferencesTypes.IS_SOUND_ACTIVE, Boolean.class, context);
		if (isSoundActive == null) {
			savePreferences(PreferencesTypes.IS_SOUND_ACTIVE, DEFAULT_SOUND_ACTIVE, context);
		}
		Boolean isVibrationActive = getPreferences(PreferencesTypes.IS_VIBRATION_ACTIVE, Boolean.class, context);
		if (isVibrationActive == null) {
			savePreferences(PreferencesTypes.IS_VIBRATION_ACTIVE, DEFAULT_VIBRATION_ACTIVE, context);
		}
		Integer checkPendingExerciseHours = getPreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_HOURS, Integer.class, context);
		if (checkPendingExerciseHours == null) {
			savePreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_HOURS, DEFAULT_NOTIFICATION_CHECK_PENDING_EXERCISE_HOURS, context);
		}
		Integer checkPendingExerciseCloseMinutes = getPreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_CLOSE_MINUTES, Integer.class, context);
		if (checkPendingExerciseCloseMinutes == null) {
			savePreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_CLOSE_MINUTES, DEFAULT_NOTIFICATION_CHECK_PENDING_EXERCISE_CLOSE_MINUTES, context);
		}
		Integer sendTimeNotificationMinutes = getPreferences(PreferencesTypes.NOTIFICATION_SEND_TIME_MINUTES, Integer.class, context);
		if (sendTimeNotificationMinutes == null) {
			savePreferences(PreferencesTypes.NOTIFICATION_SEND_TIME_MINUTES, DEFAULT_NOTIFICATION_SEND_TIME_MINUTES, context);
		}
		Integer updateFreqMeters = getPreferences(PreferencesTypes.MAP_UPDATE_FREQ_METERS, Integer.class, context);
		if (updateFreqMeters == null) {
			savePreferences(PreferencesTypes.MAP_UPDATE_FREQ_METERS, DEFAULT_MAP_UPDATE_FREQ_METERS, context);
		}
		Integer updateFreqSeconds = getPreferences(PreferencesTypes.MAP_UPDATE_FREQ_SEC,Integer.class, context);
		if (updateFreqSeconds == null) {
			savePreferences(PreferencesTypes.MAP_UPDATE_FREQ_SEC, DEFAULT_MAP_UPDATE_FREQ_SEC, context);
		}
		Integer mapZoom = getPreferences(PreferencesTypes.MAP_ZOOM, Integer.class, context);
		if (mapZoom == null) {
			savePreferences(PreferencesTypes.MAP_ZOOM, DEFAULT_MAP_ZOOM, context);
		}
		Integer savePointsPercent = getPreferences(PreferencesTypes.MAP_SAVE_POINTS, Integer.class, context);
		if (savePointsPercent == null) {
			savePreferences(PreferencesTypes.MAP_SAVE_POINTS, DEFAULT_MAP_SAVE_POINTS, context);
		}
		Short mapTypeId = getPreferences(PreferencesTypes.MAP_TYPE, Short.class, context);
		if (mapTypeId == null) {
			savePreferences(PreferencesTypes.MAP_TYPE, DEFAULT_MAP_TYPE.getId(), context);
		}
		Integer clockCountdown = getPreferences(PreferencesTypes.CLOCK_COUNTDOWN, Integer.class, context);
		if (clockCountdown == null) {
			savePreferences(PreferencesTypes.CLOCK_COUNTDOWN, DEFAULT_CLOCK_COUNTDOWN, context);
		}
		Boolean clockShowMilis = getPreferences(PreferencesTypes.CLOCK_SHOW_MILISECONDS, Boolean.class, context);
		if (clockShowMilis == null) {
			savePreferences(PreferencesTypes.CLOCK_SHOW_MILISECONDS, DEFAULT_CLOCK_SHOW_MILISECONDS, context);
		}
		String syncUrl = getPreferences(PreferencesTypes.SYNC_URL, String.class, context);
		if (syncUrl == null) {
			savePreferences(PreferencesTypes.SYNC_URL, DEFAULT_SYNC_URL, context);
		}
		Integer syncTimeout = getPreferences(PreferencesTypes.SYNC_TIMEOUT, Integer.class, context);
		if (syncTimeout == null) {
			savePreferences(PreferencesTypes.SYNC_TIMEOUT, DEFAULT_SYNC_TIMEOUT, context);
		}
		Boolean syncHttps = getPreferences(PreferencesTypes.SYNC_HTTPS, Boolean.class, context);
		if (syncHttps == null) {
			savePreferences(PreferencesTypes.SYNC_HTTPS, DEFAULT_SYNC_HTTPS, context);
		}
		Boolean syncCryptPass = getPreferences(PreferencesTypes.SYNC_CRYPT_PASS, Boolean.class, context);
		if (syncCryptPass == null) {
			savePreferences(PreferencesTypes.SYNC_CRYPT_PASS, DEFAULT_SYNC_CRYPT_PASS, context);
		}
		Boolean syncAuto = getPreferences(PreferencesTypes.SYNC_AUTO, Boolean.class, context);
		if (syncAuto == null) {
			savePreferences(PreferencesTypes.SYNC_AUTO, DEFAULT_SYNC_AUTO, context);
		}
		Integer googleFitTimeout = getPreferences(PreferencesTypes.GOOGLE_FIT_TIMEOUT, Integer.class, context);
		if (googleFitTimeout == null) {
			savePreferences(PreferencesTypes.GOOGLE_FIT_TIMEOUT, DEFAULT_GOOGLE_FIT_TIMEOUT, context);
		}
		Boolean googleFitSync = getPreferences(PreferencesTypes.GOOGLE_FIT_SYNC, Boolean.class, context);
		if (googleFitSync == null) {
			savePreferences(PreferencesTypes.GOOGLE_FIT_SYNC, DEFAULT_GOOGLE_FIT_SYNC, context);
		}
	}
}
