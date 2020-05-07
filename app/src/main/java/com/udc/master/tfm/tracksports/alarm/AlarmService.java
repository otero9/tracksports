package com.udc.master.tfm.tracksports.alarm;

import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.Html;
import com.udc.master.tfm.tracksports.MainActivity;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExercise;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.utils.AlarmUtils;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.NotificationsUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Servicio encargado de chequear si hay algun ejercicio pendiente de ejecucion
 * @author a.oteroc
 *
 */
@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
public class AlarmService extends Service {
    
	@Override
    public int onStartCommand(Intent startIntent, int flags, int startId) {
		
		//Se comprueba si existe algun ejercicio pendiente
		PendingExercise storePendingExercise = PreferencesUtils.getPreferences(PreferencesTypes.PENDING_EXERCISE, PendingExercise.class, getBaseContext());
		
		//Se obtienen los ejercicios pendientes de BBDD
		PendingExerciseDAO pendingExerciseHelper = DatabaseFactory.getInstance().getPendingExerciseDAO(getApplicationContext());
		PendingExercise pendingExercise = pendingExerciseHelper.findNextPendingExerciseByProfile(
				PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, getApplicationContext()));
		
		//Si existe algun ejercicio pendiente en la BBDD se comprueba si existe algun ejercicio almacenado
		//Si no existe ejercicio almacenado se marca el ejercicio como pendiente para ejecutar
		//Si existe ejercicio almacenado se comprueba si es el mismo que se recupera de BBDD
		//si lo es no se hace nada, sino lo es se elimina el que exista y se crea el nuevo
		//ya que la ejecucion del nuevo va a ser anterior
		if (pendingExercise != null) {
			boolean createAlarm = false;
			if (storePendingExercise == null) {
				createAlarm = true;
			} else {
				if (!pendingExercise.getId().equals(storePendingExercise.getId())) {
					AlarmUtils.deletePendingExerciseAlarm(getBaseContext());
					createAlarm = true;
				}
			}
			if (createAlarm) {
				AlarmUtils.createPendingExerciseAlarm(getBaseContext(), pendingExercise);
				PreferencesUtils.savePreferences(PreferencesTypes.PENDING_EXERCISE, pendingExercise, getBaseContext());
			}
		}
		
		//Si se ha encontrado algun ejercicio pendiente se lanza la notificacion
		//Solo se lanzara si se esta dentro del intervalo configurado
		if (pendingExercise != null) {
			Date now = Calendar.getInstance().getTime();
			Long remaining = (pendingExercise.getStartTime().getTime() - now.getTime()) / (1000 * 60);
			Integer sendTimeNotificationMinutes = PreferencesUtils.getPreferences(PreferencesTypes.NOTIFICATION_SEND_TIME_MINUTES, Integer.class, getBaseContext());
			if (remaining < sendTimeNotificationMinutes && remaining >= 0) {
				Boolean reduceInterval = PreferencesUtils.getPreferences(PreferencesTypes.IS_INTERVAL_EXECUTE_PENDING_EXERCISE, Boolean.class, getBaseContext());
				if (!Boolean.TRUE.equals(reduceInterval)) {
					//Se almacena en las preferencias para indica que es necesario ejecutar la alarma en menos intervalo
					PreferencesUtils.savePreferences(PreferencesTypes.IS_INTERVAL_EXECUTE_PENDING_EXERCISE, Boolean.TRUE, getBaseContext());
					//Se vuelve a crear la alarma de chequeo con el intervalo reducido
					Integer checkPendingExerciseCloseMinutes = PreferencesUtils.getPreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_CLOSE_MINUTES, Integer.class, getBaseContext());
					AlarmUtils.createCheckPendingExercisesAlarm(getBaseContext(), checkPendingExerciseCloseMinutes * 60 * 1000);
				}
				
				//Actividad que se ejecutara al selecionar la notificacion
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				//Actividad que se ejcutara al selecionar la opcion de descartar la notificacion
				Intent discardIntent = new Intent(getBaseContext(), DiscardExerciseReceiver.class);
				discardIntent.putExtra(ConstantsUtils.PENDING_EXERCISE_PARAM, pendingExercise);
				discardIntent.putExtra(ConstantsUtils.NOTIFICATION_ID_PARAM, NotificationsUtils.PENDING_EXERCISE_NOTIFICATION_ID);
				PendingIntent discardPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, discardIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				//Actividad que se ejecutara al selecionar la opcion de empezar el ejercicio
				Intent startItent = new Intent(getBaseContext(), StartExerciseReceiver.class);
				startItent.putExtra(ConstantsUtils.NOTIFICATION_ID_PARAM, NotificationsUtils.PENDING_EXERCISE_NOTIFICATION_ID);
				PendingIntent startPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, startItent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				//Se construye la notificacion
				Notification.Builder mBuilder = new Notification.Builder(getBaseContext())
					.setSmallIcon(R.drawable.running)
					.setContentTitle(getString(R.string.app_name))
					.setContentIntent(pendingIntent);
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					mBuilder.setStyle(new Notification.BigTextStyle().bigText(getNotificationText(pendingExercise, remaining)))
						.addAction(R.drawable.delete, getString(R.string.notification_discard_button), discardPendingIntent)
						.addAction(R.drawable.running, getString(R.string.notification_start_button), startPendingIntent);
				} else {
					mBuilder.setContentText(getNotificationText(pendingExercise, remaining));
				}
				Notification notification = mBuilder.getNotification();
				notification.defaults |= Notification.DEFAULT_LIGHTS;
				if (Boolean.TRUE.equals(PreferencesUtils.getPreferences(PreferencesTypes.IS_SOUND_ACTIVE, Boolean.class, getBaseContext()))) {
					notification.defaults |= Notification.DEFAULT_SOUND;
				}
				if (Boolean.TRUE.equals(PreferencesUtils.getPreferences(PreferencesTypes.IS_VIBRATION_ACTIVE, Boolean.class, getBaseContext()))) {
					notification.defaults |= Notification.DEFAULT_VIBRATE;
				}
				
				//Se envia la notificacion
				NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				notificationManager.notify(NotificationsUtils.PENDING_EXERCISE_NOTIFICATION_ID, notification);
			}
			
			//Si se ha pasado el tiempo de ejecucion, se descartar el ejercicio
			if (remaining < 0) {
				PreferencesUtils.deletePreferences(PreferencesTypes.PENDING_EXERCISE, getBaseContext());
			}
		}
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * Metodo que obtiene el texto para mostrar en la notificacion
	 * @param pendingExercise
	 * @param remaining
	 * @return
	 */
	private String getNotificationText(PendingExercise pendingExercise, Long remaining) {
		StringBuilder sb = new StringBuilder();
		
		if (remaining == 0) {
			sb.append(getString(R.string.notification_pending_exercise_last_minute));
		} else {
			sb.append(getString(R.string.notification_pending_exercise_remaining, remaining.toString()));
		}
		sb.append(Html.fromHtml("<br />"));
		
		boolean enter = false;
		if (pendingExercise.getDistance() != null && pendingExercise.getDistance() != 0) {
			sb.append(getString(R.string.exercise_detail_distance));
			sb.append(" ");
			sb.append(pendingExercise.getDistance());
			sb.append(" ");
			sb.append(getString(R.string.kilometers));
			enter = true;
		}
		if (pendingExercise.getDuration() != null && pendingExercise.getDuration() != 0) {
			if (enter) {
				sb.append(" ");
			}
			sb.append(getString(R.string.exercise_detail_duration));
			sb.append(" ");
			sb.append(pendingExercise.getDuration());
			sb.append(" ");
			sb.append(getString(R.string.minutes));
			enter = true;
		}
		if (pendingExercise.getComments() != null) {
			if (enter) {
				sb.append(Html.fromHtml("<br />"));
			}
			sb.append(pendingExercise.getComments());
		}
		return sb.toString();
	}
}
