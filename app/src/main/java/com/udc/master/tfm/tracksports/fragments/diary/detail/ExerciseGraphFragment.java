package com.udc.master.tfm.tracksports.fragments.diary.detail;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;

import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.graph.Graph;
import com.udc.master.tfm.tracksports.graph.GraphFragment;
import com.udc.master.tfm.tracksports.graph.GraphTimeFormat;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.DateUtils;

/**
 * Fragmento con las gráficas resumen del ejercicio
 * @author a.oteroc
 *
 */
public class ExerciseGraphFragment extends GraphFragment {

	/** Ejercicio que realiza el usuario */
	private Exercise exercise;
	/** Lista de graficas a mostrar */
	private List<Graph> series = new ArrayList<Graph>();
	/** Botones para mostrar las diferentes graficas */
	private ImageButton buttonGraph1;
	private ImageButton buttonGraph2;
	private ImageButton buttonGraph3;
	private ImageButton buttonGraph4;
	private ImageButton buttonGraph5;
	private ImageButton buttonGraph6;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View graphView = inflater.inflate(R.layout.fragment_exercise_graph, container, false);
		//Se obtiene el detalle del ejercicio
		exercise = (Exercise) getActivity().getIntent().getSerializableExtra(ConstantsUtils.EXERCISE_PARAM);
		//Se obtiene la grafica para pintar el ejercicio
		View plotView = graphView.findViewById(R.id.exercise_detail_plot);
		initPlot(plotView);
        List<MapPosition> points = exercise.getRoute();
        if (points == null || points.isEmpty()) {
        	return graphView;
        }
        //Se muestran las graficas
		showGraphs(points);
        
        //Se anaden los eventos a los botones para cambiar de grafica
        buttonGraph1 = (ImageButton)graphView.findViewById(R.id.ImageButton_graph1);
        buttonGraph2 = (ImageButton)graphView.findViewById(R.id.ImageButton_graph2);
        buttonGraph3 = (ImageButton)graphView.findViewById(R.id.ImageButton_graph3);
        buttonGraph4 = (ImageButton)graphView.findViewById(R.id.ImageButton_graph4);
        buttonGraph5 = (ImageButton)graphView.findViewById(R.id.ImageButton_graph5);
        buttonGraph6 = (ImageButton)graphView.findViewById(R.id.ImageButton_graph6);
       
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
        buttonGraph6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeSeries();
				addSeries(series.get(5));
			}
		});
        
		return graphView;
	}
	
	private void showGraphs(List<MapPosition> points) {
		//Se limpia las graficas por si es necesario recargar
		series.clear();
		removeSeries();
		
        //Se obtiene los valores y se construyen las graficas
        int totalSize = points.size();
		int percentGraphPoints = 100; //TODO: Configurar
		int totalGraphPoints = totalSize * percentGraphPoints / 100;
		int moduleGraphPoints = 0;
		if (totalGraphPoints > 0) {
			moduleGraphPoints = totalSize / totalGraphPoints;
		}
		
		List<Number> distanceSeries = new ArrayList<Number>();
		List<Number> speedPaceSeries = new ArrayList<Number>();
		List<Number> speedSeries = new ArrayList<Number>();
		List<Number> altitudeSeries = new ArrayList<Number>();
		List<Number> timeSeries = new ArrayList<Number>();
        for (int i = 0; i < totalSize; i++) {
        	MapPosition mapPosition = points.get(i);
        	if (i + 1 == totalSize || i % moduleGraphPoints == 0) {
        		Float distance = Float.valueOf(String.format(DateUtils.DEFAULT_LOCALE, "%.0f", mapPosition.getDistance()));
        		distance /= 1000; //Se muestra la informacion en Km
        		distanceSeries.add(distance);
        		Float speedPace = Float.valueOf(String.format(DateUtils.DEFAULT_LOCALE, "%.2f", mapPosition.getSpeedPace()).replace(",", "."));
        		speedPaceSeries.add(speedPace);
        		Float speed = Float.valueOf(String.format(DateUtils.DEFAULT_LOCALE, "%.2f", mapPosition.getSpeed()).replace(",", "."));
        		speedSeries.add(speed);
        		Integer altitude = Integer.valueOf(String.format(DateUtils.DEFAULT_LOCALE, "%.0f", mapPosition.getAltitude()));
        		altitudeSeries.add(altitude);
        		Long time = mapPosition.getTime();
        		timeSeries.add(time);
        	}
		}
        
        XYSeries speedPaceDistance = new SimpleXYSeries(distanceSeries, speedPaceSeries, getString(R.string.graph_speed_pace_title));
        XYSeries speedDistance = new SimpleXYSeries(distanceSeries, speedSeries, getString(R.string.graph_speed_title));
        XYSeries altitudeDistance = new SimpleXYSeries(distanceSeries, altitudeSeries, getString(R.string.graph_altitude_title));

        XYSeries speedPaceTime = new SimpleXYSeries(timeSeries, speedPaceSeries, getString(R.string.graph_speed_pace_title));
        XYSeries speedTime = new SimpleXYSeries(timeSeries, speedSeries, getString(R.string.graph_speed_title));
        XYSeries altitudeTime = new SimpleXYSeries(timeSeries, altitudeSeries, getString(R.string.graph_altitude_title));
        
        Format floatFormat = new DecimalFormat("###.##");
        Format integerFormat = new DecimalFormat("###");
        Format timeFormat = new GraphTimeFormat(exercise.getStartTime());
        
        Graph graphSpeedPaceDistance = new Graph(
        		speedPaceDistance,
        		Color.YELLOW,
        		true,
        		false,
        		getString(R.string.graph_speed_pace_title),
        		false,
        		getString(R.string.graph_distance_label), 
        		getString(R.string.graph_speed_pace_label), 
        		floatFormat, 
        		floatFormat);
        Graph graphSpeedDistance = new Graph(
        		speedDistance, 
        		Color.RED,
        		true,
        		false,
        		getString(R.string.graph_speed_title), 
        		false,
        		getString(R.string.graph_distance_label), 
        		getString(R.string.graph_speed_label), 
        		floatFormat, 
        		floatFormat);
        Graph graphAltitudeDistance = new Graph(
        		altitudeDistance,
        		Color.GREEN,
        		true,
        		false,
        		getString(R.string.graph_altitude_title), 
        		false,
        		getString(R.string.graph_distance_label), 
        		getString(R.string.graph_altitude_label),  
        		floatFormat, 
        		integerFormat);
        Graph graphSpeedPaceTime = new Graph(
        		speedPaceTime,
        		Color.BLUE,
        		true,
        		false,
        		getString(R.string.graph_speed_pace_title), 
        		false,
        		getString(R.string.graph_time_label), 
        		getString(R.string.graph_speed_pace_label),  
        		timeFormat, 
        		floatFormat);
        Graph graphSpeedTime = new Graph(speedTime, 
        		Color.CYAN,
        		true,
        		false,
        		getString(R.string.graph_speed_title), 
        		false,
        		getString(R.string.graph_time_label), 
        		getString(R.string.graph_speed_label),  
        		timeFormat, 
        		floatFormat);
        Graph graphAltitudeTime = new Graph(
        		altitudeTime, 
        		Color.MAGENTA,
        		true,
        		false,
        		getString(R.string.graph_altitude_title),
        		false,
        		getString(R.string.graph_time_label), 
        		getString(R.string.graph_altitude_label),  
        		timeFormat, 
        		integerFormat);
        
        series.add(graphSpeedPaceDistance);
        series.add(graphSpeedDistance);
        series.add(graphAltitudeDistance);
        series.add(graphSpeedPaceTime);
        series.add(graphSpeedTime);
        series.add(graphAltitudeTime);
        addSeries(series.get(0));
	}
}
