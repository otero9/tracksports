package com.udc.master.tfm.tracksports.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExercise;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExerciseDAO;
import com.udc.master.tfm.tracksports.utils.AlarmUtils;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Clase utilizada para descartar un ejercicio pendiente al seleccionar la opcion en la notificacion
 * @author a.oteroc
 *
 */
public class DiscardExerciseReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//Se elimina la alarma pendiente
		AlarmUtils.deletePendingExerciseAlarm(context);
		
		//Se elimina el ejercicio pendiente
		PendingExercise pendingExercise = (PendingExercise)intent.getExtras().get(ConstantsUtils.PENDING_EXERCISE_PARAM);
		PendingExerciseDAO pendingExerciseHelper = DatabaseFactory.getInstance().getPendingExerciseDAO(context);
		pendingExerciseHelper.delete(pendingExercise);
		
		//Se elimina la notificacion
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Integer notificationId = (Integer)intent.getExtras().get(ConstantsUtils.NOTIFICATION_ID_PARAM);
		notificationManager.cancel(notificationId);
		
		//Se elimina el ejercicio pendiente de las preferencias
		PreferencesUtils.deletePreferences(PreferencesTypes.PENDING_EXERCISE, context);
		PreferencesUtils.deletePreferences(PreferencesTypes.IS_INTERVAL_EXECUTE_PENDING_EXERCISE, context);
	}
}
