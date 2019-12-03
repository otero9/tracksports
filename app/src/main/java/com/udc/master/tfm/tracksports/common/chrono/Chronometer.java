package com.udc.master.tfm.tracksports.common.chrono;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.udc.master.tfm.tracksports.common.spinner.SpinnerArrayAdapter;
import com.udc.master.tfm.tracksports.common.textView.DefaultTextInterface;
import com.udc.master.tfm.tracksports.time.FinishTimeListener;
import com.udc.master.tfm.tracksports.time.TimeListener;
import com.udc.master.tfm.tracksports.utils.DateUtils;
import com.udc.master.tfm.tracksports.utils.TimeUtils;

/**
 * Componente que representa un cronometro para medir el tiempo
 * @author a.oteroc
 *
 */
public class Chronometer extends TextView implements DefaultTextInterface {

	/** Tiempo por defecto para inicializar el cronometro */
	private static final String DEFAULT_TIME_FORMAT = "00:00:00";
	/** Tiempo por defecto para inicializar el cronometro con milisegundos */
	private static final String DEFAULT_TIME_FORMAT_WITH_MILISECONDS = "00:00:00.000";
	/** Variable que representa el delay con el que se creo el temporizador (en ms)*/
	private static final long DEFAULT_DELAY_TIME = 0;
	/** Contexto de la aplicacion */
	private Context context;
	/** Temporizador encargado de medir el tiempo */
	private Timer timer;
	/** Tarea que se ejecuta para medir el tiempo */
	private TimerTask timerTask;
	/** Tiempo (en ms) a la que se inicia el cronometro */
	private long startTime;
	/** Tiempo (en ms) transcurrido hasta pausar el cronometro*/
	private long elapsedHalfTime = 0;
	/** Tiempo (en ms) transcurrido desde el inicio (sin tener en cuenta pausas) */
	private long elapsedTime;
	/** Tiempo (en ms) transcurrido en total */
	private long totalTime;
	/** Adaptador ligado al cronometro para actualizar los cambios */
	private SpinnerArrayAdapter arrayAdapter;
	/** Listener que notifica el tiempo trascurrido */
	private List<TimeListener> timeListeners = new ArrayList<TimeListener>();
	/** Listener que notifica cuando finaliza el tiempo especificado */
	private List<FinishTimeListener> finishTimeListeners = new ArrayList<FinishTimeListener>();
	/** Tiempo de delay a la hora de iniciar el cronometro (en segundos) */
	private Long delayTime;
	/** Variable que indica si se mostraran los segundos en el cronometro */
	private boolean showMiliseconds;
	/** Indica si se muestra cuenta atras */
	private boolean showCountdown;
	/** Tiempo (en ms) especificado como maximo para recorrer */
	private long totalTimeSet;
	/** Tiempo (en ms) transcurrido frente al maximo especificado para recorrer */
	private long totalTimeSetElapsed;
	
	/**
	 * Constructor de la clase
	 * Este constructor invoca un cronometro cuenta adelante
	 * @param context
	 */
	public Chronometer(Context context) {
		super(context);
		this.context = context;
		initTimeText();
	}
	
	/**
	 * Constructor de la clase
	 * Este constructor invoca un cronometro cuenta adelante
	 * @param context
	 * @param delayTime
	 * @param showMiliseconds
	 */
	public Chronometer(Context context, long delayTime, boolean showMiliseconds) {
		super(context);
		this.context = context;
		this.delayTime = delayTime;
		this.showMiliseconds = showMiliseconds;
		initTimeText();
	}
	
	/**
	 * Constructor de la clase
	 * Este constructor invoca un cronometro con cuenta atras
	 * @param context
	 * @param delayTime
	 * @param showMiliseconds
	 * @param totalTime
	 */
	public Chronometer(Context context, long delayTime, boolean showMiliseconds, long totalTime) {
		super(context);
		this.context = context;
		this.delayTime = delayTime;
		this.showMiliseconds = showMiliseconds;
		this.totalTimeSet = totalTime;
		this.showCountdown = true;
		initTimeText();
	}
	
	/**
	 * Constructor de la clase
	 * Este constructor invoca un cronometro cuenta adelante
	 * @param context
	 * @param attributeSet
	 */
	public Chronometer(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;
		initTimeText();
	}
	
	/**
	 * Constructor de la clase
	 * Este constructor invoca un cronometro cuenta adelante
	 * @param context
	 * @param attributeSet
	 * @param defStyleAttr
	 */
	public Chronometer(Context context, AttributeSet attributeSet, int defStyleAttr) {
		super(context, attributeSet, defStyleAttr);
		this.context = context;
		initTimeText();
	}
	
	/**
	 * Metodo que anade un timeListener al cronometro
	 * @param tl
	 */
	public void addTimeListener(TimeListener tl) {
		timeListeners.add(tl);
	}
	
	/**
	 * Metodo que anade un finishTimeListener al cronometro
	 * @param tl
	 */
	public void addFinishTimeListener(FinishTimeListener ftl) {
		finishTimeListeners.add(ftl);
	}

	/**
	 * Metodo que inicializa el timer para medir el tiempo
	 * @param useDelay Indica si hay que tener en cuenta el delay configurado
	 */
	private void startTimer(boolean useDelay) {
		if (timer == null) {
			timer = new Timer();
		}
		
		//Solo se permite tener una tarea simultanea
		if (timerTask == null) {
			startTime = System.currentTimeMillis();
			timerTask = new TimerTask() {
				@Override
				public void run() {
					((Activity)context).runOnUiThread(new MyRunner());
				}
			};
			timer.scheduleAtFixedRate(timerTask, 
					useDelay ? getDelayTime() : 0, 
					showMiliseconds ? TimeUtils.DEFAULT_INTERVAL_TIME_WITH_MILISECONDS : TimeUtils.DEFAULT_INTERVAL_TIME);
		}
	}
	
	/**
	 * Metodo que devuelve el tiempo de delay en segundos en caso de especificar uno
	 * @return
	 */
	private long getDelayTime() {
		return (delayTime != null) ? delayTime * 1000: DEFAULT_DELAY_TIME;
	}
	
	/**
	 * Metodo que inicia el cronometro
	 */
	public void start() {
		startTimer(true);
		elapsedHalfTime = - getDelayTime();
	}
	
	/**
	 * Metodo que detiene el cronometro
	 */
	public void stop() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
		elapsedHalfTime = 0;
		initTimeText();
	}
	
	/**
	 * Metodo que pausa el cronometro
	 */
	public void pause() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
			elapsedHalfTime += elapsedTime;
		}
	}
	
	/**
	 * Metodo que reanuda el cronometro
	 */
	public void resume() {
		startTimer(false);
	}
	
	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @return the totalTime
	 */
	public long getTotalTime() {
		return totalTime;
	}

	/**
	 * Clase auxiliar encargada de actualizar el texto con el tiempo pasado
	 * @author a.oteroc
	 *
	 */
	private class MyRunner implements Runnable {
		@Override
		public void run() {
			//Solo se actualiza el tiempo cuando la tarea esta activa
			if (timerTask != null) {
				elapsedTime = System.currentTimeMillis() - startTime;
				totalTime = elapsedTime + elapsedHalfTime;
				if (showCountdown) {
					totalTimeSetElapsed = totalTimeSet - totalTime;
					if (totalTimeSetElapsed < 0) {
						totalTimeSetElapsed = 0;
					}
				}
				//se obtiene el tiempo desglosado
				long [] time = DateUtils.getTime(showCountdown ? totalTimeSetElapsed : totalTime);
				//Se actualiza el texto con el tiempo
				setTimeText(time[0], time[1], time[2], time[3]);
			}
			//Si se ha establecido un adaptador se actualizan sus cambios
			if (arrayAdapter != null) {
				arrayAdapter.notifyDataSetChanged();
			}
			
			//Si se ha establece algun listener se actualizan los cambios
			if (timeListeners != null && !timeListeners.isEmpty()) {
				for (TimeListener timeListener : timeListeners) {
					timeListener.onUpdateTime(totalTime);
				}
			}
			//Si el cronometro mide el tiempo transcurrido y se ha completado, se notifica a sus listeners
			if (showCountdown && totalTimeSetElapsed <= 0 && finishTimeListeners != null && !finishTimeListeners.isEmpty()) {
				for (FinishTimeListener finishTimeListener : finishTimeListeners) {
					finishTimeListener.onFinishTime();
				}
			}
		}
	}
	
	/**
	 * Metodo que inicializa el texto con el formato inicial
	 */
	private void initTimeText() {
		if (showCountdown) {
			long [] time = DateUtils.getTime(totalTimeSet);
			setTimeText(time[0], time[1], time[2], time[3]);
		} else {
			if (showMiliseconds) {
				super.setText(DEFAULT_TIME_FORMAT_WITH_MILISECONDS);
			} else {
				super.setText(DEFAULT_TIME_FORMAT);
			}
		}
	}
	
	/**
	 * Metodo que actualiza el texto con el tiempo a partir de horas/minutos/segundos/milisegundos
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @param miliSeconds
	 */
	private void setTimeText(long hours, long minutes, long seconds, long miliSeconds) {
		StringBuilder sb = new StringBuilder();
		
		if (hours < 10) {
			sb.append("0");
		}
		sb.append(hours);
		sb.append(":");
		
		if (minutes < 10) {
			sb.append("0");
		}
		sb.append(minutes);
		sb.append(":");
		
		if (seconds < 10) {
			sb.append("0");
		}
		sb.append(seconds);
		if (showMiliseconds) {
			sb.append(".");
			if (miliSeconds < 10) {
				sb.append("00");
			} else if (miliSeconds < 100) {
				sb.append("0");
			}
			sb.append(miliSeconds);
		}
		
		super.setText(sb.toString());
	}

	@Override
	public void setDefaultText() {
		showCountdown = false;
		totalTimeSet = 0;
		this.initTimeText();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getText().toString();
	}
}
