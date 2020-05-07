package com.udc.master.tfm.tracksports.common.touch;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Clase envoltorio encargada de obtener los eventos de pulsacion por el usuario
 * @author a.oteroc
 *
 */
public class TouchableWrapper extends FrameLayout {

	/** Variable que indica el tiempo de margen para actualizar el mapa */
	private static final long SCROLL_TIME = 200L;
	/** Indica el ultimo momento en el que el usuario interactuo */
	private long lastTouched = 0;
	/** Listener encargado de gestionar los eventos interaccion del usuario */
	private UserInteractionListener listener;
	
	/**
	 * Contructor de la clase
	 * @param context
	 * @param listener
	 */
	public TouchableWrapper(Context context, UserInteractionListener listener) {
		super(context);
		this.listener = listener;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		//Evento de pulsacion
		case MotionEvent.ACTION_DOWN:
			lastTouched = SystemClock.uptimeMillis();
			break;
		//Evento de finalizar la pulsacion
		case MotionEvent.ACTION_UP:
			final long now = SystemClock.uptimeMillis();
			if (now - lastTouched > SCROLL_TIME) {
				//Se actualiza el mapa
				listener.onUpdateAfterUserInteraction();
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
