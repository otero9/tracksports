package com.udc.master.tfm.tracksports.directcom.fitness;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionInsertRequest;

import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

/**
 * Clase asincrona que sincroniza un ejercicio con Google FIT
 * @author a.oteroc
 *
 */
public class AsyncInsertGoogleFitSession extends AsyncTask<String, Void, Boolean> {

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
	 * @param mClient
	 */
	public AsyncInsertGoogleFitSession(Context context, Exercise exercise, GoogleApiClient mClient) {
		this.context = context;
		this.exercise = exercise;
		this.mClient = mClient;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		return insertSession();
	}

    @Override
    protected void onPostExecute(final Boolean success) {
    	if (mClient != null && mClient.isConnected()) {
    		Log.i("Fitness", "Disconnecting...");
    		mClient.disconnect();
    	}
    }
    
	private boolean insertSession() {
		Long startTime = exercise.getStartTime().getTime();
		Long endTime = startTime + exercise.getDuration();
		
		// Create a session with metadata about the activity.
		Session session = new Session.Builder()
		        .setName(context.getString(R.string.session_identifier, exercise.getId().toString()))
		        .setDescription(context.getString(R.string.session_identifier, exercise.getId().toString()))
		        .setIdentifier(exercise.getId().toString())
		        .setActivity(FitnessActivities.RUNNING)
		        .setStartTime(startTime, TimeUnit.MILLISECONDS)
		        .setEndTime(endTime, TimeUnit.MILLISECONDS)
		        .build();

		DataSource dataSourceSteps = new DataSource.Builder()
			.setAppPackageName(context)
			.setDataType(DataType.TYPE_STEP_COUNT_DELTA)
			.setName(context.getString(R.string.session_steps))
			.setType(DataSource.TYPE_RAW)
			.build();
		
		DataSource dataSourceCalories = new DataSource.Builder()
		.setAppPackageName(context)
		.setDataType(DataType.TYPE_CALORIES_EXPENDED)
		.setName(context.getString(R.string.session_calories))
		.setType(DataSource.TYPE_RAW)
		.build();
		
		DataSource dataSourceDistance = new DataSource.Builder()
		.setAppPackageName(context)
		.setDataType(DataType.TYPE_DISTANCE_DELTA)
		.setName(context.getString(R.string.session_distance))
		.setType(DataSource.TYPE_RAW)
		.build();
		
		/*DataSource dataSourceLocation = new DataSource.Builder()
		.setAppPackageName(context)
		.setDataType(DataType.TYPE_LOCATION_SAMPLE)
		.setName(context.getString(R.string.session_location))
		.setType(DataSource.TYPE_RAW)
		.build();*/
		
		DataSet dataSetSteps = DataSet.create(dataSourceSteps);
		DataPoint dataPointSteps = dataSetSteps.createDataPoint()
		        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
		dataPointSteps.getValue(Field.FIELD_STEPS).setInt(exercise.getSteps().intValue());
		dataSetSteps.add(dataPointSteps);
		
		DataSet dataSetCalories = DataSet.create(dataSourceCalories);
		DataPoint dataPointCalories = dataSetCalories.createDataPoint()
		        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
		dataPointCalories.getValue(Field.FIELD_CALORIES).setFloat(exercise.getCaloriesBurned().floatValue());
		dataSetCalories.add(dataPointCalories);
		
		DataSet dataSetDistance = DataSet.create(dataSourceDistance);
		DataPoint dataPointDistance = dataSetDistance.createDataPoint()
		        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
		dataPointDistance.getValue(Field.FIELD_DISTANCE).setFloat(exercise.getDistance().floatValue());
		dataSetDistance.add(dataPointDistance);
			
		/*DataSet dataSetLocation = DataSet.create(dataSourceLocation);
		if (exercise.getRoute() != null && !exercise.getRoute().isEmpty()) {
			Long startPositionTime = startTime;
			Long endPositionTime = null;
			for (MapPosition mapPosition : exercise.getRoute()) {
				endPositionTime = mapPosition.getTime();
				DataPoint dataPointLocation = dataSetLocation.createDataPoint()
				        .setTimeInterval(startPositionTime, endPositionTime, TimeUnit.MILLISECONDS);
				dataPointLocation.getValue(Field.FIELD_ALTITUDE).setFloat(mapPosition.getAltitude().floatValue());
				dataPointLocation.getValue(Field.FIELD_LONGITUDE).setFloat(mapPosition.getLongitude().floatValue());
				dataPointLocation.getValue(Field.FIELD_LATITUDE).setFloat(mapPosition.getLatitude().floatValue());
				dataPointLocation.getValue(Field.FIELD_SPEED).setFloat(mapPosition.getSpeed().floatValue());
				dataPointLocation.getValue(Field.FIELD_DISTANCE).setFloat(mapPosition.getDistance().floatValue());
				dataPointLocation.getValue(Field.FIELD_DURATION).setInt(mapPosition.getTime().intValue());
				dataSetLocation.add(dataPointLocation);
				startPositionTime = endPositionTime;
			}
		}*/
		
		// Build a session insert request
		SessionInsertRequest.Builder insertRequestBuilder = new SessionInsertRequest.Builder()
		        .setSession(session)
		        .addDataSet(dataSetSteps)
		        .addDataSet(dataSetCalories)
		        .addDataSet(dataSetDistance);
		
		/*if (exercise.getRoute() != null && !exercise.getRoute().isEmpty()) {
			insertRequestBuilder.addDataSet(dataSetLocation);
		}*/
		
		SessionInsertRequest insertRequest = insertRequestBuilder.build();
		
		// Then, invoke the Sessions API to insert the session and await the result,
		// which is possible here because of the AsyncTask. Always include a timeout when
		// calling await() to avoid hanging that can occur from the service being shutdown
		// because of low memory or other conditions.
		Log.i("Fitness", "Inserting the session in the History API");
		Integer timeout = PreferencesUtils.getPreferences(PreferencesTypes.GOOGLE_FIT_TIMEOUT, Integer.class, context);
		com.google.android.gms.common.api.Status insertStatus =
		        Fitness.SessionsApi.insertSession(mClient, insertRequest)
		                .await(timeout, TimeUnit.SECONDS);

		// Before querying the session, check to see if the insertion succeeded.
		if (!insertStatus.isSuccess()) {
		    Log.i("Fitness", "There was a problem inserting the session: " +
		            insertStatus.getStatusMessage());
		    return false;
		}

		// At this point, the session has been inserted and can be read.
		Log.i("Fitness", "Session insert was successful!");
		return true;
	}
}
