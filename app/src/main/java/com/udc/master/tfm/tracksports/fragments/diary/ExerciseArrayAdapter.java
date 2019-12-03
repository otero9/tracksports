package com.udc.master.tfm.tracksports.fragments.diary;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.utils.DateUtils;
import com.udc.master.tfm.tracksports.utils.FormatUtils;

/**
 * Adaptador para mostrar la informacion de los ejercicios de un usuario en un ListView
 * @author a.oteroc
 *
 */
public class ExerciseArrayAdapter extends ArrayAdapter<Exercise> {

	/** Contexto de la aplicacion */
	private final Context context;
	/** Lista de ejercicios */
	private List<Exercise> exercises;
	
	/**
	 * Constructor del adaptador
	 * @param context
	 * @param profiles
	 */
	public ExerciseArrayAdapter (Context context, List<Exercise> exercises) {
		super(context, R.layout.exercise_view, exercises);
		this.context = context;
		this.exercises = exercises;
	}
	
    @Override
    public int getCount() {
        return this.exercises.size();
    }
 
    @Override
    public Exercise getItem(int position) {
        return this.exercises.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View rowView = convertView;
    	
    	//Se crea la nueva vista para la fila dentro de la lista
    	if (convertView == null) {
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		rowView = inflater.inflate(R.layout.exercise_view, parent, false);
    	}
        
    	//Se setea la fila con la informacion del exercicio
    	TextView startTimeTextView = (TextView) rowView.findViewById(R.id.textView_startTime);
    	TextView distanceTextView = (TextView) rowView.findViewById(R.id.textView_distance);
    	TextView durationTextView = (TextView) rowView.findViewById(R.id.textView_duration);
    	TextView caloriesTextView = (TextView) rowView.findViewById(R.id.textView_calories);
    	ImageView syncImageView = (ImageView) rowView.findViewById(R.id.imageView_sync);
    	TextView syncTextView = (TextView) rowView.findViewById(R.id.textView_sync);
	
    	Exercise exercise = exercises.get(position);
    	
    	startTimeTextView.setText(DateUtils.getDateHourFormatter().format(exercise.getStartTime()));
    	
    	distanceTextView.setText(FormatUtils.formatDistance(
    			exercise.getDistance(), 
    			context.getString(R.string.kilometers), 
    			context.getString(R.string.meters)));
    	
    	durationTextView.setText(FormatUtils.formatTime(
    			exercise.getDuration(), 
    			false, 
    			context.getString(R.string.hours), 
    			context.getString(R.string.minutes), 
    			null));
    	caloriesTextView.setText(String.format("%.0f", exercise.getCaloriesBurned()) + " " + context.getString(R.string.calories_measure));
    	
    	if (HttpStatusType.STATUSES_OK.contains(exercise.getSyncStatus())) {
    		syncTextView.setText(context.getString(R.string.exercise_sync));
    		syncImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.sync_ok));
    	} else if (HttpStatusType.STATUSES_KO.contains(exercise.getSyncStatus())) {
    		syncTextView.setText(context.getString(R.string.exercise_sync_problem));
    		syncImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.sync_problem));
    	} else { //No sincronizado
    		syncTextView.setText(context.getString(R.string.exercise_not_sync));
    		syncImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.not_sync));
    	}
    	return rowView;
    }

	/**
	 * @return the exercises
	 */
	public List<Exercise> getExercises() {
		return exercises;
	}

	/**
	 * @param exercises the exercises to set
	 */
	public void setExercises(List<Exercise> exercises) {
		this.exercises = exercises;
	}
}
