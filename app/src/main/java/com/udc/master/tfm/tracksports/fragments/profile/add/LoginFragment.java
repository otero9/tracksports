package com.udc.master.tfm.tracksports.fragments.profile.add;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.bbdd.profiles.ProfileDAO;
import com.udc.master.tfm.tracksports.directcom.rest.async.AutenticationWSAsync;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.CryptoUtils;
import com.udc.master.tfm.tracksports.utils.StringUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Fragmento para recoger el login/contrasena para sincronizar
 * el usuario con la aplicacion Web
 * @author a.oteroc
 *
 */
public class LoginFragment extends AddProfileAbstractFragment {

	/** Texto del login */
	private EditText loginUserEditText;
	/** Texto de la contrasena */
	private EditText loginPasswordEditText;
	/** Texto a repetir de la contrasena */
	private EditText repeatLoginPasswordEditText;
	
	/**
	 * Constructor del fragmento
	 */
	public LoginFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View loginView = inflater.inflate(R.layout.fragment_login, container, false);
		
		setNextButton((ImageButton) loginView.findViewById(R.id.button_login_next));
		loginUserEditText = (EditText) loginView.findViewById(R.id.editText_login_user);
		loginPasswordEditText = (EditText) loginView.findViewById(R.id.editText_login_password);
		repeatLoginPasswordEditText = (EditText) loginView.findViewById(R.id.editText_repeat_password);
		
		loginPasswordEditText.setTypeface(Typeface.DEFAULT);
		repeatLoginPasswordEditText.setTypeface(Typeface.DEFAULT);
		
		fillParams();
		
		//Se navega al fragmento de los detalles de usuario
		getNextButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//Se permite navegar a la siguiente pagina si la validacion es satisfactoria
				if (validateForm()) {
					setParams();
					getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, new UserDetailsFragment()).commit();	
				}
			}
		});
		
		//Se valida el login del usuario
		//Solo sera valido si no es vacio y si no existe uno con el mismo login
		loginUserEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateLogin((TextView)v);
				}
			}
		});
		
		//Se valida la contrasena del usuario
		//Solo sera valida si no es vacia
		loginPasswordEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validatePassword((TextView)v);
				}
			}
		});
		
		//Se valida si coincide el campo con la contrasena
		repeatLoginPasswordEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateRepeatPassword((TextView)v);
				}
			}
		});
		
		return loginView;
	}
	
	protected void fillParams() {
		Profile profile = (Profile)getActivity().getIntent().getSerializableExtra(ConstantsUtils.PROFILE_PARAM);
		if (profile != null) {
			loginUserEditText.setText(profile.getUser());
			String pass = CryptoUtils.decrypt(profile.getEnPass());
			loginPasswordEditText.setText(pass);
			repeatLoginPasswordEditText.setText(pass);
		}
	}
	
	protected void setParams() {
		Profile profile = (Profile)getActivity().getIntent().getSerializableExtra(ConstantsUtils.PROFILE_PARAM);
		if (profile == null) {
			profile = new Profile();
		}
		profile.setUser(loginUserEditText.getText().toString());
		String enPass = CryptoUtils.encrypt(loginPasswordEditText.getText().toString());
		profile.setEnPass(enPass);
		
		//Se autentica contra la plataforma
		if (Boolean.TRUE.equals(PreferencesUtils.getPreferences(PreferencesTypes.SYNC_AUTO, Boolean.class, getActivity()))) {
			AutenticationWSAsync autenticationWSAsync = new AutenticationWSAsync(getActivity(), profile);
			autenticationWSAsync.execute();
		}
		
		getActivity().getIntent().putExtra(ConstantsUtils.PROFILE_PARAM, profile);
	}
	
	/**
	 * Metodo que valida todos los campos del formulario
	 * @return
	 */
	private boolean validateForm() {
		boolean validateLogin = true;
		boolean validatePass = true;
		boolean validateRepeatPass = true;
		if (loginUserEditText != null) {
			validateLogin = validateLogin(loginUserEditText);
		}
		if (loginPasswordEditText != null) {
			validatePass = validatePassword(loginPasswordEditText);
		}
		if (repeatLoginPasswordEditText != null) {
			validateRepeatPass = validateRepeatPassword(repeatLoginPasswordEditText);
		}
		return validateLogin && validatePass && validateRepeatPass;
	}
	
	/**
	 * Se valida si el login es correcto
	 * @param t
	 * @return
	 */
	private boolean validateLogin(TextView t) {
		if (StringUtils.isBlank(t.getText())) {
			t.setError(getString(R.string.validate_login_not_valid));
			return false;
		} else {
			boolean editProfile = getActivity().getIntent().getExtras().getBoolean(ConstantsUtils.EDIT_PROFILE_PARAM, false);
			if (!editProfile) {
				ProfileDAO profileHelper = DatabaseFactory.getInstance().getProfileDAO(getActivity());
				boolean existsUser = profileHelper.existsUser(t.getText().toString());
				if (existsUser) {
					t.setError(getString(R.string.validate_login_already_exists));
					return false;
				} else {
					t.setError(null);
					return true;
				}
			}
			t.setError(null);
			return true;
		}
	}
	
	/**
	 * Se valida si la contrasena es correcta
	 * @param t
	 * @return
	 */
	private boolean validatePassword(TextView t) {
		if (StringUtils.isBlank(t.getText())) {
			t.setError(getString(R.string.validate_password_not_valid));
			return false;
		} else {
			t.setError(null);
			return true;
		}
	}
	
	/**
	 * Se valida si coinciden las contrasenas
	 * @param t
	 * @return
	 */
	private boolean validateRepeatPassword(TextView t) {
		TextView compareTextView = (TextView)loginPasswordEditText;
		if (StringUtils.isBlank(t.getText())) {
			t.setError(getString(R.string.validate_password_not_valid));
			return false;
		} else if (!t.getText().toString().equals(compareTextView.getText().toString())) {
			t.setError(getString(R.string.validate_password_not_match));
			return false;
		} else {
			t.setError(null);
			return true;
		}
	}
}
