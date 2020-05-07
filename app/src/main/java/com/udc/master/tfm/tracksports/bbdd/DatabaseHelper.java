package com.udc.master.tfm.tracksports.bbdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.activity.mapposition.ExerciseMapPositionDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPositionDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExerciseDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.profiles.ProfileDAOImpl;

/**
 * Clase que gestiona la conexion con la BBDD
 * @author a.oteroc
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 * Constructor de la clase
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, BDConstants.BBDD_NAME, null, BDConstants.BBDD_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ProfileDAOImpl.getCreateSQL());
		db.execSQL(MapPositionDAOImpl.getCreateSQL());
		db.execSQL(ExerciseDAOImpl.getCreateSQL());
		db.execSQL(ExerciseMapPositionDAOImpl.getCreateSQL());
		db.execSQL(PendingExerciseDAOImpl.getCreateSQL());
	}
	
	/**
	 * Metodo llamada cuando se eliminan las tablas de BBDD
	 * @param db
	 */
	private void onDelete(SQLiteDatabase db) {
		db.execSQL(ProfileDAOImpl.getDeleteSQL());
		db.execSQL(MapPositionDAOImpl.getDeleteSQL());
		db.execSQL(ExerciseDAOImpl.getDeleteSQL());
		db.execSQL(ExerciseMapPositionDAOImpl.getDeleteSQL());
		db.execSQL(PendingExerciseDAOImpl.getDeleteSQL());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.onDelete(db);
		this.onCreate(db);
	}
}
