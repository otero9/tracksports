package com.udc.master.tfm.tracksports.common.speak;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import com.udc.master.tfm.tracksports.utils.DateUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Speaker de la aplicacion
 * @author a.oteroc
 *
 */
public class Speaker implements OnInitListener {
	/** Clase encargada de pronunciar los textos especificados */
	private TextToSpeech tts;
	/** Indica si el speaker esta listo para utilizarse */
	private boolean ready;
	/** Indica se se permite utilizar el speaker */
	private boolean allowSpeak;
	/** Contexto de la aplicacion */
	private Context context;
	
	/**
	 * Constructor de clase
	 * @param context
	 * @param allowSpeak
	 */
	public Speaker(Context context, boolean allowSpeak) {
		this.tts = new TextToSpeech(context, this);
		this.allowSpeak = allowSpeak;
		this.context = context;
	}
	
	/**
	 * Constructor de clase
	 * @param context
	 * @param allowSpeak
	 */
	public Speaker(Context context) {
		this.tts = new TextToSpeech(context, this);
		this.allowSpeak = Boolean.TRUE.equals(PreferencesUtils.getPreferences(PreferencesTypes.IS_SOUND_ACTIVE, Boolean.class, context));
		this.context = context;
	}
	
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			tts.setLanguage(DateUtils.DEFAULT_LOCALE);
			ready = true;
		} else if (status == TextToSpeech.LANG_MISSING_DATA
					|| status == TextToSpeech.LANG_NOT_SUPPORTED) {
			//Idioma no soportado
		} else {
			//Error en la inicializacion
		}
	}

	/**
	 * Metodo que pronuncia un texto
	 * @param text
	 */
	@SuppressWarnings("deprecation")
	public void speak(String text) {
		allowSpeak = Boolean.TRUE.equals(PreferencesUtils.getPreferences(PreferencesTypes.IS_SOUND_ACTIVE, Boolean.class, context));
		if (allowSpeak && ready) {
			tts.speak(text, TextToSpeech.QUEUE_ADD, null);
		}
	}
	
	/**
	 * Metodo que pausa el speaker
	 * @param duration
	 */
	@SuppressWarnings("deprecation")
	public void pause(int duration) {
		tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
	}
	
	/**
	 * Metodo que finaliza el speaker
	 */
	public void finish() {
		tts.stop();
		tts.shutdown();
	}
}
