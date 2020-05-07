package com.udc.master.tfm.tracksports.directcom.fitness;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;

/**
 * Clase asincrona que configura el cliente para la conexion de Google FIT
 * e inserta un ejercicio
 * @author a.oteroc
 *
 */
public class AsyncGoogleFitExercise extends AsyncTask<String, Void, Boolean> {
	/** Contexto de la aplicacion */
	private Context context;
	/** Ejercicio que se sincroniza */
	private Exercise exercise;
	/** Cliente para la conexion con el API de Google FIT */
	private GoogleApiClient mClient;
	
	/**
	 * Constructor de la clase
	 * @param context
	 * @param exercise
	 * //@param mClient
	 */
	public AsyncGoogleFitExercise(Context context, Exercise exercise) {
		this.context = context;
		this.exercise = exercise;
	}

    @Override
    protected Boolean doInBackground(final String... args) {
    	Log.i("Fitness", "Building Client...");
    	mClient = buildFitnessClient();
    	Log.i("Fitness", "Connecting...");
    	mClient.connect();
    	return true;
    }
    
	/**
	 *  Build a {@link GoogleApiClient} that will authenticate the user and allow the application
	 *  to connect to Fitness APIs. The scopes included should match the scopes your app needs
	 *  (see documentation for details). Authentication will occasionally fail intentionally,
	 *  and in those cases, there will be a known resolution, which the OnConnectionFailedListener()
	 *  can address. Examples of this include the user never having signed in before, or having
	 *  multiple accounts on the device and needing to specify which account to use, etc.
	 */
	private GoogleApiClient buildFitnessClient() {
	    // Create the Google API Client
	    return new GoogleApiClient.Builder(context)
	            //.addApi(Fitness.API)
	            .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
	            .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
	            .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
	            .addConnectionCallbacks(
	                    new GoogleApiClient.ConnectionCallbacks() {

	                        @Override
	                        public void onConnected(Bundle bundle) {
	                            Log.i("Fitness", "Connected!!!");
	                            AsyncInsertGoogleFitSession asyncInsertGoogleFitSession = new AsyncInsertGoogleFitSession(context, exercise, mClient);
	                            asyncInsertGoogleFitSession.execute();
	                        }

	                        @Override
	                        public void onConnectionSuspended(int i) {
	                            // If your connection to the sensor gets lost at some point,
	                            // you'll be able to determine the reason and react to it here.
	                            if (i == ConnectionCallbacks.CAUSE_NETWORK_LOST) {
	                                Log.i("Fitness", "Connection lost.  Cause: Network Lost.");
	                            } else if (i == ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
	                                Log.i("Fitness", "Connection lost.  Reason: Service Disconnected");
	                            }
	                        }
	                    }
	            )
	            .addOnConnectionFailedListener(
	                    new GoogleApiClient.OnConnectionFailedListener() {
	                        // Called whenever the API client fails to connect.
	                        @Override
	                        public void onConnectionFailed(ConnectionResult result) {
	                            Log.i("Fitness", "Connection failed. Cause: " + result.toString());
	                            try {
									result.startResolutionForResult((Activity)context, result.getErrorCode());
								} catch (SendIntentException e) {
									Log.e("Fitness", "Connection failed. Cause: " + e.getMessage(), e);
								}
	                        }
	                    }
	            )
	            .build();
	}
}
