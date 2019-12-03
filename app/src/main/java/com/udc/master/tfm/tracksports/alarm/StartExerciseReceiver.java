package com.udc.master.tfm.tracksports.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.udc.master.tfm.tracksports.MainActivity;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;

/**
 * Clase utilizada para empezar un ejercicio pendiente al seleccionar la opcion en la notificacion
 * @author a.oteroc
 *
 */
public class StartExerciseReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Integer notificationId = (Integer)intent.getExtras().get(ConstantsUtils.NOTIFICATION_ID_PARAM);
		Intent executeIntent = new Intent(context, MainActivity.class);
		executeIntent.setAction(MainActivity.class.getSimpleName());
		executeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		bundle.putBoolean(ConstantsUtils.AUTO_START_PARAM, true);
		bundle.putInt(ConstantsUtils.NOTIFICATION_ID_PARAM, notificationId);
		executeIntent.putExtras(bundle);
		context.startActivity(executeIntent);
	}

}
