package com.udc.master.tfm.tracksports.fragments.diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExercise;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.common.dialog.CustomDialogBuilder;
import com.udc.master.tfm.tracksports.common.dialog.DialogArrayAdapter;
import com.udc.master.tfm.tracksports.common.dialog.DialogItem;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.directcom.rest.async.SaveExerciseWSAsync;
import com.udc.master.tfm.tracksports.fragments.diary.addexercise.AddPendingExercise;
import com.udc.master.tfm.tracksports.fragments.diary.detail.ExerciseDetailActivity;
import com.udc.master.tfm.tracksports.graph.Graph;
import com.udc.master.tfm.tracksports.graph.GraphDateFormat;
import com.udc.master.tfm.tracksports.graph.GraphFragment;
import com.udc.master.tfm.tracksports.graph.GraphTimeFormat;
import com.udc.master.tfm.tracksports.utils.AlarmUtils;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.DateUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento con la lista de actividades realizadas por el usuario
 * @author a.oteroc
 *
 */
public class DiaryFragment extends GraphFragment implements OnTabChangeListener {

	/** Vista con los distintos fragmentos*/
    private TabHost mTabHost;
	/** Lista con los ejercicios realizados */
	private ListView exercisesListView;
	/** Lista con los ejercicios pendientes de realizar */
	private ListView pendingExercisesListView;
	/** Grafica con los detalles de ejercicio */
	private View plotView;
	/** Vista con la informacion de TAB1*/
	private View tab1;
	/** Vista con la informacion de TAB2*/
	private View tab2;
	/** Vista con la informacion de TAB3*/
	private View tab3;
	/** Tab actual seleccionado */
	private String currentTab = "tab1";
	/** Botones para anadir un ejercicio pendiente */
	private Button buttonAddExercise;
	private Button buttonAddExercise2;
	/** Boton para sincronizar todos los ejercicios */
	private ImageButton buttonSyncExercise;
	/** Botones para mostrar las diferentes graficas */
	private ImageButton buttonGraph1;
	private ImageButton buttonGraph2;
	private ImageButton buttonGraph3;
	private ImageButton buttonGraph4;
	private ImageButton buttonGraph5;
	/** Adaptador para mostrar los ejercicios en la lista */
	private ExerciseArrayAdapter exerciseArrayAdapter;
	/** Adaptador para mostrar los ejercicios pendientes en la lista */
	private PendingExerciseArrayAdapter pendingExerciseArrayAdapter;
	/** Lista de ejercicios */
	private List<Exercise> exercises;
	/** Lista de ejercicios pendientes */
	private List<PendingExercise> pendingExercises;
	/** Ejercicio selecionado */
	private Exercise selectExercise;
	/** Ejercicio pendiente selecionado */
	private PendingExercise selectPendingExercise;
	/** Lista de graficas a mostrar */
	private List<Graph> series = new ArrayList<Graph>();
	/** Indica si es necesario refrescar la lista de ejercicios pendientes */
	private boolean refreshPendingExercisesList;
    
	public void onPause() {
		super.onPause();
	}
	
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (refreshPendingExercisesList) {
			//Se actualiza la lista de ejercicios pendientes
			setPendingExerciseListAdapter(false);
		}
		refreshPendingExercisesList = false;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View diaryView = inflater.inflate(R.layout.fragment_diary, container, false);
		final Resources resources = getActivity().getResources();
		
		exercisesListView = (ListView) diaryView.findViewById(R.id.listView_exercises);
		pendingExercisesListView = (ListView) diaryView.findViewById(R.id.listView_pending_exercises);
		tab1 = diaryView.findViewById(R.id.tab1);
		tab2 = diaryView.findViewById(R.id.tab2);
		tab3 = diaryView.findViewById(R.id.tab3);
		mTabHost = (TabHost)diaryView.findViewById(R.id.tabhost_diary);
		plotView = diaryView.findViewById(R.id.exercise_list_plot);
		
		//Se inicializa la informacion de la grafica
		initPlot(plotView);
		
		//Se crean los tabs
		initialiteTabHost();
		
		//Se obtienen la lista de ejercicios del perfil
		setExerciseListAdapter(true);
		
		//Se obtiene la lista de ejercicios pendientes del perfil
		setPendingExerciseListAdapter(true);
		
		//Se crea el dialogo con las opciones que se pueden realizar sobre un ejercicio
    	DialogArrayAdapter dialogExerciseAdapter = new DialogArrayAdapter(getActivity(), getExerciseItems(resources));
    	
		CustomDialogBuilder builderExercise = new CustomDialogBuilder(getActivity());
		builderExercise.setTitle(resources.getString(R.string.exercise_option_title_message));
		builderExercise.setAdapter(dialogExerciseAdapter, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	if (which == ExerciseOptionType.SYNC_EXERCISE.getId()) {
		    		//Se sincroniza el ejercicio con el servicio Web
		    		if (selectExercise != null && selectExercise.getProfile() != null && selectExercise.getProfile().getGuid() != null) {
		    			//Si el ejercicio ya esta sincronizado se muestra unicamente un aviso
		    			if (HttpStatusType.STATUSES_OK.contains(selectExercise.getSyncStatus())) {
		    				Toast toast = Toast.makeText(getActivity(), R.string.exercise_already_sync, Toast.LENGTH_SHORT);
		    				toast.show();
		    			} else {
			    			SaveExerciseWSAsync saveExerciseWSAsync = new SaveExerciseWSAsync(getActivity(), selectExercise.getProfile(), selectExercise);
			    			saveExerciseWSAsync.execute();
		    			}
		    		}
		    	} else {
		    		if (selectExercise != null) {
		    			confirmDeleteExercise();
		    		}
		    	}
		    	
		    }
		});
    	final AlertDialog dialogExercise = builderExercise.create();
		
		//Se crea el dialogo con las opciones que se pueden realizar sobre un ejercicio pendiente
    	DialogArrayAdapter dialogPendingExerciseAdapter = new DialogArrayAdapter(getActivity(), getPendingExerciseItems(resources));
    	
		CustomDialogBuilder builderPendingExercise = new CustomDialogBuilder(getActivity());
		builderPendingExercise.setTitle(resources.getString(R.string.pending_exercise_option_title_message));
		builderPendingExercise.setAdapter(dialogPendingExerciseAdapter, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
	    		if (selectPendingExercise != null) {
	    			confirmDeletePendingExercise();
	    		}
		    }
		});
    	final AlertDialog dialogPendingExercise = builderPendingExercise.create();    	
    	
		//Se muestra el detalle del ejercicio
		exercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
	        	Exercise exercise = (Exercise) parent.getItemAtPosition(position);
				Intent intent = new Intent(getActivity(), ExerciseDetailActivity.class);
				intent.putExtra(ConstantsUtils.EXERCISE_PARAM, exercise);
				getActivity().startActivity(intent);
	        }
		});
		
		//Se muestra el dialogo sobre el ejercicio
		exercisesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	        	//Se marca el ejericicio selecionado
				Exercise exercise = (Exercise) parent.getItemAtPosition(position);
	        	selectExercise = exercise;
				dialogExercise.show();
				return true;
			}
		});
		
		//Se muestra el dialogo del ejercicio pendiente
		pendingExercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
	        	//Se marca el ejercicio pendiente selecionado
	        	PendingExercise pendingExercise = (PendingExercise) parent.getItemAtPosition(position);
	        	selectPendingExercise = pendingExercise;
	        	dialogPendingExercise.show();
	        }
		});
		
		//Se muestra el dialogo del ejercicio pendiente
		pendingExercisesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	        	//Se marca el ejercicio pendiente selecionado
	        	PendingExercise pendingExercise = (PendingExercise) parent.getItemAtPosition(position);
	        	selectPendingExercise = pendingExercise;
	        	dialogPendingExercise.show();
				return true;
			}
		});
    	
		//Se construye la informacion para mostrar en las graficas
		showGraphs();
		
		//Botones para anadir un ejercicio
		buttonAddExercise = (Button)diaryView.findViewById(R.id.button_add_exercise);
		buttonAddExercise2 = (Button)diaryView.findViewById(R.id.button_add_exercise2);
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AddPendingExercise.class);
				getActivity().startActivity(intent);
				refreshPendingExercisesList = true;
			}
		};
		buttonAddExercise.setOnClickListener(onClickListener);
		buttonAddExercise2.setOnClickListener(onClickListener);
		
		//Boton para sincronizar todos los ejercicios
		buttonSyncExercise = (ImageButton)diaryView.findViewById(R.id.button_sync_exercise);
		buttonSyncExercise.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	    		//Se sincronizan la lista de ejercicios
	    		if (exercises != null && !exercises.isEmpty() && exercises.get(0).getProfile() != null && exercises.get(0).getProfile().getGuid() != null) {
	    			//Solo se sincronizan aquellos que no estan sincronizados
	    			List<Exercise> syncExercises = new ArrayList<Exercise>();
	    			for (Exercise exercise : exercises) {
						if (!HttpStatusType.STATUSES_OK.contains(exercise.getSyncStatus())) {
							syncExercises.add(exercise);
						}
					}
	    			//Si no hay ejercicios para sincronizar se muestra una alerta
	    			if (syncExercises.isEmpty()) {
	    				Toast toast = Toast.makeText(getActivity(), R.string.all_exercises_already_sync, Toast.LENGTH_SHORT);
	    				toast.show();
	    			} else {
		    			SaveExerciseWSAsync saveExerciseWSAsync = new SaveExerciseWSAsync(getActivity(), syncExercises.get(0).getProfile(), syncExercises);
		    			saveExerciseWSAsync.execute();	
	    			}
	    		}
			}
		});
		
        //Se anaden los eventos a los botones para cambiar de grafica
        buttonGraph1 = (ImageButton)diaryView.findViewById(R.id.ImageButton_graph_list1);
        buttonGraph2 = (ImageButton)diaryView.findViewById(R.id.ImageButton_graph_list2);
        buttonGraph3 = (ImageButton)diaryView.findViewById(R.id.ImageButton_graph_list3);
        buttonGraph4 = (ImageButton)diaryView.findViewById(R.id.ImageButton_graph_list4);
        buttonGraph5 = (ImageButton)diaryView.findViewById(R.id.ImageButton_graph_list5);
       
        buttonGraph1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeSeries();
				addSeries(series.get(0));
			}
		});
        buttonGraph2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeSeries();
				addSeries(series.get(1));
			}
		});
        buttonGraph3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeSeries();
				addSeries(series.get(2));
			}
		});
        buttonGraph4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeSeries();
				addSeries(series.get(3));
			}
		});
        buttonGraph5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeSeries();
				addSeries(series.get(4));
			}
		});
        
		return diaryView;
	}
	
	/**
	 * Metodo que inicializa el conjunto de Tabs
	 */
	private void initialiteTabHost() {
		mTabHost.setup();
		//Tab con la lista de ejercicios del perfil
		TabSpec tabList = mTabHost.newTabSpec("tab1").setIndicator(getString(R.string.tab_diary_list));
		//Tab con la lista de ejercicios pendientes del perfil
		TabSpec tabPendingList = mTabHost.newTabSpec("tab2").setIndicator(getString(R.string.tab_diary_pending_list));
		//Tab con las graficas comparando los ejercicios
		TabSpec tabGraph = mTabHost.newTabSpec("tab3").setIndicator(getString(R.string.tab_diary_graph));
		tabList.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				return tab1;
			}
		});
		tabPendingList.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				return tab2;
			}
		});
		tabGraph.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				return tab3;
			}
		});
		mTabHost.addTab(tabList);
		mTabHost.addTab(tabPendingList);
		mTabHost.addTab(tabGraph);
		mTabHost.setCurrentTabByTag(currentTab);
		mTabHost.setOnTabChangedListener(this);
		
		//Se inicializa el color de fondo
		TabWidget widget = mTabHost.getTabWidget();
		for(int i = 0; i < widget.getChildCount(); i++) {
		    View v = widget.getChildAt(i);

		    // Look for the title view to ensure this is an indicator and not a divider.
		    TextView tv = (TextView)v.findViewById(android.R.id.title);
		    if(tv == null) {
		        continue;
		    }
		    v.setBackgroundResource(R.drawable.tab_indicator_holo);
		}
	}
	
	@Override
	public void onTabChanged(String tabId) {
		currentTab = tabId;
		mTabHost.setCurrentTabByTag(tabId);
	}
	
	/**
	 * Metodo que crea o actualiza el adaptador para gestionar la lista de ejercicios
	 * @param forceCreate Fuerza la creacion del adaptador
	 */
	private void setExerciseListAdapter(boolean forceCreate) {
		ExerciseDAO exerciseHelper = DatabaseFactory.getInstance().getExerciseDAO(getActivity());
		exercises = exerciseHelper.findExercisesByProfile(PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, getActivity()), null);
		
		if (forceCreate) {
			exerciseArrayAdapter = null;
		}
		
		//Si no se devuelven resultados se setea la lista vacia
		if (exercises == null) {
			exercises = new ArrayList<Exercise>();
		}
		
		if (exerciseArrayAdapter != null) {
			exerciseArrayAdapter.setExercises(exercises);
			exerciseArrayAdapter.notifyDataSetChanged();
		} else {
			exerciseArrayAdapter = new ExerciseArrayAdapter(getActivity(), exercises);
			exercisesListView.setAdapter(exerciseArrayAdapter);
		}
	}
	
	/**
	 * Metodo que crea o actualiza el adaptador para gestionar la lista de ejercicios pendientes
	 * @param forceCreate Fuerza la creacion del adaptador
	 */
	private void setPendingExerciseListAdapter(boolean forceCreate) {
		PendingExerciseDAO pendingExerciseHelper = DatabaseFactory.getInstance().getPendingExerciseDAO(getActivity());
		pendingExercises = pendingExerciseHelper.findPendingExercisesByProfile(PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, getActivity()));
		
		if (forceCreate) {
			pendingExerciseArrayAdapter = null;
		}
		
		//Si no se devuelven resultados se setea la lista vacia
		if (pendingExercises == null) {
			pendingExercises = new ArrayList<PendingExercise>();
		}
		
		if (pendingExerciseArrayAdapter != null) {
			pendingExerciseArrayAdapter.setPendingExercises(pendingExercises);
			pendingExerciseArrayAdapter.notifyDataSetChanged();
		} else {
			pendingExerciseArrayAdapter = new PendingExerciseArrayAdapter(getActivity(), pendingExercises);
			pendingExercisesListView.setAdapter(pendingExerciseArrayAdapter);
		}
	}
	
    /**
     * Metodo que obtiene la lista de elementos para mostrar
     * en el dialogo de opciones sobre un ejercicio
     * @param resources
     * @return
     */
    private List<DialogItem> getExerciseItems(Resources resources) {
    	List<DialogItem> items = new ArrayList<DialogItem>();
    	
    	String [] elements = resources.getStringArray(R.array.exerciseOptions);
    	for (int i = 0; i < elements.length; i++) {
    		Drawable image = null;
    		if (i == ExerciseOptionType.SYNC_EXERCISE.getId()) {
    			image = resources.getDrawable(R.drawable.sync_ok);
    		} else {
    			image = resources.getDrawable(R.drawable.delete);
    		}
    		DialogItem item = new DialogItem(image, elements[i]);
    		items.add(item);
		}
    	return items;
    }
    
    /**
     * Metodo que obtiene la lista de elementos para mostrar
     * en el dialogo de opciones sobre un ejercicio pendiente
     * @param resources
     * @return
     */
    private List<DialogItem> getPendingExerciseItems(Resources resources) {
    	List<DialogItem> items = new ArrayList<DialogItem>();
    	
    	String [] elements = resources.getStringArray(R.array.pendingExerciseOptions);
    	for (int i = 0; i < elements.length; i++) {
    		Drawable image = resources.getDrawable(R.drawable.delete);
    		DialogItem item = new DialogItem(image, elements[i]);
    		items.add(item);
		}
    	return items;
    }
    
    /**
     * Metodo que construye las graficas para mostrar
     */
    private void showGraphs() {
    	//Se limpia las graficas por si es necesario recargar 
    	series.clear();
    	removeSeries();
    	
		List<Number> durationSeries = new ArrayList<Number>();
		List<Number> distanceSeries = new ArrayList<Number>();
		List<Number> speedAvgSeries = new ArrayList<Number>();
		List<Number> speedPaceSeries = new ArrayList<Number>();
		List<Number> caloriesBurnedSeries = new ArrayList<Number>();
		List<Number> startTimeSeries = new ArrayList<Number>();
		
		for (Exercise exercise : exercises) {
			durationSeries.add(exercise.getDuration());
			
    		Float distance = Float.valueOf(String.format(DateUtils.DEFAULT_LOCALE, "%.0f", exercise.getDistance()));
    		distance /= 1000; //Se muestra la informacion en Km
    		distanceSeries.add(distance);
			
    		Float speedPace = Float.valueOf(String.format(DateUtils.DEFAULT_LOCALE, "%.2f", exercise.getSpeedPace()).replace(",", "."));
    		speedPaceSeries.add(speedPace);
    		
    		Float speed = Float.valueOf(String.format(DateUtils.DEFAULT_LOCALE, "%.2f", exercise.getSpeedAvg()).replace(",", "."));
    		speedAvgSeries.add(speed);
    		
    		Float caloriesBurned = Float.valueOf(String.format(DateUtils.DEFAULT_LOCALE, "%.0f", exercise.getCaloriesBurned()));
    		caloriesBurnedSeries.add(caloriesBurned);
    		
    		startTimeSeries.add(exercise.getStartTime().getTime());
			
		}
		
		XYSeries durationTime = new SimpleXYSeries(startTimeSeries, durationSeries, getString(R.string.graph_duration_title));
		XYSeries distanceTime = new SimpleXYSeries(startTimeSeries, distanceSeries, getString(R.string.graph_distance_title));
		XYSeries speedAvgTime = new SimpleXYSeries(startTimeSeries, speedAvgSeries, getString(R.string.graph_speed_title));
		XYSeries speedPaceTime = new SimpleXYSeries(startTimeSeries, speedPaceSeries, getString(R.string.graph_speed_pace_title));
		XYSeries caloriesBurnedTime = new SimpleXYSeries(startTimeSeries, caloriesBurnedSeries, getString(R.string.graph_calories_title));
		
        Format floatFormat = new DecimalFormat("###.##");
        Format integerFormat = new DecimalFormat("###");
		Format dayFormat = new GraphDateFormat();
        Format timeFormat = new GraphTimeFormat();
		
        Graph graphDurationTime = new Graph(
        		durationTime, 
        		Color.YELLOW,
        		false,
        		false,
        		getString(R.string.graph_duration_title), 
        		false,
        		getString(R.string.graph_day_label), 
        		getString(R.string.graph_time_label),
        		dayFormat, 
        		timeFormat);
		
        Graph graphDistanceTime = new Graph(
        		distanceTime, 
        		Color.BLUE,
        		false,
        		false,
        		getString(R.string.graph_distance_title), 
        		false,
        		getString(R.string.graph_day_label), 
        		getString(R.string.graph_distance_label),  
        		dayFormat, 
        		floatFormat);
        
        Graph graphSpeedAvgTime = new Graph(
        		speedAvgTime, 
        		Color.RED,
        		false,
        		false,
        		getString(R.string.graph_speed_title), 
        		false,
        		getString(R.string.graph_day_label), 
        		getString(R.string.graph_speed_label),  
        		dayFormat, 
        		floatFormat);
        
        Graph graphSpeedPaceTime = new Graph(
        		speedPaceTime, 
        		Color.GREEN,
        		false,
        		false,
        		getString(R.string.graph_speed_pace_title), 
        		false,
        		getString(R.string.graph_day_label), 
        		getString(R.string.graph_speed_pace_label),  
        		dayFormat, 
        		floatFormat);
        
        Graph graphCaloriesBurnedTime = new Graph(
        		caloriesBurnedTime, 
        		Color.MAGENTA,
        		false,
        		false,
        		getString(R.string.graph_calories_title), 
        		false,
        		getString(R.string.graph_day_label), 
        		getString(R.string.graph_calories_label),  
        		dayFormat, 
        		integerFormat);
        
        series.add(graphDurationTime);
        series.add(graphDistanceTime);
        series.add(graphSpeedAvgTime);
        series.add(graphSpeedPaceTime);
        series.add(graphCaloriesBurnedTime);
        addSeries(series.get(0));
    }
    
    /**
     * Metodo que actualiza la lista de ejercicios y sus graficas
     */
    public void refreshExercises() {
		//Se refresca la lista de ejercicios
		setExerciseListAdapter(true);
		//Se refresca la lista de ejercicios pendientes
		setPendingExerciseListAdapter(true);
		//se refrescan las graficas
		showGraphs();
    }
    
    /**
     * Metodo que envia una alertar para confirmar la eliminacion de un ejercicio
     * Si el usuario lo acepta, se elimina
     */
	private void confirmDeleteExercise() {
		CustomDialogBuilder alertDialog = new CustomDialogBuilder(getActivity());
        alertDialog.setTitle(R.string.exercise_delete_confirmation_title);
        alertDialog.setMessage(R.string.exercise_delete_confirmation_content);
        //Si se presiona el boton de configuracion
        alertDialog.setPositiveButton(R.string.exercise_delete_confirmation_ok_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	ExerciseDAO exerciseHelper = DatabaseFactory.getInstance().getExerciseDAO(getActivity());
	    		exerciseHelper.delete(selectExercise);
	    		selectExercise = null;
				Toast toast = Toast.makeText(getActivity(), R.string.exercise_option_remove_succesful, Toast.LENGTH_SHORT);
				toast.show();
				refreshExercises();
            }
        });
        //Si se presiona el boton de cancelar
        alertDialog.setNegativeButton(R.string.exercise_delete_confirmation_cance_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Se muestra el dialogo
        alertDialog.show();
	}
	
    /**
     * Metodo que envia una alertar para confirmar la eliminacion de un ejercicio pendiente
     * Si el usuario lo acepta, se elimina
     */
	private void confirmDeletePendingExercise() {
		CustomDialogBuilder alertDialog = new CustomDialogBuilder(getActivity());
        alertDialog.setTitle(R.string.pending_exercise_delete_confirmation_title);
        alertDialog.setMessage(R.string.pending_exercise_delete_confirmation_content);
        //Si se presiona el boton de configuracion
        alertDialog.setPositiveButton(R.string.pending_exercise_delete_confirmation_ok_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
    			//Si el ejercicio es el proximo en ejecutar se elimina como pendiente
				PendingExercise pendingExercise = PreferencesUtils.getPreferences(PreferencesTypes.PENDING_EXERCISE, PendingExercise.class, getActivity());
				if (pendingExercise != null && pendingExercise.getId().equals(selectPendingExercise.getId())) {
					PreferencesUtils.deletePreferences(PreferencesTypes.PENDING_EXERCISE, getActivity());
					AlarmUtils.deletePendingExerciseAlarm(getActivity());
					PreferencesUtils.deletePreferences(PreferencesTypes.IS_INTERVAL_EXECUTE_PENDING_EXERCISE, getActivity());
				}
    			
				PendingExerciseDAO pendingExerciseHelper = DatabaseFactory.getInstance().getPendingExerciseDAO(getActivity());
    			pendingExerciseHelper.delete(selectPendingExercise);
    			selectPendingExercise = null;
				Toast toast = Toast.makeText(getActivity(), R.string.pending_exercise_option_remove_succesful, Toast.LENGTH_SHORT);
				toast.show();
				//Se refresca la lista de ejercicios pendientes
				setPendingExerciseListAdapter(true);
            }
        });
        //Si se presiona el boton de cancelar
        alertDialog.setNegativeButton(R.string.pending_exercise_delete_confirmation_cance_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Se muestra el dialogo
        alertDialog.show();
	}
}
