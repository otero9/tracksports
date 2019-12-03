package com.udc.master.tfm.tracksports.bbdd.factory;

import android.content.Context;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExerciseDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.profiles.ProfileDAO;
import com.udc.master.tfm.tracksports.bbdd.profiles.ProfileDAOImpl;

/**
 * Factoria concreta que obtiene las interfaces para trabajar con la BBDD
 * @author a.oteroc
 *
 */
public class DatabaseFactory extends DatabaseAbstractFactory {

	/** Unica instancia de la factoria */
	private static DatabaseFactory instance = null;
	
	/** Metodo que crea la factoria */
	private synchronized static void createInstance() {
		if (instance == null) {
			instance = new DatabaseFactory();
		}
	}
	
	/**
	 * Metodo que obtiene la factoria
	 * @return
	 */
	public static DatabaseFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}
	
	@Override
	public ExerciseDAO getExerciseDAO(Context context) {
		return new ExerciseDAOImpl(context);
	}

	@Override
	public PendingExerciseDAO getPendingExerciseDAO(Context context) {
		return new PendingExerciseDAOImpl(context);
	}

	@Override
	public ProfileDAO getProfileDAO(Context context) {
		return new ProfileDAOImpl(context);
	}
}
