package com.udc.master.tfm.tracksports.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Clase encargada de invocar al servicio para comprobar los ejercicios pendientes de ejecutar
 * @author a.oteroc
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, AlarmService.class);
		context.startService(service);
	}

}
