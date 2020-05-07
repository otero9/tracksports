package com.udc.master.tfm.tracksports.directcom.rest.async;

import org.apache.http.conn.ConnectTimeoutException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.directcom.rest.HttpUtils;
import com.udc.master.tfm.tracksports.directcom.rest.WebCommunicationService;
import com.udc.master.tfm.tracksports.directcom.rest.factory.WebServiceFactory;
import com.udc.master.tfm.tracksports.directcom.rest.object.AutenticationResponseWS;
import com.udc.master.tfm.tracksports.fragments.profile.add.UserDetailsFragment;

/**
 * Clase asincrona encargada de autenticarse contra el servicio web
 * @author a.oteroc
 *
 */
public class AutenticationWSAsync extends AsyncTask<String, Void, HttpStatusType> {
	/** Contexto de la aplicacion */
	private Context context;
	/** Dialogo de carga al guardar el ejercicio */
	private ProgressDialog progress;
	/** Usuario que se autentica */
	private Profile profile;
	
	/**
	 * Constructor del comando
	 * @param context
	 * @param profile
	 */
	public AutenticationWSAsync(Context context, Profile profile) {
		this.context = context;
		this.profile = profile;
	}
	
    @Override
    protected void onPreExecute() {
		progress = new ProgressDialog(context);
//		progress.setTitle(context.getString(R.string.progress_dialog_autentication_title));
		progress.setMessage(context.getString(R.string.progress_dialog_autentication_content));
		progress.setCancelable(false);
		progress.show();
    }
	
	@Override
	protected HttpStatusType doInBackground(String... args) {
        HttpStatusType status = HttpStatusType.AUTHENTICATION_OK;
		try {
			//Se realiza la peticion contra el servicio Web
			WebCommunicationService service = WebServiceFactory.getInstance().getWebCommunicationService(context);
			AutenticationResponseWS response = service.autenticate(profile.getUser(), profile.getEnPass());

        	//Se obtiene el codigo de error recibido
        	if (response == null || HttpUtils.GUID_NOT_VALID.equals(response.getIdUsuario()) || response.getCodigoError() != null) {
        		HttpStatusType statusRev = null;
        		if (response.getCodigoError() != null) {
        			statusRev = HttpStatusType.valueOf(response.getCodigoError());
        		}
        		if (statusRev != null) {
        			status = statusRev;
        		} else {
        			status = HttpStatusType.AUTHENTICATION_KO;
        		}
        		
        	} else {
        		profile.setAutenticationResponse(response);
        	}
		} catch (ConnectTimeoutException cte) {
			status = HttpStatusType.CONNECTION_TIMEOUT;
        } catch(Exception ex) {
        	Log.e("WebService", "Error en la comunicacion", ex);
        	status = HttpStatusType.AUTHENTICATION_KO;
        }

        return status;
	}

    @Override
    protected void onPostExecute(final HttpStatusType status) {
    	if (progress.isShowing()) {
        	progress.dismiss();
        }
        
    	//Si la autenticacion fue satisfactoria, se recarga la pagina de detalles de perfil con los datos del usuario
    	profile.setSyncStatus(status);
    	if (HttpStatusType.AUTHENTICATION_OK.equals(status)) {
			((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
			.replace(R.id.container, new UserDetailsFragment()).commit();
    	}
    	
		Toast toast = Toast.makeText(context, status.getTextId(), Toast.LENGTH_SHORT);
		toast.show();
    }
}
