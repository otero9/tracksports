package com.udc.master.tfm.tracksports.bbdd.pendingactivity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.udc.master.tfm.tracksports.bbdd.DatabaseHelper;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.bbdd.profiles.ProfileDAOImpl;
import com.udc.master.tfm.tracksports.utils.DateUtils;

/**
 * Implementacion de los metodos para el acceso a la tabla de ejercicios pendientes
 * @author a.oteroc
 *
 */
public class PendingExerciseDAOImpl implements PendingExerciseDAO {

	/** Nombre de la tabla*/
	private static final String TABLE_NAME = "ACTIVIDAD_PENDIENTE";
	/** Lista de columnas de la tabla */
	private static final String TABLE_ID = "ID_ACTIVIDAD_PENDIENTE";
	private static final String START_TIME = "HORA_INICIO";
	private static final String DURATION = "DURACION";
	private static final String DISTANCE = "DISTANCIA";
	private static final String COMMENTS = "COMENTARIOS";
	
	private DatabaseHelper databaseHelper;
	
	/**
	 * Constructor vacio
	 */
	public PendingExerciseDAOImpl() {}
	
	/**
	 * Constructor de la clase
	 * @param context
	 */
	public PendingExerciseDAOImpl(Context context) {
		this.databaseHelper = new DatabaseHelper(context);
	}
	
	/**
	 * Metodo que devuelve el nombre de la tabla
	 * @return
	 */
	public static String getTableName() {
		return TABLE_NAME;
	}
	
	/**
	 * Metodo que devuelve el nombre de la columna identificador de la tabla
	 * @return
	 */
	public static String getColumnNameTableId() {
		return TABLE_ID;
	}
	
	/**
	 * Metodo que devuelve la sentencia para crear la tabla
	 * @return
	 */
	public static String getCreateSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS {0} (");
		sb.append("{1} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
		sb.append("{2} TIMESTAMP NOT NULL,");
		sb.append("{3} LONG,");
		sb.append("{4} REAL,");
		sb.append("{5} VARCHAR,");
		sb.append("{6} INTEGER, ");
		sb.append("FOREIGN KEY ({6}) REFERENCES {7} ({6})");
		sb.append(")");
		String createSQL = MessageFormat.format(sb.toString(), 
				TABLE_NAME,
				TABLE_ID,
				START_TIME,
				DURATION,
				DISTANCE,
				COMMENTS,
				ProfileDAOImpl.getColumnNameTableId(),
				ProfileDAOImpl.getTableName());
		return createSQL;
	}

	/**
	 * Metodo que devuelve la consulta para eliminar la tabla
	 * @return
	 */
	public static String getDeleteSQL() {
		String deleteSQL = MessageFormat.format("DROP TABLE IF EXISTS {0}", TABLE_NAME);
		return deleteSQL;
	}
	
	@Override
	public int insert(PendingExercise pendingExercise) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		ContentValues params = getContentValues(pendingExercise);
		long id = db.insert(TABLE_NAME, null, params);
		db.close();
		return Integer.valueOf(Long.valueOf(id).toString());
	}
	
	/**
	 * Metodo que elimina un ejercicio pendiente
	 * @param pendingExercise
	 * @param db
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 */
	private void delete(PendingExercise pendingExercise, SQLiteDatabase db) {
		String [] args = new String[1];
		args[0] = pendingExercise.getId().toString();
		db.delete(TABLE_NAME, TABLE_ID+"=?", args);
	}
	
	@Override
	public void delete(PendingExercise pendingExercise) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		this.delete(pendingExercise, db);
		db.close();
	}
	
	/**
	 * Metodo que elimina todos los ejercicios pendientes de un perfil
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param profile
	 * @param db
	 */
	public void deleteByProfile(Profile profile, SQLiteDatabase db) {
		List<PendingExercise> pendingExercises = this.findPendingExercisesByProfile(profile, db);
		if (pendingExercises != null && !pendingExercises.isEmpty()) {
			for (PendingExercise pendingExercise : pendingExercises) {
				this.delete(pendingExercise, db);
			}	
		}
	}

	/**
	 * Metodo que obtiene todos los ejercicios pendientes asociados a un usuario
	 * En caso de que el usuario no existe se recuperaran los ejercicios pendientes
	 * que no estan asociados a ningun usuario
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param profile
	 * @param db
	 * @return
	 */
	private List<PendingExercise> findPendingExercisesByProfile(Profile profile, SQLiteDatabase db) {
		Integer profileId = null;
		if (profile != null) {
			profileId = profile.getId();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT e.{1}, e.{2}, e.{3}, e.{4}, e.{5} FROM {0} e ");
		if (profileId != null) {
			sb.append("WHERE e.{6} = {7} ");
		} else {
			sb.append("WHERE e.{6} is NULL ");
		}
		sb.append("ORDER BY e.{2} ASC");
		
		String selectSQL = MessageFormat.format(sb.toString(), 
				TABLE_NAME, //Nombre de la tabla principal
				//Lista de columnas que se obtiene en la consulta
				TABLE_ID,
				START_TIME,
				DURATION,
				DISTANCE,
				COMMENTS,
				ProfileDAOImpl.getColumnNameTableId(),
				profileId
				);
		
		Cursor c = db.rawQuery(selectSQL, null);
		List<PendingExercise> pendingExercises = null;
		if (c.moveToFirst()) {
			pendingExercises = new ArrayList<>();
			do {
				PendingExercise exercise = new PendingExercise();
				
				exercise.setId(c.getLong(0));
				try {
					exercise.setStartTime(DateUtils.getBbddDateHourFormatter().parse(c.getString(1)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				exercise.setDuration(c.getLong(2));
				exercise.setDistance(c.getFloat(3));
				exercise.setComments(c.getString(4));
				exercise.setProfile(profile);
				
				pendingExercises.add(exercise);
			} while (c.moveToNext());
		}
		c.close();
		return pendingExercises;
	}
	
	@Override
	public PendingExercise findNextPendingExerciseByProfile(Profile profile) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Integer profileId = null;
		if (profile != null) {
			profileId = profile.getId();
		}
		String now = DateUtils.getBbddDateHourFormatter().format(Calendar.getInstance().getTime());
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT e.{1}, e.{2}, e.{3}, e.{4}, e.{5} FROM {0} e ");
		if (profileId != null) {
			sb.append("WHERE e.{6} = {7} ");
		} else {
			sb.append("WHERE e.{6} is NULL ");
		}
		sb.append("AND e.{2} > ''");
		sb.append(now);
		sb.append("'' ");
		sb.append("ORDER BY e.{2} ASC ");
		sb.append("LIMIT 1");
		
		String selectSQL = MessageFormat.format(sb.toString(), 
				TABLE_NAME, //Nombre de la tabla principal
				//Lista de columnas que se obtiene en la consulta
				TABLE_ID,
				START_TIME,
				DURATION,
				DISTANCE,
				COMMENTS,
				ProfileDAOImpl.getColumnNameTableId(),
				profileId
				);
		
		Cursor c = db.rawQuery(selectSQL, null);
		PendingExercise exercise = null;
		if (c.moveToFirst()) {
			do {
				exercise = new PendingExercise();
				
				exercise.setId(c.getLong(0));
				try {
					exercise.setStartTime(DateUtils.getBbddDateHourFormatter().parse(c.getString(1)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				exercise.setDuration(c.getLong(2));
				exercise.setDistance(c.getFloat(3));
				exercise.setComments(c.getString(4));
				
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return exercise;
	}
	
	@Override
	public List<PendingExercise> findPendingExercisesByProfile(Profile profile) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		List<PendingExercise> pendingExercises = this.findPendingExercisesByProfile(profile, db);
		db.close();
		return pendingExercises;
	}
	
	/**
	 * Metodo que transforma un objeto <code>PendingExercise</code> a
	 * una lista de valores para pasarle a las sentencias de BBDD
	 * @param pendingExercise
	 * @return
	 */
	private ContentValues getContentValues(PendingExercise pendingExercise) {
		ContentValues params = new ContentValues();
		params.put(START_TIME, pendingExercise.getBbddTextStartTime());
		params.put(DURATION, pendingExercise.getDuration());
		params.put(DISTANCE, pendingExercise.getDistance());
		params.put(COMMENTS, pendingExercise.getComments());
		if (pendingExercise.getProfile() != null) {
			params.put(ProfileDAOImpl.getColumnNameTableId(), pendingExercise.getProfile().getId());
		}
		return params;
	}
}

