package com.udc.master.tfm.tracksports.fragments.diary;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExercise;
import com.udc.master.tfm.tracksports.utils.DateUtils;

/**
 * Adaptador para mostrar la informacion de los ejercicios pendientes de un usuario en un ListView
 * @author a.oteroc
 *
 */
public class PendingExerciseArrayAdapter extends ArrayAdapter<PendingExercise> {

	/** Contexto de la aplicacion */
	private final Context context;
	/** Lista de ejercicios pendientes */
	private List<PendingExercise> pendingExercises;
	
	/**
	 * Constructor del adaptador
	 * @param context
	 * @param profiles
	 */
	public PendingExerciseArrayAdapter (Context context, List<PendingExercise> pendingExercises) {
		super(context, R.layout.pending_exercise_view, pendingExercises);
		this.context = context;
		this.pendingExercises = pendingExercises;
	}
	
    @Override
    public int getCount() {
        return this.pendingExercises.size();
    }
 
    @Override
    public PendingExercise getItem(int position) {
        return this.pendingExercises.get(position);
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
    		rowView = inflater.inflate(R.layout.pending_exercise_view, parent, false);
    	}
        
    	//Se setea la fila con la informacion del ejercicio pendiente
    	TextView startTimeTextView = (TextView) rowView.findViewById(R.id.textView_startTime);
    	TextView distanceTextView = (TextView) rowView.findViewById(R.id.textView_distance);
    	TextView durationTextView = (TextView) rowView.findViewById(R.id.textView_duration);
    	TextView commentsTextView = (TextView) rowView.findViewById(R.id.textView_comments);
    	
    	PendingExercise pendingExercise = pendingExercises.get(position);
    	
    	startTimeTextView.setText(DateUtils.getDateHourFormatter().format(pendingExercise.getStartTime()));
    	
    	distanceTextView.setText(
    			Integer.valueOf(String.format(DateUtils.DEFAULT_LOCALE, "%.0f", pendingExercise.getDistance()))
    			+ " " + context.getString(R.string.kilometers));	
    	
    	durationTextView.setText(pendingExercise.getDuration() + " " + context.getString(R.string.minutes));	
    	
    	if (pendingExercise.getComments() != null && !pendingExercise.getComments().isEmpty()) {
    		commentsTextView.setText(pendingExercise.getComments());
    	} else {
    		commentsTextView.setText(context.getString(R.string.pending_exercise_no_comments));
    	}
    	
    	return rowView;
    }

	/**
	 * @return the pendingExercises
	 */
	public List<PendingExercise> getPendingExercises() {
		return pendingExercises;
	}

	/**
	 * @param pendingExercises the pendingExercises to set
	 */
	public void setPendingExercises(List<PendingExercise> pendingExercises) {
		this.pendingExercises = pendingExercises;
	}
}
