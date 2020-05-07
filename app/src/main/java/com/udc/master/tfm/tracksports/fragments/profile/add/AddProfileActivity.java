package com.udc.master.tfm.tracksports.fragments.profile.add;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.udc.master.tfm.tracksports.R;

/**
 * Clase que representa la actividad para dar de alta o editar un perfil
 * @author a.oteroc
 *
 */
public class AddProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_profile);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new LoginFragment()).commit();
		}
	}
}
