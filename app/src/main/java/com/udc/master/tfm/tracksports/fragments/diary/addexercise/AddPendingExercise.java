package com.udc.master.tfm.tracksports.fragments.diary.addexercise;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExercise;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.utils.AlarmUtils;
import com.udc.master.tfm.tracksports.utils.DateUtils;
import com.udc.master.tfm.tracksports.utils.StringUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Actividad para añadir un ejercicio a la lista de pendientes
 * @author a.oteroc
 *
 */
public class AddPendingExercise extends Activity {

	/** Texto con la hora de inicio */
	private EditText startTimeEditText;
	/** Texto con la distancia a recorrer */
	private EditText distanceEditText;
	/** Texto con la duracion del ejercicio a llevar a cabo */
	private EditText durationEditText;
	/** Texto con los comentarios del ejercicio*/
	private EditText commentsEditText;
	/** Boton para seleccionar la hora de inicio */
	private ImageButton selectStartTimeButton;
	/** Selector de hora */
	private TimePicker timePicker;
	/** Selector de fecha */
	private DatePicker datePicker;
	/** Boton para anadir el ejercicio */
	private Button addButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_pending_exercise);
		
		startTimeEditText = (EditText)findViewById(R.id.editText_pending_exercise_startTime);
		distanceEditText = (EditText) findViewById(R.id.editText_pending_exercise_distance);
		durationEditText = (EditText) findViewById(R.id.editText_pending_exercise_duration);
		commentsEditText = (EditText) findViewById(R.id.editText_pending_exercise_comments);
		selectStartTimeButton = (ImageButton) findViewById(R.id.button_select_startTime);
		addButton = (Button) findViewById(R.id.button_add_pending_exercise);
		
		//Se evita poder editar la fecha de forma manual
		startTimeEditText.setKeyListener(null);
		
		//Se muestra el dialogo para seleccionar la hora de inicio
		selectStartTimeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddPendingExercise.this);
//				dialogBuilder.setView(R.layout.date_time_picker_layout);
//				dialogBuilder.setTitle(getString(R.string.date_time_picker_title));
				dialogBuilder.setCancelable(false);
				dialogBuilder.setPositiveButton(getString(R.string.date_time_picker_accept_button), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
						startTimeEditText.setText(DateUtils.getDateHourWithoutSecondsFormatter().format(calendar.getTime()));
						validateStartTime(startTimeEditText);
					}
				});
				dialogBuilder.setNegativeButton(getString(R.string.date_time_picker_cancel_button), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog dialog = dialogBuilder.create();
				dialog.setView(getLayoutInflater().inflate(R.layout.date_time_picker_layout, null));
				dialog.show();
				timePicker = (TimePicker)dialog.findViewById(R.id.timePicker);
				timePicker.setIs24HourView(true);
				datePicker = (DatePicker)dialog.findViewById(R.id.datePicker);
			}
		});
		
		//Se valida la fecha de inicio
		startTimeEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateStartTime((TextView)v);
				}
			}
		});
		
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PendingExercise pendingExercise = new PendingExercise();
				String startTimeText = startTimeEditText.getText().toString(); 
				if (!startTimeText.isEmpty()) {
					try {
						pendingExercise.setStartTime(DateUtils.getDateHourWithoutSecondsFormatter().parse(startTimeText));
					} catch (ParseException e) {
						startTimeEditText.setError(getString(R.string.validate_birthday_invalid_value));
					}
				}
				String distanceText = distanceEditText.getText().toString();
				if (!distanceText.isEmpty()) {
					pendingExercise.setDistance(Float.valueOf(distanceText));
				}
				String durationText = durationEditText.getText().toString();
				if (!durationText.isEmpty()) {
					pendingExercise.setDuration(Long.valueOf(durationText));
				}
				String commentsText = commentsEditText.getText().toString();
				if (!commentsText.isEmpty()) {
					pendingExercise.setComments(commentsText);
				}
				pendingExercise.setProfile(PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, getApplicationContext()));
				
				if (validateForm()) {
					//Se inserta el ejercicio pendiente
					PendingExerciseDAO pendingExerciseHelper = DatabaseFactory.getInstance().getPendingExerciseDAO(getApplicationContext());
					pendingExerciseHelper.insert(pendingExercise);
					
					//Se vuelve a crear la alarma por si el ejercicio esta ya en el intervalo de ejecucion
					Integer checkPendingExerciseCloseMinutes = PreferencesUtils.getPreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_CLOSE_MINUTES, Integer.class, getApplicationContext());
					AlarmUtils.createCheckPendingExercisesAlarm(getBaseContext(), checkPendingExerciseCloseMinutes * 60 * 1000);
					
					finish();
				}
			}
		});
	}
	
	/**
	 * Metodo que valida todos los campos del formulario
	 * @return
	 */
	private boolean validateForm() {
		boolean validateStartTime = true;
		if (startTimeEditText != null) {
			validateStartTime = validateStartTime(startTimeEditText);
		}
		return validateStartTime;
	}
	
	/**
	 * Se valida si la fecha de inicio es correcta
	 * @param t
	 * @return
	 */
	private boolean validateStartTime(TextView t) {
		if (StringUtils.isBlank(t.getText())) {
			t.setError(getString(R.string.validate_pending_exercise_startTime));
			return false;
		} else {
			try {
				//Se comprueba si la fecha introducida es anterior a la fecha actual
				Date selectDate = DateUtils.getDateHourWithoutSecondsFormatter().parse(t.getText().toString());
				if (selectDate.before(Calendar.getInstance().getTime())) {
					t.setError(getString(R.string.validate_pending_exercise_startTime_previous_now));
					return false;
				}
			} catch (ParseException e) {
				t.setError(getString(R.string.validate_pending_exercise_startTime_not_valid));
				return false;
			}
			
			
			t.setError(null);
			return true;
		}
	}
}
