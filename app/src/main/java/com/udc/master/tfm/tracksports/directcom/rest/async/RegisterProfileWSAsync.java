package com.udc.master.tfm.tracksports.directcom.rest.async;

import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.bbdd.profiles.ProfileDAO;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.directcom.rest.HttpUtils;
import com.udc.master.tfm.tracksports.directcom.rest.WebCommunicationService;
import com.udc.master.tfm.tracksports.directcom.rest.factory.WebServiceFactory;
import com.udc.master.tfm.tracksports.directcom.rest.object.RegistrationResponseWS;
import com.udc.master.tfm.tracksports.fragments.profile.ProfileFragment;

/**
 * Clase asincrona encargada de registrarse contra el servicio web
 * @author a.oteroc
 *
 */
public class RegisterProfileWSAsync extends AsyncTask<String, Void, HttpStatusType> {
	/** Contexto de la aplicacion */
	private Context context;
	/** Dialogo de carga al guardar el ejercicio */
	private ProgressDialog progress;
	/** Usuario que se autentica */
	private Profile profile;
	/** Indica si es necesario cerrar la actividad actual */
	private boolean syncronization;
	
	/**
	 * Constructor del comando
	 * @param context
	 * @param profile
	 */
	public RegisterProfileWSAsync(Context context, Profile profile, boolean syncronization) {
		this.context = context;
		this.profile = profile;
		this.syncronization = syncronization;
	}
	
    @Override
    protected void onPreExecute() {
		progress = new ProgressDialog(context);
		if (syncronization) {
//			progress.setTitle(context.getString(R.string.progress_dialog_sync_profile_title));
			progress.setMessage(context.getString(R.string.progress_dialog_sync_profile_content));
		} else {
//			progress.setTitle(context.getString(R.string.progress_dialog_registration_title));
			progress.setMessage(context.getString(R.string.progress_dialog_registration_content));
		}

		progress.setCancelable(false);
		progress.show();
    }
	
	@Override
	protected HttpStatusType doInBackground(String... params) {
        HttpStatusType status = HttpStatusType.REGISTRATION_OK;
		try {	
			//Se realiza la peticion contra el servicio Web
			WebCommunicationService service = WebServiceFactory.getInstance().getWebCommunicationService(context);
			RegistrationResponseWS response = service.register(profile.getUser(), profile.getEnPass(), profile.getRegistrationRequest());

			//Se obtiene el codigo de error recibido
        	if (HttpUtils.GUID_NOT_VALID.equals(response.getIdUsuario()) || response.getCodigoError() != null) {
        		HttpStatusType statusRev = null;
        		if (response.getCodigoError() != null) {
        			statusRev = HttpStatusType.valueOf(response.getCodigoError());
        		}
        		if (statusRev != null) {
        			status = statusRev;
        		} else {
        			status = HttpStatusType.REGISTRATION_KO;
        		}
        		
        	} else {
        		profile.setGuid(response.getIdUsuario());
        	}
		} catch (ConnectTimeoutException cte) {
			status = HttpStatusType.CONNECTION_TIMEOUT;
        } catch(Exception ex) {
        	Log.e("WebService", "Error en la comunicacion", ex);
        	status = HttpStatusType.REGISTRATION_KO;
        }

        return status;
	}
	
    @Override
    protected void onPostExecute(final HttpStatusType status) {
    	if (progress.isShowing()) {
        	progress.dismiss();
        }
    	
    	///Si el registro es de una sincronizacion se modifica el codigo
    	HttpStatusType finishStatus = status;
		if (syncronization) {
			if (HttpStatusType.STATUSES_OK.contains(status)) {
				finishStatus = HttpStatusType.SYNC_EXERCISE_OK;
			} else {
				finishStatus = HttpStatusType.SYNC_EXERCISE_KO;
			}
		}
    	
    	//Se actualiza el Guid del usuario
    	profile.setSyncStatus(finishStatus);
    	ProfileDAO profileHelper = DatabaseFactory.getInstance().getProfileDAO(context);
		profileHelper.update(profile);
		
		//Se muestra un mensaje informativo
		Toast toast = Toast.makeText(context, finishStatus.getTextId(), Toast.LENGTH_SHORT);
		toast.show();
		
		//Se refrescan la lista de perfiles
		if (syncronization) {
	    	List<Fragment> fragments = ((FragmentActivity)context).getSupportFragmentManager().getFragments();
	    	for (Fragment fragment : fragments) {
				if (fragment instanceof ProfileFragment)  {
					ProfileFragment profileFragment = (ProfileFragment) fragment;
					profileFragment.refreshProfiles();
					return;
				}
			}
		} else {
			//Se finaliza la actividad actual
			((FragmentActivity)context).finish();
		}
    }
}
