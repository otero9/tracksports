package com.udc.master.tfm.tracksports.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.udc.master.tfm.tracksports.MainActivity;
import com.udc.master.tfm.tracksports.alarm.AlarmReceiver;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExercise;

/**
 * Clase de utilidad relacionada con las alarmas
 * @author a.oteroc
 *
 */
public class AlarmUtils {
	/** Id de notificacion para indicar que la aplicacion se esta ejecutando */
	public static final int ALARM_CHECK_PENDING_EXERCISES_ID = 1;
	/** Id de notificacion de ejercicio pendiente de ejecutar */
	public static final int ALARM_EXECUTE_PENDING_EXERCISE_ID = 2;
	
	private AlarmUtils() {}
	
	/**
	 * Metodo que crea una alarma  para chequear si existen ejercicios pendientes para su ejecucion
	 * @param context
	 * @param interval
	 */
	public static void createCheckPendingExercisesAlarm(Context context, long interval) {
		Intent alarmIntent = new Intent(context, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmUtils.ALARM_CHECK_PENDING_EXERCISES_ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, pendingIntent);
	}
	
	/**
	 * Metodo que crea una alarma a la hora especificada para ejecutar un ejercicio pendiente
	 * @param context
	 * @param pendingExercise
	 */
	@SuppressLint("NewApi")
	public static void createPendingExerciseAlarm(Context context, PendingExercise pendingExercise) {
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	    	alarmManager.setExact(AlarmManager.RTC_WAKEUP, pendingExercise.getStartTime().getTime(), getMainActiviyPendingIntent(context));
	    } else {
	    	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, pendingExercise.getStartTime().getTime(), AlarmManager.INTERVAL_DAY, getMainActiviyPendingIntent(context));
	    }
	}
	
	/**
	 * Metodo que elimina una alarma para la ejecucion de un ejercicio pendiente
	 * @param context
	 */
	public static void deletePendingExerciseAlarm(Context context) {
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(getMainActiviyPendingIntent(context));
	}
	
	/**
	 * Metodo que obtiene un pending intent del MainActivity
	 * @param context
	 * @return
	 */
	private static PendingIntent getMainActiviyPendingIntent(Context context) {
		Intent executeIntent = new Intent(context, MainActivity.class);
		executeIntent.setAction(MainActivity.class.getSimpleName());
		Bundle bundle = new Bundle();
		bundle.putBoolean(ConstantsUtils.AUTO_START_PARAM, true);
		bundle.putInt(ConstantsUtils.NOTIFICATION_ID_PARAM, NotificationsUtils.PENDING_EXERCISE_NOTIFICATION_ID);
		executeIntent.putExtras(bundle);
		return PendingIntent.getActivity(context, AlarmUtils.ALARM_EXECUTE_PENDING_EXERCISE_ID, executeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
