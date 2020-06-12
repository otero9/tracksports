package com.udc.master.tfm.tracksports.fragments.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import androidx.fragment.app.Fragment;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExercise;
import com.udc.master.tfm.tracksports.common.chrono.Chronometer;
import com.udc.master.tfm.tracksports.common.spinner.SpinnerArrayAdapter;
import com.udc.master.tfm.tracksports.common.spinner.SpinnerItem;
import com.udc.master.tfm.tracksports.common.spinner.SpinnerType;
import com.udc.master.tfm.tracksports.common.textView.CustomTextView;
import com.udc.master.tfm.tracksports.common.textView.DefaultTextInterface;
import com.udc.master.tfm.tracksports.locationtracker.LocationTracker;
import com.udc.master.tfm.tracksports.locationtracker.LocationUpdateListener;
import com.udc.master.tfm.tracksports.map.GoogleMapFragment;
import com.udc.master.tfm.tracksports.map.listener.AltitudeListener;
import com.udc.master.tfm.tracksports.map.listener.CaloriesListener;
import com.udc.master.tfm.tracksports.map.listener.DistanceListener;
import com.udc.master.tfm.tracksports.map.listener.LineRouteListener;
import com.udc.master.tfm.tracksports.map.listener.SpeedListener;
import com.udc.master.tfm.tracksports.stepdetector.StepDetector;
import com.udc.master.tfm.tracksports.stepdetector.listener.StepCaloriesListener;
import com.udc.master.tfm.tracksports.stepdetector.listener.StepCountListener;
import com.udc.master.tfm.tracksports.stepdetector.listener.StepDistanceListener;
import com.udc.master.tfm.tracksports.stepdetector.listener.StepPaceListener;
import com.udc.master.tfm.tracksports.stepdetector.listener.StepSpeedListener;
import com.udc.master.tfm.tracksports.time.FinishTimeListener;
import com.udc.master.tfm.tracksports.time.TimeListener;
import com.udc.master.tfm.tracksports.utils.DateUtils;
import com.udc.master.tfm.tracksports.utils.FormatUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Clase abstracta que engloba los fragmentos en los que se actualizara la informacion del recorrido
 * @author a.oteroc
 *
 */
public abstract class UpdatableRunningFragment extends Fragment implements FinishTimeListener, DistanceListener.DistanceReaminingComplete, StepDistanceListener.DistanceReaminingComplete {

	/** Servicio para obtener la posicion actual */
	private static LocationTracker locationTracker;
	/** Listener encargado de actualizar la posicion actual */
	private static LocationUpdateListener locationListener;
	/** Adatador con la informacion de los spinners */
	private static SpinnerArrayAdapter adapter;
	/** Mapa con los componentes del spinner */
	private static Map<SpinnerType, SpinnerItem> spinnerItems;
	/** Lista con la informacion de la actividad realizada */
	private static Exercise exercise;
	/** Manejador de los sensores del dispositivo */
    private static SensorManager sensorManager;
    /** Clase encargada de detectar los pasos caminados*/
    private static StepDetector stepDetector;
    /** Listener que cuenta los pasos caminados*/
	private static StepCountListener stepCountListener;
	/** Listener que mide la distancia recorrida a traves de los pasos */
	private static StepDistanceListener stepDistanceListener;
	/** Listener que mide la velocidad a traves de los pasos */
	private static StepSpeedListener stepSpeedListener;
	/** Listener que mide las calorias consumidas*/
	private static StepCaloriesListener stepCaloriesListener;
	/** Listener que mide el ritmo medio de pasos*/
    private static StepPaceListener stepPaceListener;
    /** Listener que mide la distancia recorrida */
    private static DistanceListener distanceListener;
    /** Listener que mide la velocidad del usuario */
    private static SpeedListener speedListener;
    /** Listener que mide la altitud del recorrido */
    private static AltitudeListener altitudeListener;
    /** Listener que actualiza el recorrido realizado */
    private static LineRouteListener lineRouteListener;
    /** Listener que mide las calorias quemadas */
    private static CaloriesListener caloriesListener;
    /** Variable que indica si se inicia el ejercicio de manera automatica */
    private static boolean autoStart;
    /** Distancia total a recorrer */
    private static float distanceRemaining = 0;
    /** Tiempo total a ejecutar el ejercicio */
    private static long timeRemaining = 0;
    /** Variable que indica si se utiliza la variable de distancia restante */
    private static boolean useDistanceRemaining = false;
    /** Variable que indica si se utiliza la variable de tiempo restante */
    private static boolean useTimeRemaining = false;
    /** Indica si ya se ha realizado algun ejercicio previo en la aplicacion */
    private static boolean hasPreviousExercise = false;
    /** Mapa donde se muestra la informacion */
    private static GoogleMapFragment googleMap;
	/**
	 * @return the locationTracker
	 */
	public static LocationTracker getLocationTracker() {
		return locationTracker;
	}

	/**
	 * @param locationTracker the locationTracker to set
	 */
	public static void setLocationTracker(LocationTracker locationTracker) {
		UpdatableRunningFragment.locationTracker = locationTracker;
	}

	/**
	 * @return the locationListener
	 */
	public static LocationUpdateListener getLocationListener() {
		return locationListener;
	}

	/**
	 * @param locationListener the locationListener to set
	 */
	public static void setLocationListener(LocationUpdateListener locationListener) {
		UpdatableRunningFragment.locationListener = locationListener;
	}

	/**
	 * @return the adapter
	 */
	public static SpinnerArrayAdapter getAdapter() {
		return adapter;
	}
	
	/**
	 * @return the spinnerItems
	 */
	public static Map<SpinnerType, SpinnerItem> getSpinnerItems() {
		return spinnerItems;
	}

	/**
	 * @return the exercise
	 */
	public Exercise getExercise() {
		return exercise;
	}
	
	/**
	 * @return the distanceListener
	 */
	public DistanceListener getDistanceListener() {
		return distanceListener;
	}

	/**
	 * @return the speedListener
	 */
	public SpeedListener getSpeedListener() {
		return speedListener;
	}

	/**
	 * @return the altitudeListener
	 */
	public static AltitudeListener getAltitudeListener() {
		return altitudeListener;
	}

	/**
	 * @return the lineRouteListener
	 */
	public LineRouteListener getLineRouteListener() {
		return lineRouteListener;
	}

	/**
	 * @return the caloriesListener
	 */
	public static CaloriesListener getCaloriesListener() {
		return caloriesListener;
	}

	/**
	 * @return the autoStart
	 */
	public static boolean isAutoStart() {
		return autoStart;
	}

	/**
	 * @param autoStart the autoStart to set
	 */
	public static void setAutoStart(boolean autoStart) {
		UpdatableRunningFragment.autoStart = autoStart;
	}

	/**
	 * @return the useDistanceRemaining
	 */
	public static boolean isUseDistanceRemaining() {
		return useDistanceRemaining;
	}

	/**
	 * @return the useTimeRemaining
	 */
	public static boolean isUseTimeRemaining() {
		return useTimeRemaining;
	}

	/**
	 * @return the hasPreviousExercise
	 */
	public static boolean hasPreviousExercise() {
		return hasPreviousExercise;
	}

	/**
	 * @param hasPreviousExercise the hasPreviousExercise to set
	 */
	public static void setHasPreviousExercise(boolean hasPreviousExercise) {
		UpdatableRunningFragment.hasPreviousExercise = hasPreviousExercise;
	}

	/**
	 * @return the googleMap
	 */
	public GoogleMapFragment getGoogleMap() {
		return googleMap;
	}

	/**
	 * @param googleMap the googleMap to set
	 */
	public void setGoogleMap(GoogleMapFragment googleMap) {
		UpdatableRunningFragment.googleMap = googleMap;
	}

	/**
	 * Metodo que inicializa el ejercicio
	 */
	protected void initExercise() {
		exercise = new Exercise();
	}
	
	protected void initUpdatableRunning() {
		//Se genera una nueva actividad
		initExercise();
		//Metodo que inicializa el adaptador con los elementos del spinner
		initSpinnerAdapter();
		//Se inicializa el detector de pasos
		initStepDetector();
		//Se inicializan los listeners para la geolocalizacion
		initMapTracker();
	}

	/** 
	 * Metodo que inicializa el spinner con sus componentes 
	 */
	private void initSpinnerAdapter() {
		final Resources resources = getActivity().getResources();
		
		//Mapa con la lista de objetos del spinner para manejarlos a partir de su tipo
		spinnerItems = new HashMap<SpinnerType, SpinnerItem>();
		//Lista con los objetos del spinner para manejarlos desde el adaptador
		List<SpinnerItem> items = new ArrayList<SpinnerItem>();
		//Adaptador encargador de mostrar los elementos en el spinner
		adapter = new SpinnerArrayAdapter(getActivity(), items);
		
		//Cronometro
		SpinnerItem item1 = new SpinnerItem();
		item1.setTitle(resources.getString(SpinnerType.CHRONO.getTitleId()));
		item1.setImage(resources.getDrawable(SpinnerType.CHRONO.getImageId()));
		Chronometer chrono = new Chronometer(getActivity(), autoStart ? 0 : PreferencesUtils.getPreferences(
				PreferencesTypes.CLOCK_COUNTDOWN, Integer.class, getActivity()).longValue(), PreferencesUtils.getPreferences(
						PreferencesTypes.CLOCK_SHOW_MILISECONDS, Boolean.class, getActivity()));
		item1.setTextView(chrono);
		
		//Distancia recorrida
		SpinnerItem item2 = new SpinnerItem();
		item2.setTitle(resources.getString(SpinnerType.DISTANCE.getTitleId()));
		item2.setImage(resources.getDrawable(SpinnerType.DISTANCE.getImageId()));
		final CustomTextView distanceTextView = new CustomTextView(getActivity(), "0 " + getString(R.string.meters)); 
		item2.setTextView(distanceTextView);
		
		//Velocidad actual
		SpinnerItem item3 = new SpinnerItem();
		item3.setTitle(resources.getString(SpinnerType.SPEED.getTitleId()));
		item3.setImage(resources.getDrawable(SpinnerType.SPEED.getImageId()));
		final CustomTextView speedTextView = new CustomTextView(getActivity(), "0", getString(R.string.speed_measure));
		item3.setTextView(speedTextView);
		
		//Velocidad media
		SpinnerItem item4 = new SpinnerItem();
		item4.setTitle(resources.getString(SpinnerType.SPEED_AVG.getTitleId()));
		item4.setImage(resources.getDrawable(SpinnerType.SPEED_AVG.getImageId()));
		final CustomTextView speedAvgTextView = new CustomTextView(getActivity(), "0", getString(R.string.speed_measure));
		item4.setTextView(speedAvgTextView);

		//Velocidad maxima
		SpinnerItem item5 = new SpinnerItem();
		item5.setTitle(resources.getString(SpinnerType.SPEED_MAX.getTitleId()));
		item5.setImage(resources.getDrawable(SpinnerType.SPEED_MAX.getImageId()));
		final CustomTextView speedMaxTextView = new CustomTextView(getActivity(), "0", getString(R.string.speed_measure));
		item5.setTextView(speedMaxTextView);
		
		//Pasos caminados
		SpinnerItem item6 = new SpinnerItem();
		item6.setTitle(resources.getString(SpinnerType.STEPS.getTitleId()));
		item6.setImage(resources.getDrawable(SpinnerType.STEPS.getImageId()));
		final CustomTextView stepsTextView = new CustomTextView(getActivity(), "0");
		item6.setTextView(stepsTextView);
		
		//Calorias quemadas
		SpinnerItem item7 = new SpinnerItem();
		item7.setTitle(resources.getString(SpinnerType.CALORIES_BURNED.getTitleId()));
		item7.setImage(resources.getDrawable(SpinnerType.CALORIES_BURNED.getImageId()));
		final CustomTextView caloriesBurnedTextView = new CustomTextView(getActivity(), "0", getString(R.string.calories_measure)); 
		item7.setTextView(caloriesBurnedTextView);
		
		//Ritmo de pasos por minuto
		SpinnerItem item8 = new SpinnerItem();
		item8.setTitle(resources.getString(SpinnerType.STEP_PACE.getTitleId()));
		item8.setImage(resources.getDrawable(SpinnerType.STEP_PACE.getImageId()));
		final CustomTextView stepPaceTextView = new CustomTextView(getActivity(), "0", getString(R.string.step_pace_measure));
		item8.setTextView(stepPaceTextView);
		
		//Altitud actual
		SpinnerItem item9 = new SpinnerItem();
		item9.setTitle(resources.getString(SpinnerType.ALTITUDE.getTitleId()));
		item9.setImage(resources.getDrawable(SpinnerType.ALTITUDE.getImageId()));
		final CustomTextView altitudeTextView = new CustomTextView(getActivity(), "0", getString(R.string.altitude_measure));
		item9.setTextView(altitudeTextView);

		//Altitud minima
		SpinnerItem item10 = new SpinnerItem();
		item10.setTitle(resources.getString(SpinnerType.ALTITUDE_MIN.getTitleId()));
		item10.setImage(resources.getDrawable(SpinnerType.ALTITUDE_MIN.getImageId()));
		final CustomTextView altitudeMinTextView = new CustomTextView(getActivity(), "0", getString(R.string.altitude_measure));
		item10.setTextView(altitudeMinTextView);
		
		//Altitud maxima
		SpinnerItem item11 = new SpinnerItem();
		item11.setTitle(resources.getString(SpinnerType.ALTITUDE_MAX.getTitleId()));
		item11.setImage(resources.getDrawable(SpinnerType.ALTITUDE_MAX.getImageId()));
		final CustomTextView altitudeMaxTextView = new CustomTextView(getActivity(), "0", getString(R.string.altitude_measure));
		item11.setTextView(altitudeMaxTextView);
		
		//Ritmo actual
		SpinnerItem item12 = new SpinnerItem();
		item12.setTitle(resources.getString(SpinnerType.SPEED_PACE.getTitleId()));
		item12.setImage(resources.getDrawable(SpinnerType.SPEED.getImageId()));
		final CustomTextView speedPaceTextView = new CustomTextView(getActivity(), "0", getString(R.string.speed_pace_measure));
		item12.setTextView(speedPaceTextView);

		//Calorias quemadas por minuto
		SpinnerItem item13 = new SpinnerItem();
		item13.setTitle(resources.getString(SpinnerType.CALORIES_BURNED_PACE.getTitleId()));
		item13.setImage(resources.getDrawable(SpinnerType.CALORIES_BURNED_PACE.getImageId()));
		final CustomTextView caloriesBurnedPaceTextView = new CustomTextView(getActivity(), "0", getString(R.string.calories_measure));
		item13.setTextView(caloriesBurnedPaceTextView);
		
		//En caso de que se ejecute un ejercicio pendiente se calcula el tiempo y la distancia restante
		String distanceRemainingString = "0";
		if (isAutoStart()) {
			PendingExercise pendingExercise = PreferencesUtils.getPreferences(PreferencesTypes.PENDING_EXERCISE, PendingExercise.class, getActivity());
			if (pendingExercise != null) {
				if (pendingExercise.getDistance() != null && pendingExercise.getDistance() != 0) {
					distanceRemaining = pendingExercise.getDistance() * 1000; //en metros
					distanceRemainingString = String.format(DateUtils.DEFAULT_LOCALE, "%.0f", distanceRemaining/1000);
				}
				if (pendingExercise.getDuration() != null && pendingExercise.getDuration() != 0) {
					timeRemaining = pendingExercise.getDuration() * 60 * 1000;
				}
			} else {
				distanceRemaining = 0;
				timeRemaining = 0;
			}
		} else {
			distanceRemaining = 0;
			timeRemaining = 0;
		}
		if (distanceRemaining > 0) {
			useDistanceRemaining = true;
		}
		if (timeRemaining > 0) {
			useTimeRemaining = true;
		}
		
		//Distancia recorrida
		SpinnerItem item14 = new SpinnerItem();
		item14.setTitle(resources.getString(SpinnerType.DISTANCE_REMAINING.getTitleId()));
		item14.setImage(resources.getDrawable(SpinnerType.DISTANCE_REMAINING.getImageId()));
		final CustomTextView distanceRemainingTextView = new CustomTextView(getActivity(), "0 " + getString(R.string.kilometers)); 
		distanceRemainingTextView.setText(distanceRemainingString + " " + getString(R.string.kilometers));
		item14.setTextView(distanceRemainingTextView);
		
		//Cronometro
		SpinnerItem item15 = new SpinnerItem();
		item15.setTitle(resources.getString(SpinnerType.CHRONO_TIME_REMAINING.getTitleId()));
		item15.setImage(resources.getDrawable(SpinnerType.CHRONO_TIME_REMAINING.getImageId()));
		Chronometer chronoTimeRemaining = new Chronometer(getActivity(), autoStart ? 0 : PreferencesUtils.getPreferences(
				PreferencesTypes.CLOCK_COUNTDOWN, Integer.class, getActivity()).longValue(), PreferencesUtils.getPreferences(
						PreferencesTypes.CLOCK_SHOW_MILISECONDS, Boolean.class, getActivity()), timeRemaining);
		item15.setTextView(chronoTimeRemaining);
		
		//Se anaden los elementos al mapa
		spinnerItems.put(SpinnerType.CHRONO, item1);
		spinnerItems.put(SpinnerType.DISTANCE, item2);
		spinnerItems.put(SpinnerType.SPEED, item3);
		spinnerItems.put(SpinnerType.SPEED_AVG, item4);
		spinnerItems.put(SpinnerType.SPEED_MAX, item5);
		spinnerItems.put(SpinnerType.STEPS, item6);
		spinnerItems.put(SpinnerType.CALORIES_BURNED, item7);
		spinnerItems.put(SpinnerType.STEP_PACE, item8);
		spinnerItems.put(SpinnerType.ALTITUDE, item9);
		spinnerItems.put(SpinnerType.ALTITUDE_MIN, item10);
		spinnerItems.put(SpinnerType.ALTITUDE_MAX, item11);
		spinnerItems.put(SpinnerType.SPEED_PACE, item12);
		spinnerItems.put(SpinnerType.CALORIES_BURNED_PACE, item13);
		spinnerItems.put(SpinnerType.DISTANCE_REMAINING, item14);
		spinnerItems.put(SpinnerType.CHRONO_TIME_REMAINING, item15);
		
		//Se anaden los elementos a la lista
		items.add(item1);
		items.add(item15);
		items.add(item2);
		items.add(item14);
		items.add(item3);
		items.add(item12);
		items.add(item4);
		items.add(item5);
		items.add(item6);
		items.add(item8);
		items.add(item7);
		items.add(item13);
		items.add(item9);
		items.add(item10);
		items.add(item11);
	}
	
	/**
	 * Metodo que inicializa los listeners para la geolocalizacion
	 */
	private void initMapTracker() {
		//Listener para monitorizar las calorias consumidas
		CaloriesListener.CaloriesDisplayListener caloriesDisplay = new CaloriesListener.CaloriesDisplayListener() {
			@Override
			public void onPositionChanged(float caloriesBurned, float caloriesBurnedPace) {
				if (isAdded() && getActivity() != null) {
					updateSpinnerValue(SpinnerType.CALORIES_BURNED, String.format("%.0f", caloriesBurned));
					updateSpinnerValue(SpinnerType.CALORIES_BURNED_PACE, String.format("%.0f", caloriesBurnedPace));
					adapter.notifyDataSetChanged();
					exercise.setCaloriesBurned(caloriesBurned);
				}
			}
		};
		caloriesListener = new CaloriesListener(getActivity(), caloriesDisplay);
		//Listeners para monitorizar el tiempo transcurrido
		TimeListener timeListener = new TimeListener() {
			@Override
			public void onUpdateTime(long time) {
				adapter.notifyDataSetChanged();
			}
		};
		Chronometer chrono = ((Chronometer)getSpinnerItems().get(SpinnerType.CHRONO).getTextView());
		chrono.addTimeListener(timeListener);
		if (stepPaceListener != null) {
			chrono.addTimeListener(stepPaceListener);
		}
		if (stepSpeedListener != null) {
			chrono.addTimeListener(stepSpeedListener);
		}
		if (stepCaloriesListener != null) {
			chrono.addTimeListener(stepCaloriesListener);
		}
		if (caloriesListener != null) {
			chrono.addTimeListener(caloriesListener);
		}
		
		Chronometer chronoRemaining = ((Chronometer)getSpinnerItems().get(SpinnerType.CHRONO_TIME_REMAINING).getTextView());
		if (useTimeRemaining) {
			chronoRemaining.addFinishTimeListener(this);
		}
		
		//Listener para medir la distancia recorrida
		DistanceListener.DistanceDisplayListener distanceDisplay = new DistanceListener.DistanceDisplayListener() {
			@Override
			public void onPositionChanged(float distance, float remainingDistance) {
				if (isAdded() && getActivity() != null) {
					updateSpinnerValue(SpinnerType.DISTANCE, FormatUtils.formatDistance(distance, getString(R.string.kilometers), getString(R.string.meters)));
					if (useDistanceRemaining) {
						updateSpinnerValue(SpinnerType.DISTANCE_REMAINING, FormatUtils.formatDistance(remainingDistance, getString(R.string.kilometers), getString(R.string.meters)));
					}
					adapter.notifyDataSetChanged();
					exercise.setDistance(distance);
				}
			}
		};
		if (useDistanceRemaining) {
			distanceListener = new DistanceListener(distanceDisplay, this, distanceRemaining);
		} else {
			distanceListener = new DistanceListener(distanceDisplay);
		}
		//Listener para monitorizar la velocidad del usuario
		SpeedListener.SpeedDisplayListener speedDisplay = new SpeedListener.SpeedDisplayListener() {
			@Override
			public void onPositionChanged(float actualSpeed, float speedPace, float speedMax, float speedAvg) {
				if (isAdded() && getActivity() != null) {
					updateSpinnerValue(SpinnerType.SPEED, String.format("%.0f", actualSpeed));
					updateSpinnerValue(SpinnerType.SPEED_PACE, String.format("%.0f", speedPace));
					updateSpinnerValue(SpinnerType.SPEED_AVG, String.format("%.0f", speedAvg));
					updateSpinnerValue(SpinnerType.SPEED_MAX, String.format("%.0f", speedMax));
					adapter.notifyDataSetChanged();
					exercise.setSpeedAvg(speedAvg);
					exercise.setSpeedMax(speedMax);
					exercise.setSpeedPace(speedPace);
				}
			}
		};
		speedListener = new SpeedListener(speedDisplay);
		//Listener para monitorizar la altitud del recorrido
		AltitudeListener.AltitudDisplayListener altitudDisplay = new AltitudeListener.AltitudDisplayListener() {
			@Override
			public void onPositionChanged(double altitude, double altitudeMin, double altitudeMax) {
				if (isAdded() && getActivity() != null) {
					updateSpinnerValue(SpinnerType.ALTITUDE, String.format("%.0f", altitude));
					updateSpinnerValue(SpinnerType.ALTITUDE_MIN, String.format("%.0f", altitudeMin));
					updateSpinnerValue(SpinnerType.ALTITUDE_MAX, String.format("%.0f", altitudeMax));
					adapter.notifyDataSetChanged();
					exercise.setAltitudeMin(altitudeMin);
					exercise.setAltitudeMax(altitudeMax);
				}
			}
		};
		altitudeListener = new AltitudeListener(altitudDisplay);
		//Listener para monitorizar la ruta recorrida
		LineRouteListener.LineRouteDisplayListener lineaRouteDisplay = new LineRouteListener.LineRouteDisplayListener() {
			@Override
			public void onPositionChanged(MapPosition mapPosition) {
				if (isAdded() && getActivity() != null) {
					exercise.addRoutePosition(mapPosition);
				}
			}
		};
		lineRouteListener = new LineRouteListener(lineaRouteDisplay);
	}
	
	/**
	 * Metodo que inicializa el detector de pasos
	 */
	private void initStepDetector() {
		stepDetector = new StepDetector();
		//Listener para contar el numero de pasos
		StepCountListener.StepDisplayListener stepCountDisplay = new StepCountListener.StepDisplayListener() {
			@Override
			public void onStepChanged(long value) {
				if (isAdded() && getActivity() != null) {
					updateSpinnerValue(SpinnerType.STEPS, String.valueOf(value));
					adapter.notifyDataSetChanged();
					exercise.setSteps(value);
				}
			}
		};
		stepCountListener = new StepCountListener(stepCountDisplay);
		//Listener para calcular las calorias consumidas
		StepCaloriesListener.StepDisplayListener caloriesDisplay = new StepCaloriesListener.StepDisplayListener() {
			@Override
			public void onStepChanged(float caloriesBurned, float caloriesBurnedPace) {
				if (isAdded() && getActivity() != null) {
					updateSpinnerValue(SpinnerType.CALORIES_BURNED, String.format("%.0f", caloriesBurned));
					updateSpinnerValue(SpinnerType.CALORIES_BURNED_PACE, String.format("%.0f", caloriesBurnedPace));
					adapter.notifyDataSetChanged();
					exercise.setCaloriesBurned(caloriesBurned);
				}
			}
		};
		stepCaloriesListener = new StepCaloriesListener(getActivity(), caloriesDisplay);
		//Listener para calcular el ritmo de pasos por minuto
		StepPaceListener.StepDisplayListener stepPaceDisplay = new StepPaceListener.StepDisplayListener() {
			@Override
			public void onStepChanged(float value) {
				if (isAdded() && getActivity() != null) {
					updateSpinnerValue(SpinnerType.STEP_PACE, String.format("%.0f", value));
					adapter.notifyDataSetChanged();	
				}
			}
		};
		stepPaceListener = new StepPaceListener(stepPaceDisplay);
		//Los siguientes listeners solo se utilizaran si el GPS no esta activo ya que la medicion no es tan exacta
		if (getLocationTracker()!=null && !getLocationTracker().isEnabled()) {
			//Listener para calcular la distancia recorrida
			StepDistanceListener.StepDisplayListener stepDistanceDisplay = new StepDistanceListener.StepDisplayListener() {
				@Override
				public void onStepChanged(float distance, float remainingDistance) {
					if (isAdded() && getActivity() != null) {
						updateSpinnerValue(SpinnerType.DISTANCE, FormatUtils.formatDistance(distance, getString(R.string.kilometers), getString(R.string.meters)));
						if (useDistanceRemaining) {
							updateSpinnerValue(SpinnerType.DISTANCE_REMAINING, FormatUtils.formatDistance(remainingDistance, getString(R.string.kilometers), getString(R.string.meters)));
						}
						adapter.notifyDataSetChanged();
						exercise.setDistance(distance);
					}
				}
			};
			if (useDistanceRemaining) {
				stepDistanceListener = new StepDistanceListener(getActivity(), stepDistanceDisplay, this, distanceRemaining);
			} else {
				stepDistanceListener = new StepDistanceListener(getActivity(), stepDistanceDisplay);
			}
			//Listener para calcular la velocidad
			StepSpeedListener.StepDisplayListener stepSpeedDisplay = new StepSpeedListener.StepDisplayListener() {
				@Override
				public void onStepChanged(float speed, float speedPace, float speedMax, float speedAvg) {
					if (isAdded() && getActivity() != null) {
						updateSpinnerValue(SpinnerType.SPEED, String.format("%.0f", speed));
						updateSpinnerValue(SpinnerType.SPEED_PACE, String.format("%.0f", speedPace));
						updateSpinnerValue(SpinnerType.SPEED_MAX, String.format("%.0f", speedMax));
						updateSpinnerValue(SpinnerType.SPEED_AVG, String.format("%.0f", speedAvg));
						adapter.notifyDataSetChanged();
						exercise.setSpeedAvg(speedAvg);
						exercise.setSpeedMax(speedMax);
						exercise.setSpeedPace(speedPace);
					}
				}
			};
			stepSpeedListener = new StepSpeedListener(getActivity(), stepSpeedDisplay);
		}
		//Se anaden los listeners al detector
		stepDetector.addStepListener(stepCountListener);
		stepDetector.addStepListener(stepCaloriesListener);
		stepDetector.addStepListener(stepPaceListener);
		if (getLocationTracker()!=null && !getLocationTracker().isEnabled()) {
			stepDetector.addStepListener(stepDistanceListener);
			stepDetector.addStepListener(stepSpeedListener);
			stepDetector.addStepListener(stepCaloriesListener);
		}
	}
	
	/**
	 * Metodo que reinicia todos valores al por defecto
	 */
	protected void resetValues() {
		//Se reinician los valores del spinner
		resetSpinnerAdapterValues();
		//Se reinician los listeners
		if (stepCountListener != null) {
			stepCountListener.reset();
		}
		if (stepDistanceListener != null) {
			stepDistanceListener.reset();
		}
		if (stepSpeedListener != null) {
			stepSpeedListener.reset();
		}
		if (stepCaloriesListener != null) {
			stepCaloriesListener.reset();
		}
		if (stepPaceListener != null) {
			stepPaceListener.reset();
		}
		if (distanceListener != null) {
			distanceListener.reset();
		}
		if (speedListener != null) {
			speedListener.reset();
		}
		if (altitudeListener != null) {
			altitudeListener.reset();
		}
		if (lineRouteListener != null) {
			lineRouteListener.reset();
		}
		if (caloriesListener != null) {
			caloriesListener.reset();
		}
	}
	
	/**
	 * Metodo que reinicia los valores del spinner con los suyos propios por defecto
	 */
	private void resetSpinnerAdapterValues() {
		if (adapter != null) {
			List<SpinnerItem> items = adapter.getItems();
			for (SpinnerItem spinnerItem : items) {
				DefaultTextInterface item = (DefaultTextInterface) spinnerItem.getTextView();
				item.setDefaultText();
			}
			adapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * Metodo que registra un listener para medir los pasos con el acelerometro
	 */
	protected void registerDetector() {
		if (stepDetector != null) {
			sensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
			Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		}
	}
	
	/**
	 * Metodo que elimina el listener para medir los pasos con el acelerometro
	 */
	protected void unRegisterDetector() {
		if (sensorManager != null) {
			sensorManager.unregisterListener(stepDetector);
		}
	}
	
	/**
	 * Metodo que actualiza el valor de un spinner, NO notifica los cambios de la actualizacion
	 * @param spinnerType
	 * @param value
	 */
	protected void updateSpinnerValue(SpinnerType spinnerType, String value) {
		CustomTextView textView = ((CustomTextView)getSpinnerItems().get(spinnerType).getTextView());
		textView.setText(value);
	}
	
	@Override
	public void onFinishTime() {
		//do Nothing
	}
	
	@Override
	public void onCompleteDistance() {
		//do Nothing
	}
	
	@Override
	public void onCompleteStepDistance() {
		//do Nothing
	}
}
