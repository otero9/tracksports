package com.udc.master.tfm.tracksports.fragments.profile.add;

import java.text.ParseException;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.profiles.Gender;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.DateUtils;
import com.udc.master.tfm.tracksports.utils.StringUtils;

/**
 * Fragmento para recoger los datos del usuario y monitorizar
 * su actividad fisica
 * @author a.oteroc
 *
 */
public class UserDetailsFragment extends AddProfileAbstractFragment {

	/** Texto con el nombre del usuario */
	private EditText nameEditText;
	/** Texto con la fecha de nacimiento del usuario */
	private EditText birthdayEditText;
	/** Boton que abre el dialogo para seleccionar la fecha de nacimiento */
	private ImageButton birthdayPickerButton;
	/** Texto con la altura del usuario */
	private EditText heightEditText;
	/** Texto con el peso del usuario */
	private EditText weightEditText;
	/** Selector del sexo del usuario */
	private Spinner genderSpinner;
	/** Texto con la longitud del paso del usuario */
	private EditText stepLengthText;
	
	/**
	 * Constructor del fragmento
	 */
	public UserDetailsFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View userDetailsView = inflater.inflate(R.layout.fragment_user_details,
				container, false);
		
		setPreviousButton((ImageButton) userDetailsView.findViewById(R.id.button_user_details_previous));
		setNextButton((ImageButton) userDetailsView.findViewById(R.id.button_user_details_next));
		nameEditText = (EditText) userDetailsView.findViewById(R.id.editText_details_name);
		birthdayEditText = (EditText) userDetailsView.findViewById(R.id.editText_details_birthday);
		heightEditText = (EditText) userDetailsView.findViewById(R.id.editText_details_height);
		weightEditText = (EditText) userDetailsView.findViewById(R.id.editText_details_weight);
		birthdayPickerButton = (ImageButton) userDetailsView.findViewById(R.id.button_select_birthday);
		genderSpinner = (Spinner) userDetailsView.findViewById(R.id.spinner_gender);
		stepLengthText = (EditText) userDetailsView.findViewById(R.id.editText_details_step_length);
		
		//Se evita poder editar la fecha de forma manual
		birthdayEditText.setKeyListener(null);
		
		fillParams();
		
		//Se navega al fragmento de Login
		getPreviousButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setParams();
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, new LoginFragment()).commit();	
			}
		});
		
		//Se navega al fragmento de introducir la imagen de perfil
		getNextButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//Se permite navegar a la siguiente pagina si la validacion es satisfactoria
				if (validateForm()) {
					setParams();
					getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, new ImageSelectFragment()).commit();
				}
			}
		});
		
		//Se muestra el dialogo para seleccionar la fecha de nacimiento
		birthdayPickerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Se inicializa la fecha con la especificada en el texto.
				//En caso de no especificar ninguna se inicializa con la fecha actual
				int [] date = null;
				Editable editableText = birthdayEditText.getText();
				String birthdayText = editableText.toString();
				if (birthdayText != null) {
					try {
						Date birthday = DateUtils.getDateFormatter().parse(birthdayText);
						date = DateUtils.getDate(birthday);
					} catch (ParseException e) {
						birthdayEditText.setError(getString(R.string.validate_birthday_invalid_value));
					}
				}
				if (date == null) {
					date = DateUtils.getCurrentDate();
				}
				
				DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						//Los meses empiezan por 0
						birthdayEditText.setText(DateUtils.getStringDate(dayOfMonth, monthOfYear+1, year));
						validateBirthday(birthdayEditText);
					}
				}, date[2], date[1], date[0]);
				datePicker.show();
			}
		});
		
		//Se valida el nombre
		nameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateName((TextView)v);
				}
			}
		});
		
		//Se valida la fecha de nacimiento
		birthdayEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateBirthday((TextView)v);
				}
			}
		});
		
		//Se valida la altura
		heightEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateHeight((TextView)v);
				}
			}
		});
		
		//Se valida el peso
		weightEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateWeight((TextView)v);
				}
			}
		});
		
		//Se valida la longitud del paso
		stepLengthText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateStepLength((TextView)v);
				}
			}
		});
		
		return userDetailsView;
	}
	
	protected void fillParams() {
		Profile profile = (Profile)getActivity().getIntent().getSerializableExtra(ConstantsUtils.PROFILE_PARAM);
		if (profile != null) {
			if (profile.getName() != null) {
				nameEditText.setText(profile.getName());
			}
			if (profile.getHeight() != null) {
				heightEditText.setText(profile.getHeight().toString());
			}
			if (profile.getWeight() != null) {
				weightEditText.setText(profile.getWeight().toString());
			}
			if (profile.getBirthday() != null) {
				birthdayEditText.setText(profile.getTextBirthday());
			}
			if (profile.getGender() != null) {
				genderSpinner.setSelection(profile.getGender().getId());
			}
			if (profile.getStepLength() != null) {
				stepLengthText.setText(profile.getStepLength().toString());
			}
		}
	}
	
	protected void setParams() {
		Profile profile = (Profile)getActivity().getIntent().getSerializableExtra(ConstantsUtils.PROFILE_PARAM);
		try {
			if (birthdayEditText.getText() != null) {
				profile.setBirthday(DateUtils.getDateFormatter().parse(birthdayEditText.getText().toString()));
			}
		} catch (ParseException ignore) {}
		profile.setName(nameEditText.getText().toString());
		try {
			profile.setHeight(Short.valueOf(heightEditText.getText().toString()));
		} catch (NumberFormatException ignore) {}
		try {
			profile.setWeight(Short.valueOf(weightEditText.getText().toString()));
		} catch (NumberFormatException ignore) {}
		try {
			profile.setStepLength(Short.valueOf(stepLengthText.getText().toString()));
		} catch (NumberFormatException ignore) {}
		
		profile.setGender(Gender.valueOf(Integer.valueOf(genderSpinner.getSelectedItemPosition()).shortValue()));
	}
	
	/**
	 * Metodo que valida todos los campos del formulario
	 * @return
	 */
	private boolean validateForm() {
		boolean validateName = true;
		boolean validateBirthday = true;
		boolean validateHeight = true;
		boolean validateWeight = true;
		boolean validateStep = true;
		if (nameEditText != null) {
			validateName = validateName(nameEditText);
		}
		if (birthdayEditText != null) {
			validateBirthday = validateBirthday(birthdayEditText);
		}
		if (heightEditText != null) {
			validateHeight = validateHeight(heightEditText);
		}
		if (weightEditText != null) {
			validateWeight = validateWeight(weightEditText);
		}
		if (stepLengthText != null) {
			validateStep = validateStepLength(stepLengthText);
		}
		
		return validateName && validateBirthday && validateHeight && validateWeight && validateStep;
	}
	
	/**
	 * Se valida si el nombre es correcto
	 * @param t
	 * @return
	 */
	private boolean validateName(TextView t) {
		if (StringUtils.isBlank(t.getText())) {
			t.setError(getString(R.string.validate_name_not_valid));
			return false;
		} else {
			t.setError(null);
			return true;
		}
	}
	
	/**
	 * Se valida si la fecha de nacimiento es correcta
	 * @param t
	 * @return
	 */
	private boolean validateBirthday(TextView t) {
		if (StringUtils.isBlank(t.getText())) {
			t.setError(getString(R.string.validate_birthday_not_valid));
			return false;
		} else {
			t.setError(null);
			return true;
		}
	}
	
	/**
	 * Se valida si la altura es correcta
	 * @param t
	 * @return
	 */
	private boolean validateHeight(TextView t) {
		if (StringUtils.isBlank(t.getText())) {
			t.setError(getString(R.string.validate_height_not_valid));
			return false;
		} else {
			try {
				Integer heigth = Integer.valueOf(t.getText().toString());
				if (heigth < 0) {
					t.setError(getString(R.string.validate_height_invalid_value));
					return false;
				}
			} catch (Exception e) {
				t.setError(getString(R.string.validate_height_invalid_value));
				return false;
			}
			t.setError(null);
			return true;
		}
	}
	
	/**
	 * Se valida si el peso es correcto
	 * @param t
	 * @return
	 */
	private boolean validateWeight(TextView t) {
		if (StringUtils.isBlank(t.getText())) {
			t.setError(getString(R.string.validate_weight_not_valid));
			return false;
		} else {
			try {
				Integer weight = Integer.valueOf(t.getText().toString());
				if (weight < 0) {
					t.setError(getString(R.string.validate_weight_invalid_value));
					return false;
				}
			} catch (Exception e) {
				t.setError(getString(R.string.validate_weight_invalid_value));
				return false;
			}
			t.setError(null);
			return true;
		}
	}
	
	/**
	 * Se valida si el peso es correcto
	 * @param t
	 * @return
	 */
	private boolean validateStepLength(TextView t) {
		if (StringUtils.isBlank(t.getText())) {
			t.setError(getString(R.string.validate_step_not_valid));
			return false;
		} else {
			try {
				Integer stepLength = Integer.valueOf(t.getText().toString());
				if (stepLength < 0) {
					t.setError(getString(R.string.validate_step_invalid_value));
					return false;
				}
			} catch (Exception e) {
				t.setError(getString(R.string.validate_step_invalid_value));
				return false;
			}
			t.setError(null);
			return true;
		}
	}
}
