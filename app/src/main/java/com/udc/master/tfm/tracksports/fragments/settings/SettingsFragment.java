package com.udc.master.tfm.tracksports.fragments.settings;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.utils.StringUtils;
import com.udc.master.tfm.tracksports.utils.preferences.MapType;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Fragmento con la configuracion general de la aplicacion
 * @author a.oteroc
 *
 */
public class SettingsFragment extends Fragment {

	/** Selectores de las configuraciones */
	private Switch soundSwitch;
	private Switch vibrationSwitch;
	private EditText notificationsCheckPendingExercises;
	private EditText notificationsCheckPendingExercisesClose;
	private EditText notificationsSendNotificationTime;
	private EditText mapUpdateFreqMetersEditText;
	private EditText mapUpdateFreqSecEditText;
	private SeekBar mapZoomSeekBar;
	private SeekBar mapPointsPerfectSeekBar;
	private Spinner mapTypeSpinner;
	private EditText clockCountdownEditText;
	private Switch clockShowMilisSwith;
	private EditText syncUrlEditText;
	private EditText syncTimeoutEditText;
	private Switch syncHttpsSwitch;
	private Switch syncCrypPassSwitch;
	private Switch syncAutoSwitch;
	private EditText googleFitTimeoutEditText;
	private Switch googleFitSyncSwitch;
	private Button buttonSavePreferences;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);
		
		/*
		 * Se inicializan los valores de las preferencias con lo almacenado en BBDD
		 */
		soundSwitch = (Switch) settingsView.findViewById(R.id.switch_sound);
		vibrationSwitch = (Switch) settingsView.findViewById(R.id.switch_vibrate);
		notificationsCheckPendingExercises = (EditText) settingsView.findViewById(R.id.editText_settings_notifications_check_pending_exercises);
		notificationsCheckPendingExercisesClose = (EditText) settingsView.findViewById(R.id.editText_settings_notifications_check_pending_exercises_close);
		notificationsSendNotificationTime = (EditText) settingsView.findViewById(R.id.editText_settings_notifications_send_notification_time);
		mapUpdateFreqMetersEditText = (EditText) settingsView.findViewById(R.id.editText_map_update_freq_meters);
		mapUpdateFreqSecEditText = (EditText) settingsView.findViewById(R.id.editText_map_update_freq_seconds);
		mapZoomSeekBar = (SeekBar) settingsView.findViewById(R.id.seekBar_map_default_zoom);
		mapPointsPerfectSeekBar = (SeekBar) settingsView.findViewById(R.id.seekBar_map_save_points);
		mapTypeSpinner = (Spinner)settingsView.findViewById(R.id.spinner_map_type);
		clockCountdownEditText = (EditText) settingsView.findViewById(R.id.editText_clock_countdown);
		clockShowMilisSwith = (Switch) settingsView.findViewById(R.id.switch_clock_miliseconds);
		syncUrlEditText = (EditText) settingsView.findViewById(R.id.editText_sync_url);
		syncTimeoutEditText = (EditText) settingsView.findViewById(R.id.editText_sync_timeout);
		syncHttpsSwitch = (Switch) settingsView.findViewById(R.id.switch_sync_https);
		syncCrypPassSwitch = (Switch) settingsView.findViewById(R.id.switch_sync_crypt_pass);
		syncAutoSwitch = (Switch) settingsView.findViewById(R.id.switch_sync_auto);
		googleFitTimeoutEditText = (EditText) settingsView.findViewById(R.id.editText_google_fit_timeout);
		googleFitSyncSwitch = (Switch) settingsView.findViewById(R.id.switch_google_fit_sync);
		
		Boolean isSoundActive = PreferencesUtils.getPreferences(PreferencesTypes.IS_SOUND_ACTIVE, Boolean.class, getActivity());
		if (isSoundActive != null) {
			soundSwitch.setChecked(isSoundActive);
		}
		Boolean isVibrationActive = PreferencesUtils.getPreferences(PreferencesTypes.IS_VIBRATION_ACTIVE, Boolean.class, getActivity());
		if (isVibrationActive != null) {
			vibrationSwitch.setChecked(isVibrationActive);
		}
		Integer checkPendingExerciseHours = PreferencesUtils.getPreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_HOURS, Integer.class, getActivity());
		if (checkPendingExerciseHours != null) {
			notificationsCheckPendingExercises.setText(checkPendingExerciseHours.toString());
		}
		Integer checkPendingExerciseCloseMinutes = PreferencesUtils.getPreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_CLOSE_MINUTES, Integer.class, getActivity());
		if (checkPendingExerciseCloseMinutes != null) {
			notificationsCheckPendingExercisesClose.setText(checkPendingExerciseCloseMinutes.toString());
		}
		Integer sendTimeNotificationMinutes = PreferencesUtils.getPreferences(PreferencesTypes.NOTIFICATION_SEND_TIME_MINUTES, Integer.class, getActivity());
		if (sendTimeNotificationMinutes != null) {
			notificationsSendNotificationTime.setText(sendTimeNotificationMinutes.toString());
		}
		Integer updateFreqMeters = PreferencesUtils.getPreferences(PreferencesTypes.MAP_UPDATE_FREQ_METERS, Integer.class, getActivity());
		if (updateFreqMeters != null) {
			mapUpdateFreqMetersEditText.setText(updateFreqMeters.toString());
		}
		Integer updateFreqSeconds = PreferencesUtils.getPreferences(PreferencesTypes.MAP_UPDATE_FREQ_SEC, Integer.class, getActivity());
		if (updateFreqSeconds != null) {
			mapUpdateFreqSecEditText.setText(updateFreqSeconds.toString());
		}
		Integer mapZoom = PreferencesUtils.getPreferences(PreferencesTypes.MAP_ZOOM, Integer.class, getActivity());
		if (mapZoom != null) {
			if (mapZoom.intValue() > mapZoomSeekBar.getMax()) {
				mapZoomSeekBar.setProgress(mapZoomSeekBar.getMax());
			} else {
				mapZoomSeekBar.setProgress(mapZoom.intValue());
			}
		}
		Integer savePointsPercent = PreferencesUtils.getPreferences(PreferencesTypes.MAP_SAVE_POINTS, Integer.class, getActivity());
		if (savePointsPercent != null) {
			if (savePointsPercent.intValue() > mapPointsPerfectSeekBar.getMax()) {
				mapPointsPerfectSeekBar.setProgress(mapPointsPerfectSeekBar.getMax());
			} else {
				mapPointsPerfectSeekBar.setProgress(savePointsPercent.intValue());
			}
		}
		Short mapTypeId = PreferencesUtils.getPreferences(PreferencesTypes.MAP_TYPE, Short.class, getActivity());
		MapType mapType = null;
		if (mapTypeId != null) {
			mapType = MapType.valueOf(mapTypeId);
		}
		if (mapType != null) {
			mapTypeSpinner.setSelection(mapType.getId());
		}
		Integer clockCountdown = PreferencesUtils.getPreferences(PreferencesTypes.CLOCK_COUNTDOWN, Integer.class, getActivity());
		if (clockCountdown != null) {
			clockCountdownEditText.setText(clockCountdown.toString());
		}
		Boolean clockShowMilis = PreferencesUtils.getPreferences(PreferencesTypes.CLOCK_SHOW_MILISECONDS, Boolean.class, getActivity());
		if (clockShowMilis != null) {
			clockShowMilisSwith.setChecked(clockShowMilis);
		}
		String syncUrl = PreferencesUtils.getPreferences(PreferencesTypes.SYNC_URL, String.class, getActivity());
		if (syncUrl != null && !StringUtils.isBlank(syncUrl)) {
			try {
				syncUrlEditText.setText(URLDecoder.decode(syncUrl, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		Integer syncTimeout = PreferencesUtils.getPreferences(PreferencesTypes.SYNC_TIMEOUT, Integer.class, getActivity());
		if (syncTimeout != null) {
			syncTimeoutEditText.setText(syncTimeout.toString());
		}
		Boolean syncHttps = PreferencesUtils.getPreferences(PreferencesTypes.SYNC_HTTPS, Boolean.class, getActivity());
		if (syncHttps != null) {
			syncHttpsSwitch.setChecked(syncHttps);
		}
		Boolean syncCryptPass = PreferencesUtils.getPreferences(PreferencesTypes.SYNC_CRYPT_PASS, Boolean.class, getActivity());
		if (syncCryptPass != null) {
			syncCrypPassSwitch.setChecked(syncCryptPass);
		}
		Boolean syncAuto = PreferencesUtils.getPreferences(PreferencesTypes.SYNC_AUTO, Boolean.class, getActivity());
		if (syncAuto != null) {
			syncAutoSwitch.setChecked(syncAuto);
		}
		Integer googleFitTimeout = PreferencesUtils.getPreferences(PreferencesTypes.GOOGLE_FIT_TIMEOUT, Integer.class, getActivity());
		if (googleFitTimeout != null) {
			googleFitTimeoutEditText.setText(googleFitTimeout.toString());
		}
		Boolean googleFitSync = PreferencesUtils.getPreferences(PreferencesTypes.GOOGLE_FIT_SYNC, Boolean.class, getActivity());
		if (googleFitSync != null) {
			googleFitSyncSwitch.setChecked(googleFitSync);
		}
		
		/*
		 * Al darle a guardar se almacenan en preferencias los valores que habian configurados en ese momento
		 */
		buttonSavePreferences = (Button) settingsView.findViewById(R.id.button_save_preferences);
		buttonSavePreferences.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PreferencesUtils.savePreferences(PreferencesTypes.IS_SOUND_ACTIVE, soundSwitch.isChecked(), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.IS_VIBRATION_ACTIVE, vibrationSwitch.isChecked(), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_HOURS, Integer.valueOf(notificationsCheckPendingExercises.getText().toString()), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_CLOSE_MINUTES, Integer.valueOf(notificationsCheckPendingExercisesClose.getText().toString()), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.NOTIFICATION_SEND_TIME_MINUTES, Integer.valueOf(notificationsSendNotificationTime.getText().toString()), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.MAP_UPDATE_FREQ_METERS, Long.valueOf(mapUpdateFreqMetersEditText.getText().toString()), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.MAP_UPDATE_FREQ_SEC, Long.valueOf(mapUpdateFreqSecEditText.getText().toString()), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.MAP_ZOOM, Integer.valueOf(mapZoomSeekBar.getProgress()).longValue(), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.MAP_SAVE_POINTS, mapPointsPerfectSeekBar.getProgress(), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.MAP_TYPE, MapType.valueOf(Integer.valueOf(mapTypeSpinner.getSelectedItemPosition()).shortValue()).getId(), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.CLOCK_COUNTDOWN, Integer.valueOf(clockCountdownEditText.getText().toString()), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.CLOCK_SHOW_MILISECONDS, clockShowMilisSwith.isChecked(), getActivity());
				try {
					PreferencesUtils.savePreferences(PreferencesTypes.SYNC_URL, URLEncoder.encode(syncUrlEditText.getText().toString(), "UTF-8"), getActivity());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				PreferencesUtils.savePreferences(PreferencesTypes.SYNC_TIMEOUT, Integer.valueOf(syncTimeoutEditText.getText().toString()), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.SYNC_HTTPS, syncHttpsSwitch.isChecked(), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.SYNC_CRYPT_PASS, syncCrypPassSwitch.isChecked(), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.SYNC_AUTO, syncAutoSwitch.isChecked(), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.GOOGLE_FIT_TIMEOUT, Integer.valueOf(googleFitTimeoutEditText.getText().toString()), getActivity());
				PreferencesUtils.savePreferences(PreferencesTypes.GOOGLE_FIT_SYNC, googleFitSyncSwitch.isChecked(), getActivity());
				
				Toast toast = Toast.makeText(getActivity(), R.string.settings_save_preferences_success, Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		
		return settingsView;
	}
}
