package com.udc.master.tfm.tracksports.bbdd.activity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.udc.master.tfm.tracksports.bbdd.DatabaseHelper;
import com.udc.master.tfm.tracksports.bbdd.activity.mapposition.ExerciseMapPositionDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPositionDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.bbdd.profiles.ProfileDAOImpl;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.utils.DateUtils;

/**
 * Implementacion de los metodos para el acceso a la tabla de ejercicios
 * @author a.oteroc
 *
 */
public class ExerciseDAOImpl implements ExerciseDAO {

	/** Nombre de la tabla*/
	private static final String TABLE_NAME = "ACTIVIDAD";
	/** Lista de columnas de la tabla */
	private static final String TABLE_ID = "ID_ACTIVIDAD";
	private static final String START_TIME = "HORA_INICIO";
	private static final String DURATION = "DURACION";
	private static final String DISTANCE = "DISTANCIA";
	private static final String SPEED_AVG = "VELOCIDAD_MEDIA";
	private static final String SPEED_MAX = "VELOCIDAD_MAXIMA";
	private static final String SPEED_PACE = "RITMO_MEDIO";
	private static final String ALTITUDE_MIN = "ALTITUD_MINIMA";
	private static final String ALTITUDE_MAX = "ALTITUD_MAXIMA";
	private static final String CALORIES_BURNED = "CALORIAS_QUEMADAS";
	private static final String STEPS = "PASOS";
	private static final String SYNC_STATUS = "ESTADO_SINCRONIZACION";
	
	private DatabaseHelper databaseHelper;
	
	/**
	 * Constructor vacio
	 */
	public ExerciseDAOImpl() {}
	
	/**
	 * Constructor de la clase
	 * @param context
	 */
	public ExerciseDAOImpl(Context context) {
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
		sb.append("{3} LONG NOT NULL,");
		sb.append("{4} REAL NOT NULL,");
		sb.append("{5} REAL NOT NULL,");
		sb.append("{6} REAL NOT NULL,");
		sb.append("{7} REAL NOT NULL,");
		sb.append("{8} REAL NOT NULL, ");
		sb.append("{9} REAL NOT NULL, ");
		sb.append("{10} REAL NOT NULL, ");
		sb.append("{11} INTEGER NOT NULL, ");
		sb.append("{12} SMALLINT, ");
		sb.append("{13} INTEGER, ");
		sb.append("FOREIGN KEY ({13}) REFERENCES {14} ({13})");
		sb.append(")");
		String createSQL = MessageFormat.format(sb.toString(), 
				TABLE_NAME,
				TABLE_ID,
				START_TIME,
				DURATION,
				DISTANCE,
				SPEED_AVG,
				SPEED_MAX,
				SPEED_PACE,
				ALTITUDE_MIN,
				ALTITUDE_MAX,
				CALORIES_BURNED,
				STEPS,
				SYNC_STATUS,
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
	public int insert(Exercise exercise, int percentPoints) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		//Se inserta el ejercicio
		ContentValues params = getContentValues(exercise);
		long id = db.insert(TABLE_NAME, null, params);

		//Se insertan las coordenadas de la ruta recorrida
		if (exercise.getRoute() != null && !exercise.getRoute().isEmpty()) {
			MapPositionDAOImpl mapPositionHelper = new MapPositionDAOImpl();
			ExerciseMapPositionDAOImpl relationHelper = new ExerciseMapPositionDAOImpl();
			List<MapPosition> points = exercise.getRoute();
			int totalSize = points.size(); //Total de puntos recogidos
			int totalRoute = totalSize * percentPoints / 100; //Total de puntos a almacenar
			int moduleRoute = 0; //Modulo para calcular los puntos que se almacenan
			if (totalRoute > 0) {
				moduleRoute = totalSize / totalRoute;
			}
			long order = 1;
			for (int i = 0; i < totalSize; i++) {
				MapPosition mapPosition = points.get(i);
				//Se almacena el porcenajte de puntos definido
				//Siempre se almacena el primer y ultimo punto
				if (i == 0|| i + 1 == totalSize || (moduleRoute > 0 && i % moduleRoute == 0)) {
					//Se inserta la coordenada
					long foreignId = mapPositionHelper.insert(mapPosition, db);
					//Se asocia la coordenada al ejercicio
					relationHelper.insert(id, foreignId, order, db);
					order++;	
				}
			}
		}
		db.close();
		return Integer.valueOf(Long.valueOf(id).toString());
	}
	
	@Override
	public void update(Exercise exercise) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		ContentValues params = getContentValues(exercise);
		String [] args = new String[1];
		args[0] = exercise.getId().toString();
		db.update(TABLE_NAME, params, TABLE_ID+"=?", args);
		db.close();
	}
	
	/**
	 * Metodo que elimina un ejercicio y sus puntos asociados
	 * @param exercise
	 * @param db
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 */
	private void delete(Exercise exercise, SQLiteDatabase db) {
		MapPositionDAOImpl mapPositionHelper = new MapPositionDAOImpl();
		List<MapPosition> route = mapPositionHelper.findExerciseRoute(exercise, db);
		
		//Se eliminan los puntos asociados
		if (route != null && !route.isEmpty()) {
			ExerciseMapPositionDAOImpl relationHelper = new ExerciseMapPositionDAOImpl();
			for (MapPosition mapPosition : route) {
				mapPositionHelper.delete(mapPosition, db);
			}
			relationHelper.delete(exercise.getId(), db);
			
		}
		String [] args = new String[1];
		args[0] = exercise.getId().toString();
		db.delete(TABLE_NAME, TABLE_ID+"=?", args);
	}
	
	@Override
	public void delete(Exercise exercise) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		this.delete(exercise, db);
		db.close();
	}
	
	/**
	 * Metodo que elimina todos los ejercicios de un perfil
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param profile
	 * @param db
	 */
	public void deleteByProfile(Profile profile, SQLiteDatabase db) {
		List<Exercise> exercises = this.findExercisesByProfile(profile, null, db);
		if (exercises != null && !exercises.isEmpty()) {
			for (Exercise exercise : exercises) {
				this.delete(exercise, db);
			}	
		}
	}

	/**
	 * Metodo que obtiene todos los ejercicios asociados a un usuario de los ultimos dias
	 * ordenados por fecha de alta descendente.
	 * En caso de que el usuario no existe se recuperaran los ejercicios
	 * que no estan asociados a ningun usuario
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param profile
	 * @param days
	 * @param db
	 * @return
	 */
	private List<Exercise> findExercisesByProfile(Profile profile, Integer days, SQLiteDatabase db) {
		Integer profileId = null;
		if (profile != null) {
			profileId = profile.getId();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT e.{1}, e.{2}, e.{3}, e.{4}, e.{5}, e.{6}, e.{7}, e.{8}, e.{9}, e.{10}, e.{11}, e.{12} FROM {0} e ");
		if (profileId != null) {
			sb.append("WHERE e.{13} = {14} ");
		} else {
			sb.append("WHERE e.{13} is NULL ");
		}
		if (days != null) {
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DAY_OF_MONTH, -days);
			String date = DateUtils.getBbddDateHourFormatter().format(now.getTime());
			sb.append("AND e.{2} > ''");
			sb.append(date);
			sb.append("'' ");
		}
		sb.append("ORDER BY e.{2} DESC");
		
		String selectSQL = MessageFormat.format(sb.toString(), 
				TABLE_NAME, //Nombre de la tabla principal
				//Lista de columnas que se obtiene en la consulta
				TABLE_ID,
				START_TIME,
				DURATION,
				DISTANCE,
				SPEED_AVG,
				SPEED_MAX,
				SPEED_PACE,
				ALTITUDE_MIN,
				ALTITUDE_MAX,
				CALORIES_BURNED,
				STEPS,
				SYNC_STATUS,
				ProfileDAOImpl.getColumnNameTableId(),
				profileId
				);
		
		Cursor c = db.rawQuery(selectSQL, null);
		List<Exercise> exercises = null;
		if (c.moveToFirst()) {
			exercises = new ArrayList<>();
			do {
				Exercise exercise = new Exercise();
				
				exercise.setId(c.getLong(0));
				try {
					exercise.setStartTime(DateUtils.getBbddDateHourFormatter().parse(c.getString(1)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				exercise.setDuration(c.getLong(2));
				exercise.setDistance(c.getFloat(3));
				exercise.setSpeedAvg(c.getFloat(4));
				exercise.setSpeedMax(c.getFloat(5));
				exercise.setSpeedPace(c.getFloat(6));
				exercise.setAltitudeMin(c.getDouble(7));
				exercise.setAltitudeMax(c.getDouble(8));
				exercise.setCaloriesBurned(c.getFloat(9));
				exercise.setSteps(c.getLong(10));
				exercise.setSyncStatus(HttpStatusType.valueOf(c.getInt(11)));
				exercise.setProfile(profile);
				exercises.add(exercise);
			} while (c.moveToNext());
		}
		c.close();
		return exercises;
	}
	
	@Override
	public List<Exercise> findExercisesByProfile(Profile profile, Integer days) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		List<Exercise> exercises = this.findExercisesByProfile(profile, days, db);
		db.close();
		return exercises;
	}
	
	@Override
	public List<MapPosition> findRoute(Exercise exercise) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		MapPositionDAOImpl mapPositionHelper = new MapPositionDAOImpl();
		List<MapPosition> route = mapPositionHelper.findExerciseRoute(exercise, db);
		db.close();
		exercise.setRoute(route);
		return route;
	}
	
	/**
	 * Metodo que transforma un objeto <code>Exercise</code> a
	 * una lista de valores para pasarle a las sentencias de BBDD
	 * @param exercise
	 * @return
	 */
	private ContentValues getContentValues(Exercise exercise) {
		ContentValues params = new ContentValues();
		params.put(START_TIME, exercise.getBbddTextStartTime());
		params.put(DURATION, exercise.getDuration());
		params.put(DISTANCE, exercise.getDistance());
		params.put(SPEED_AVG, exercise.getSpeedAvg());
		params.put(SPEED_MAX, exercise.getSpeedMax());
		params.put(SPEED_PACE, exercise.getSpeedPace());
		params.put(ALTITUDE_MIN, exercise.getAltitudeMin());
		params.put(ALTITUDE_MAX, exercise.getAltitudeMax());
		params.put(CALORIES_BURNED, exercise.getCaloriesBurned());
		params.put(STEPS, exercise.getSteps());
		params.put(SYNC_STATUS, (exercise.getSyncStatus() != null)? exercise.getSyncStatus().getCode():null);
		if (exercise.getProfile() != null) {
			params.put(ProfileDAOImpl.getColumnNameTableId(), exercise.getProfile().getId());
		}
		return params;
	}
}

