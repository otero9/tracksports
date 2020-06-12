package com.udc.master.tfm.tracksports.fragments.home;

import java.util.Date;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExercise;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.common.chrono.ChronoStatus;
import com.udc.master.tfm.tracksports.common.chrono.Chronometer;
import com.udc.master.tfm.tracksports.common.dialog.CustomDialogBuilder;
import com.udc.master.tfm.tracksports.common.speak.Speaker;
import com.udc.master.tfm.tracksports.common.spinner.SpinnerType;
import com.udc.master.tfm.tracksports.directcom.fitness.AsyncGoogleFitExercise;
import com.udc.master.tfm.tracksports.directcom.rest.async.SaveExerciseWSAsync;
import com.udc.master.tfm.tracksports.locationtracker.LocationTracker;
import com.udc.master.tfm.tracksports.utils.AlarmUtils;
import com.udc.master.tfm.tracksports.utils.FormatUtils;
import com.udc.master.tfm.tracksports.utils.TimeUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Fragmento inicial de la aplicacion con la informacion
 * para iniciar una nueva actividad
 * @author a.oteroc
 *
 */
public class HomeFragment extends UpdatableRunningFragment {
	/** Boton para iniciar/resumir/pausar la actividad */
	private ImageButton startButton;
	/** Boton para detener la actividad */
	private ImageButton stopButton;
	/** Spinner 1 con la informacion a mostrar del recorrido */
	private Spinner spinner1;
	/** Spinner 2 con la informacion a mostrar del recorrido */
	private Spinner spinner2;
	/** Spinner 2 con la informacion a mostrar del recorrido */
	private Spinner spinner3;
	/** Cronometro que mide el tiempo de la actividad */
	private Chronometer chrono;
	/** Cronometro que indica el tiempo restante para finalizar la actividad */
	private Chronometer chronoRemaining;
	/** Estado del cronometro */
	private ChronoStatus chronoStatus = ChronoStatus.STOPPED;
	/** Lock de CPU mientras se ejecuta la tarea */
    private WakeLock wakeLock;
    /** Clase encargada de pronunciar los sonidos durante el ejercicio */
    private Speaker speaker;
    
	/**
	 * Constructor vacio
	 */
	public HomeFragment() {}

	/**
	 * Constructor del fragmento
	 * @param locationTracker
	 * @param autoStart
	 */
	public HomeFragment(LocationTracker locationTracker, Boolean autoStart) {
		setLocationTracker(locationTracker);
		setAutoStart(autoStart);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		speaker = new Speaker(getActivity());
		initUpdatableRunning();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View homeView = inflater.inflate(R.layout.fragment_home, container,	false);
		final Resources resources = getActivity().getResources();
		
		//Se inicializa el boton de start si no se ha inicializado
		initStartButton(homeView, resources);
		
		//Se inicializa el boton de stop si no se ha inicializado
		initStopButton(homeView, resources);
		
		//Se inicializan los spinners si no se han inicializado
		spinner1 = (Spinner) homeView.findViewById(R.id.spinner_home_1);
		spinner1.setAdapter(getAdapter());

		spinner2 = (Spinner) homeView.findViewById(R.id.spinner_home_2);
		spinner2.setAdapter(getAdapter());
		
		spinner3 = (Spinner) homeView.findViewById(R.id.spinner_home_3);
		spinner3.setAdapter(getAdapter());
		
		//Se incializa el chronometro para poder manejarlo si no se ha inicializado
		chrono = (Chronometer) getSpinnerItems().get(SpinnerType.CHRONO).getTextView();

		//Se incializa el chronometro para poder manejarlo si no se ha inicializado
		chronoRemaining = (Chronometer) getSpinnerItems().get(SpinnerType.CHRONO_TIME_REMAINING).getTextView();
		
		//Se actualiza el estado de los botones y spinners
		checkChronoStatus(resources);
		
		//Si se informa la variable se inicia el ejercicio de forma automatica
		if (isAutoStart()) {
			//Se elimina el ejericico pendiente y se inicia el ejercicio
			deletePendingExercise();
			start(getActivity().getResources());
		}
		
		return homeView;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		final Resources resources = getActivity().getResources();
		stop(resources, false);
		//Se finaliza el speaker
		if (speaker != null) {
			speaker.finish();
		}
	}
	
	/**
	 * Metodo que inicializa el boton de start
	 * @param homeView
	 * @param resources
	 */
	private void initStartButton(View homeView, final Resources resources) {
		startButton = (ImageButton) homeView.findViewById(R.id.button_start_chronometer);
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Si el cronometro esta parado se inicia y se inicia el tracker para obtener la posicion
				if (ChronoStatus.STOPPED.equals(chronoStatus)) {
					//Si no hay ningun usuario configurado en la aplicacion se muestra una alerta
					if (PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, getActivity()) == null) {
						CustomDialogBuilder alertDialog = new CustomDialogBuilder(getActivity());
				        alertDialog.setTitle(R.string.default_profile_not_exists_alert);
				        alertDialog.setMessage(R.string.default_profile_not_exists_alert_msg);
				        //Si se presiona el boton de aceptar
				        alertDialog.setPositiveButton(R.string.default_profile_not_exists_alert_ok_button, new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog,int which) {
				            	start(resources);
				            }
				        });
				        //Si se presiona el boton de cancelar
				        alertDialog.setNegativeButton(R.string.default_profile_not_exists_alert_cancel_button, new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				                dialog.cancel();
				            }
				        });
				        //Se muestra el dialogo
				        alertDialog.show();
					} else {
						start(resources);

					}
				//Si el cronometro esta pausado se resume la actividad
				} else if (ChronoStatus.PAUSED.equals(chronoStatus)) {
					resume(resources);
				//Si el cronometro esta corriendo se pausa la actividad
				} else if (ChronoStatus.RUNNING.equals(chronoStatus)) {
					pause(resources);
				}
			}
		});
	}
	
	/**
	 * Metodo que inicializa el boton de stop
	 * @param homeView
	 * @param resources
	 */
	private void initStopButton(View homeView, final Resources resources) {
		stopButton = (ImageButton) homeView.findViewById(R.id.button_stop_chronometer);
		//Solo se muestra el boton de stop cuando se esta ejecutando
		stopButton.setVisibility(View.INVISIBLE);
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se muestra una advertencia en primera instancia de que se va a detener la actividad
				CustomDialogBuilder alertDialog = new CustomDialogBuilder(getActivity());
		        alertDialog.setTitle(R.string.finish_tracking_alert);
		        alertDialog.setMessage(R.string.finish_tracking_alert_msg);
		        //Si se presiona el boton de aceptar
		        alertDialog.setPositiveButton(R.string.finish_tracking_alert_ok_button, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            	stop(resources, true);
		            }
		        });
		        //Si se presiona el boton de cancelar
		        alertDialog.setNegativeButton(R.string.finish_tracking_alert_cancel_button, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.cancel();
		            }
		        });
		        //Se muestra el dialogo
		        alertDialog.show();
			}
		});
	}
	
	/**
	 * Metodo que cambia el estado de los spinners para poder editarlos o no
	 * @param enable
	 */
	private void changeSpinnerStatus(boolean enable) {
		spinner1.setEnabled(enable);
		spinner2.setEnabled(enable);
		spinner3.setEnabled(enable);
	}
	
	/**
	 * Metodo que comprueba el estado del cronometro para actualizar el estado
	 * de los botones y de los spinners
	 * @param resources
	 */
	private void checkChronoStatus(final Resources resources) {
		if (ChronoStatus.STOPPED.equals(chronoStatus)) {
			startButton.setBackgroundDrawable(resources.getDrawable(R.drawable.play));
			stopButton.setVisibility(View.INVISIBLE);
			changeSpinnerStatus(true);
			if (isAutoStart()) {
				if (isUseTimeRemaining()) {
					spinner1.setSelection(1);
				} else {
					spinner1.setSelection(0);
				}
				if (isUseDistanceRemaining()) {
					spinner2.setSelection(3);
				} else {
					spinner2.setSelection(2);
				}
				spinner3.setSelection(4);
			} else {
				spinner1.setSelection(0);
				spinner2.setSelection(2);
				spinner3.setSelection(4);
			}
		} else if (ChronoStatus.RUNNING.equals(chronoStatus)) {
			startButton.setBackgroundDrawable(resources.getDrawable(R.drawable.pause));
			stopButton.setVisibility(View.VISIBLE);
			changeSpinnerStatus(false);
		} else if (ChronoStatus.PAUSED.equals(chronoStatus)) {
			startButton.setBackgroundDrawable(resources.getDrawable(R.drawable.play));
			stopButton.setVisibility(View.VISIBLE);
			changeSpinnerStatus(true);
		}
	}
	
	/**
	 * Metodo que inicia el ejercicio
	 * @param resources
	 */
	private void start(Resources resources) {
		//Se crea un Lock para mantener la ejecucion del ejercicio
		//PowerManager powerManager = (PowerManager) getActivity().getSystemService(Application.POWER_SERVICE);
		//wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeLock");
		//if (!wakeLock.isHeld()) {
		//	wakeLock.acquire();
		//}
		PowerManager powerManager = (PowerManager) getActivity().getSystemService(Application.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"MyApp::MyWakelockTag");
		wakeLock.acquire();
		//Se inicia el cronometro
		chrono.start();
		if (isAutoStart() && isUseTimeRemaining()) {
			chronoRemaining.start();
		}

		//Si se especifica el autostart no se instancia el countdown
		if (isAutoStart()) {
	    	//Se inicializan los servicios
	    	initExerciseServices();
		} else {
			//Se crea un countdown para iniciar el ejercicio
			initCountDown();
		}
		
		//Se muestra el boton de STOP
		stopButton.setVisibility(View.VISIBLE);
		//Call setBackground(...) requires API level 16 (current min is 8) ...
		//Se cambia la imagen del boton a PAUSE
		startButton.setBackgroundDrawable(resources.getDrawable(R.drawable.pause));
		//Se actualiza el estado del cronometro
		chronoStatus = ChronoStatus.RUNNING;
		//Se desabilita el uso de los spinners
		changeSpinnerStatus(false);
		//Se genera una nueva actividad
		initExercise();
		//Se setea la fecha de inicio
		Date startTime = new Date(chrono.getStartTime());
		getExercise().setStartTime(startTime);
		//Se setea el perfil que realiza el ejercicio (en caso de estar definido)
		Profile profile = PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, getActivity());
		getExercise().setProfile(profile);
	}
	
	/**
	 * Metodo que reanuda el ejercicio
	 * @param resources
	 */
	private void resume(Resources resources) {
		chrono.resume();
		if (isAutoStart() && isUseTimeRemaining()) {
			chronoRemaining.resume();
		}
		if (getLocationTracker() != null) {
			getLocationTracker().start(getLocationListener());
		}
		//Call setBackground(...) requires API level 16 (current min is 8) ...
		//Se cambia la imagen del boton a PAUSE
		startButton.setBackgroundDrawable(resources.getDrawable(R.drawable.pause));
		//Se actualiza el estado del cronometro
		chronoStatus = ChronoStatus.RUNNING;
		//Se inicia el listener para medir los pasos
		registerDetector();
		//Se desabilita el uso de los spinners
		changeSpinnerStatus(false);
	}
	
	/**
	 * Metodo que pausa el ejercicio
	 * @param resources
	 */
	private void pause(Resources resources) {
		chrono.pause();
		if (isAutoStart() && isUseTimeRemaining()) {
			chronoRemaining.pause();
		}
		if (getLocationTracker() != null) {
			getLocationTracker().stop();
		}
		//Call setBackground(...) requires API level 16 (current min is 8) ...
		//Se cambia la imagen del boton a PLAY
		startButton.setBackgroundDrawable(resources.getDrawable(R.drawable.play));
		//Se actualiza el estado del cronometro
		chronoStatus = ChronoStatus.PAUSED;
		//Se detiene el listener para medir los pasos
		unRegisterDetector();
		//Se habilita el uso de los spinners
		changeSpinnerStatus(true);
	}
	
	/**
	 * Metodo que detiene el ejercicio
	 * @param resources
	 * @param saveExercise
	 */
	private void stop(Resources resources, boolean saveExercise) {
		//Se libera el bloqueo
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
		}
		//Se detiene el cronometro y el tracker
		chrono.stop();
		if (isAutoStart() && isUseTimeRemaining()) {
			chronoRemaining.stop();
		}
		if (getLocationTracker() != null) {
			getLocationTracker().stop();
		}
		if (getGoogleMap() != null) {
			getGoogleMap().clear();
		}
		setAutoStart(false);
		//Call setBackground(...) requires API level 16 (current min is 8) ...
		//Se cambia la imagen del boton a PLAY
		startButton.setBackgroundDrawable(resources.getDrawable(R.drawable.play));
		//Se oculta el boton de STOP
		stopButton.setVisibility(View.INVISIBLE);
		//Se actualiza el estado del cronometro
		chronoStatus = ChronoStatus.STOPPED;
		//Se reinician los valores
		resetValues();
		//Se detiene el listener para medir los pasos
		unRegisterDetector();
		//Se habilita el uso de los spinners
		changeSpinnerStatus(true);
		//Se inserta el ejercicio de forma asincrona
		if (saveExercise) {
			AsyncSaveExercise async = new AsyncSaveExercise();
			async.execute();
		}
		setHasPreviousExercise(true);
	}
	
	/**
	 * Metodo que inicia la cuenta atras
	 */
	private void initCountDown() {
		final long delayTime = PreferencesUtils.getPreferences(
				PreferencesTypes.CLOCK_COUNTDOWN, Integer.class, getActivity()).longValue();
		
		final CustomDialogBuilder alertDialogBuilder = new CustomDialogBuilder(getActivity());
		alertDialogBuilder.setTitle(getString(R.string.dialog_countdown_start_exercise));
		alertDialogBuilder.setMessage(FormatUtils.formatTime(delayTime, PreferencesUtils.getPreferences(
				PreferencesTypes.CLOCK_SHOW_MILISECONDS, Boolean.class, getActivity())));
		final AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setCancelable(false);
		alertDialog.show();
		CustomDialogBuilder.setDividerColor(getActivity(), alertDialog);
		
		CountDownTimer countDownTimer = new CountDownTimer(delayTime*1000, 
				PreferencesUtils.getPreferences(
						PreferencesTypes.CLOCK_SHOW_MILISECONDS, Boolean.class, getActivity()) 
						? TimeUtils.DEFAULT_INTERVAL_TIME_WITH_MILISECONDS : TimeUtils.DEFAULT_INTERVAL_TIME) {
			long previous = delayTime;
			@Override
			public void onTick(long millisUntilFinished) {
				long now = millisUntilFinished/1000;
				if (now < previous) {
					if (speaker != null) {
						speaker.speak(String.valueOf(now));
					}
				}
				alertDialog.setMessage(FormatUtils.formatTime(millisUntilFinished, PreferencesUtils.getPreferences(
						PreferencesTypes.CLOCK_SHOW_MILISECONDS, Boolean.class, getActivity())));
				previous = now;
		    }
		    @Override
		    public void onFinish() {
		    	//se cierra el dialogo
		    	alertDialog.cancel();
		    	//Se inicializan los servicios
		    	initExerciseServices();
		    }
		};
		countDownTimer.start();	
	}
	
	/**
	 * Metodo que inicializa los servicios para iniciar el ejercicio
	 */
	private void initExerciseServices() {
    	//Se inicia el servicio de localizacion
		if (getLocationTracker() != null) {
			getLocationTracker().start(getLocationListener());
		}
		//Se inicia el listener para medir los pasos
		registerDetector();
		//Se pronuncia el texto para empezar el ejercicio
		speaker.speak(getString(R.string.speaker_start_exercise));
	}
	
	/**
	 * Clase asincrona que se encarga de almacenar el ejercicio
	 * @author a.oteroc
	 *
	 */
	private class AsyncSaveExercise extends AsyncTask<String, Void, Boolean> {
		/** Dialogo de carga al guardar el ejercicio */
		private ProgressDialog progress;

	    @Override
	    protected void onPreExecute() {
			progress = new ProgressDialog(getActivity());
//			progress.setTitle(getString(R.string.progress_dialog_save_exercise_title));
			progress.setMessage(getString(R.string.progress_dialog_save_exercise_content));
			progress.setCancelable(false);
			progress.show();
	    }

	    @Override
	    protected Boolean doInBackground(final String... args) {
			getExercise().setDuration(chrono.getTotalTime());
			ExerciseDAO exerciseHelper = DatabaseFactory.getInstance().getExerciseDAO(getActivity());
			Integer percentPoints = PreferencesUtils.getPreferences(PreferencesTypes.MAP_SAVE_POINTS, Integer.class, getActivity());
			if (percentPoints == null) {
				percentPoints = 100;
			}
			getExercise().setId((long)exerciseHelper.insert(getExercise(), percentPoints));
	    	return true;
	    }

	    @Override
	    protected void onPostExecute(final Boolean success) {
	        if (progress.isShowing()) {
	        	progress.dismiss();
	        }
	        
	        //Se sincroniza el ejercicio con el servicio Web al guardarlo siempre y cuando tenga usuario asociado
	        boolean syncAuto = Boolean.TRUE.equals(PreferencesUtils.getPreferences(PreferencesTypes.SYNC_AUTO, Boolean.class, getActivity()));
	        if (getExercise().getProfile() != null && syncAuto) {
				SaveExerciseWSAsync saveExerciseWSAsync = new SaveExerciseWSAsync(getActivity(), getExercise().getProfile(), getExercise());
				saveExerciseWSAsync.execute();
	        }
			
			//Se sincroniza el ejercicio con Google FIT
	        boolean syncGoogleFit = Boolean.TRUE.equals(PreferencesUtils.getPreferences(PreferencesTypes.GOOGLE_FIT_SYNC, Boolean.class, getActivity()));
			if (syncGoogleFit) {
		        AsyncGoogleFitExercise asyncGoogleFitExercise = new AsyncGoogleFitExercise(getActivity(), getExercise());
				asyncGoogleFitExercise.execute();	
			}
	    }
	}
	
	/**
	 * Metodo invocado cuando para eliminar un ejercicio pendiente
	 */
	private void deletePendingExercise() {
		//Se elimina el ejercicio como pendiente de las preferencias y de la BBDD
		PendingExercise pendingExercise = PreferencesUtils.getPreferences(PreferencesTypes.PENDING_EXERCISE, PendingExercise.class, getActivity());
		if (pendingExercise != null) {
			PreferencesUtils.deletePreferences(PreferencesTypes.PENDING_EXERCISE, getActivity());
			PreferencesUtils.deletePreferences(PreferencesTypes.IS_INTERVAL_EXECUTE_PENDING_EXERCISE, getActivity());
			PendingExerciseDAO pendingExerciseHelper = DatabaseFactory.getInstance().getPendingExerciseDAO(getActivity());
			pendingExerciseHelper.delete(pendingExercise);
			AlarmUtils.deletePendingExerciseAlarm(getActivity());
		}
	}
	
	/**
	 * Metodo invocado cuando se finaliza un ejercicio pendiente
	 */
	private void finishPendingExercise() {
		if (isAutoStart() && (ChronoStatus.RUNNING.equals(chronoStatus)
				|| ChronoStatus.PAUSED.equals(chronoStatus))) {
			//Cuando finaliza el tiempo se para la actividad y se almacena
			final Resources resources = getActivity().getResources();
			stop(resources, true);
		}
	}
	
	@Override
	public void onFinishTime() {
		finishPendingExercise();
	}

	@Override
	public void onCompleteDistance() {
		finishPendingExercise();
	}

	@Override
	public void onCompleteStepDistance() {
		finishPendingExercise();
	}
}
