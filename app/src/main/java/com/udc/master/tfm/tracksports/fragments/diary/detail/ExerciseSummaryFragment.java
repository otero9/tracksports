package com.udc.master.tfm.tracksports.fragments.diary.detail;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.DateUtils;
import com.udc.master.tfm.tracksports.utils.FormatUtils;

/**
 * Fragmento con el detalle de un ejericio
 * @author a.oteroc
 *
 */
public class ExerciseSummaryFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View summaryView = inflater.inflate(R.layout.fragment_exercise_summary, container, false);
		
		Exercise exercise = (Exercise) getActivity().getIntent().getSerializableExtra(ConstantsUtils.EXERCISE_PARAM);
		
    	TextView startTimeTextView = (TextView) summaryView.findViewById(R.id.TextView_startTime_detail);
    	TextView durationTextView = (TextView) summaryView.findViewById(R.id.TextView_duration_detail);
    	TextView distanceTextView = (TextView) summaryView.findViewById(R.id.TextView_distance_detail);
    	TextView speedAvgTextView = (TextView) summaryView.findViewById(R.id.TextView_speedAvg_detail);
    	TextView speedMaxTextView = (TextView) summaryView.findViewById(R.id.TextView_speedMax_detail);
    	TextView speedPaceTextView = (TextView) summaryView.findViewById(R.id.TextView_speedPace_detail);
    	TextView altitudeMinTextView = (TextView) summaryView.findViewById(R.id.TextView_altitudeMin_detail);
    	TextView altitudeMaxTextView = (TextView) summaryView.findViewById(R.id.TextView_altitudeMax_detail);
    	TextView caloriesBurnedTextView = (TextView) summaryView.findViewById(R.id.TextView_calories_burned_detail);
    	TextView stepsTextView = (TextView) summaryView.findViewById(R.id.TextView_steps_detail);
    	
    	startTimeTextView.setText(DateUtils.getDateHourFormatter().format(exercise.getStartTime()));
    	durationTextView.setText(FormatUtils.formatTime(
    			exercise.getDuration(), 
    			true, 
    			getActivity().getString(R.string.hours), 
    			getActivity().getString(R.string.minutes), 
    			getActivity().getString(R.string.seconds)));
    	distanceTextView.setText(FormatUtils.formatDistance(
    			exercise.getDistance(), 
    			getActivity().getString(R.string.kilometers), 
    			getActivity().getString(R.string.meters)));
    	speedAvgTextView.setText(String.format("%.0f", exercise.getSpeedAvg()) + " " + getActivity().getString(R.string.speed_measure));
    	speedMaxTextView.setText(String.format("%.0f", exercise.getSpeedMax()) + " " + getActivity().getString(R.string.speed_measure));
    	speedPaceTextView.setText(String.format("%.0f", exercise.getSpeedPace()) + " " + getActivity().getString(R.string.speed_pace_measure));
    	altitudeMinTextView.setText(String.format("%.0f", exercise.getAltitudeMin()) + " " + getActivity().getString(R.string.altitude_measure));
    	altitudeMaxTextView.setText(String.format("%.0f", exercise.getAltitudeMax()) + " " + getActivity().getString(R.string.altitude_measure));
    	caloriesBurnedTextView.setText(String.format("%.0f", exercise.getCaloriesBurned()) + " " + getActivity().getString(R.string.calories_measure));
    	stepsTextView.setText(exercise.getSteps().toString());
    	
		return summaryView;
	}
}
