package com.udc.master.tfm.tracksports.directcom.rest.async;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.directcom.rest.WebCommunicationService;
import com.udc.master.tfm.tracksports.directcom.rest.factory.WebServiceFactory;
import com.udc.master.tfm.tracksports.directcom.rest.object.ExerciseResponseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.ExerciseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.ProfileExerciseWS;
import com.udc.master.tfm.tracksports.fragments.diary.DiaryFragment;

/**
 * Clase asincrona encargada de editar un usuario contra el servicio web
 * @author a.oteroc
 *
 */
public class SaveExerciseWSAsync extends AsyncTask<String, Void, HttpStatusType> {
	/** Contexto de la aplicacion */
	private Context context;
	/** Dialogo de carga al guardar el ejercicio */
	private ProgressDialog progress;
	/** Usuario que realiza los ejercicios */
	private Profile profile;
	/** Lista de ejercicios a sincronizar */
	private List<Exercise> exercises;
	
	/**
	 * Constructor del comando
	 * @param context
	 * @param profile
	 * @param exercise
	 */
	public SaveExerciseWSAsync(Context context, Profile profile, Exercise exercise) {
		this.context = context;
		this.profile = profile;
		this.exercises = Arrays.asList(exercise);
	}
	
	/**
	 * Constructor del comando
	 * @param context
	 * @param profile
	 * @param exercises
	 */
	public SaveExerciseWSAsync(Context context, Profile profile, List<Exercise> exercises) {
		this.context = context;
		this.profile = profile;
		this.exercises = exercises;
	}
	
    @Override
    protected void onPreExecute() {
		progress = new ProgressDialog(context);
//		progress.setTitle(context.getString(R.string.progress_dialog_sync_exercise_title));
		if (exercises.size() > 1) {
			progress.setMessage(context.getString(R.string.progress_dialog_sync_exercises_content));
		} else {
			progress.setMessage(context.getString(R.string.progress_dialog_sync_exercise_content));
		}
		progress.setCancelable(false);
		progress.show();
    }
	
	@Override
	protected HttpStatusType doInBackground(String... params) {
        HttpStatusType status = HttpStatusType.SYNC_EXERCISE_OK;
        try {
			ProfileExerciseWS profileExerciseWS = new ProfileExerciseWS();
			profileExerciseWS.setIdUsuario(profile.getGuid());
			profileExerciseWS.setEjercicios(new ArrayList<ExerciseWS>());
			for (Exercise exercise : exercises) {
				profileExerciseWS.getEjercicios().add(exercise.getExerciseWS());
			}
		
			//Se realiza la peticion contra el servicio Web
			WebCommunicationService service = WebServiceFactory.getInstance().getWebCommunicationService(context);
			ExerciseResponseWS response = service.saveExercise(profile.getGuid(), profileExerciseWS);

        	//Se obtiene el codigo de error recibido
        	if (!Boolean.TRUE.equals(response.getResultado()) || response.getCodigoError() != null) {
        		HttpStatusType statusRev = null;
        		if (response.getCodigoError() != null) {
        			statusRev = HttpStatusType.valueOf(response.getCodigoError());
        		}
        		if (statusRev != null) {
        			status = statusRev;
        		} else {
        			status = HttpStatusType.SYNC_EXERCISE_KO;
        		}
        		
        	}
		} catch (ConnectTimeoutException cte) {
			status = HttpStatusType.CONNECTION_TIMEOUT;
        } catch(Exception ex) {
        	Log.e("WebService", "Error en la comunicacion", ex);
        	status = HttpStatusType.SYNC_EXERCISE_KO;
        }

        return status;
	}
	
    @Override
    protected void onPostExecute(final HttpStatusType status) {
    	if (progress.isShowing()) {
        	progress.dismiss();
        }
    	
    	ExerciseDAO exerciseHelper = DatabaseFactory.getInstance().getExerciseDAO(context);
    	for (Exercise exercise : exercises) {
    		exercise.setSyncStatus(status);
    		exerciseHelper.update(exercise);
		}
    	
		Toast toast = Toast.makeText(context, status.getTextId(), Toast.LENGTH_SHORT);
		toast.show();
		
		//Se refrescan la lista de ejercicios
    	List<Fragment> fragments = ((FragmentActivity)context).getSupportFragmentManager().getFragments();
    	for (Fragment fragment : fragments) {
			if (fragment instanceof DiaryFragment)  {
				DiaryFragment profileFragment = (DiaryFragment) fragment;
				profileFragment.refreshExercises();
				return;
			}
		}
    }
}
