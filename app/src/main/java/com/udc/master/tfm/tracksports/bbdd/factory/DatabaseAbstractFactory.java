package com.udc.master.tfm.tracksports.bbdd.factory;

import android.content.Context;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.profiles.ProfileDAO;

/**
 * Factoria abstracta para obtener los DAO de acceso a la BBDD
 * @author a.oteroc
 *
 */
public abstract class DatabaseAbstractFactory {

	/**
	 * Metodo que obtiene el DAO para el acceso a los ejercicios
	 * @param context
	 * @return
	 */
	public abstract ExerciseDAO getExerciseDAO(Context context);
	
	/**
	 * Metodo que obtiene el DAO para el acceso a los ejercicios pendientes
	 * @param context
	 * @return
	 */
	public abstract PendingExerciseDAO getPendingExerciseDAO(Context context);
	
	/**
	 * Metodo que obtiene el DAO para el acceso a los perfiles
	 * @param context
	 * @return
	 */
	public abstract ProfileDAO getProfileDAO(Context context);
}
