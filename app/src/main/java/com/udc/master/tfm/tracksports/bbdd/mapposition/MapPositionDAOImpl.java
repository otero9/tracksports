package com.udc.master.tfm.tracksports.bbdd.mapposition;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.activity.mapposition.ExerciseMapPositionDAOImpl;

/**
 * Implementacion de los metodos para el acceso a la tabla de coordenadas
 * @author a.oteroc
 *
 */
public class MapPositionDAOImpl implements MapPositionDAO {

	/** Nombre de la tabla*/
	private static final String TABLE_NAME = "COORDENADA";
	/** Lista de columnas de la tabla */
	private static final String TABLE_ID = "ID_COORDENADA";
	private static final String LATITUDE = "LATITUD";
	private static final String LONGITUDE = "LONGITUD";
	private static final String ALTITUDE = "ALTITUD";
	private static final String VELOCIDAD = "VELOCIDAD";
	private static final String RITMO = "RITMO";
	private static final String DISTANCIA = "DISTANCIA";
	private static final String TIEMPO = "TIEMPO";
	
	/**
	 * Constructor de la clase
	 * @param context
	 */
	public MapPositionDAOImpl() {}
	
	/**
	 * Metodo que devuelve la sentencia para crear la tabla
	 * @return
	 */
	public static String getCreateSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS {0} (");
		sb.append("{1} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
		sb.append("{2} REAL NOT NULL,");
		sb.append("{3} REAL NOT NULL,");
		sb.append("{4} REAL, ");
		sb.append("{5} REAL, ");
		sb.append("{6} REAL, ");
		sb.append("{7} REAL, ");
		sb.append("{8} BIGINT ");
		sb.append(")");
		String createSQL = MessageFormat.format(sb.toString(), 
				TABLE_NAME,
				TABLE_ID,
				LATITUDE,
				LONGITUDE,
				ALTITUDE,
				VELOCIDAD,
				RITMO,
				DISTANCIA,
				TIEMPO);
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
	 * Metodo que devuelve el nombre de la columna que representa la latitud
	 * @return the latitude
	 */
	public static String getColumNameLatitude() {
		return LATITUDE;
	}

	/**
	 * Metodo que devuelve el nombre de la columna que representa la longitud
	 * @return the longitude
	 */
	public static String getColumNameLongitude() {
		return LONGITUDE;
	}
	
	/**
	 * Metodo que comprueba si existe una coordenada por su identificador
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param mapPosition
	 * @param db
	 * @return
	 */
	public boolean exists(MapPosition mapPosition, SQLiteDatabase db) {
		String [] column = new String[]{TABLE_ID};
		Cursor c = db.query(TABLE_NAME, column, TABLE_ID+"=?", null, null, null, null);
		return c.getCount() > 0;
	}

	/**
	 * Metodo que inserta una coordenada <br>
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param mapPosition
	 * @param db
	 * @return el identificador del valor que se inserta
	 */
	public long insert(MapPosition mapPosition, SQLiteDatabase db) {
		ContentValues params = getContentValues(mapPosition);
		long id = db.insert(TABLE_NAME, null, params);
		return id;
	}
	
	/**
	 * Metodo que actualizar los datos de una coordenada
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param mapPosition
	 * @param db
	 */
	public void update(MapPosition mapPosition, SQLiteDatabase db) {
		ContentValues params = getContentValues(mapPosition);
		String [] args = new String[1];
		args[0] = mapPosition.getId().toString();
		db.update(TABLE_NAME, params, TABLE_ID+"=?", args);
	}
	
	/**
	 * Metodo que elimina una coordenada
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param mapPosition
	 * @param db
	 */
	public void delete(MapPosition mapPosition, SQLiteDatabase db) {
		String [] args = new String[1];
		args[0] = mapPosition.getId().toString();
		db.delete(TABLE_NAME, TABLE_ID+"=?", args);
	}
	
	/**
	 * Metodo que obtiene la ruta recorrida durante un ejercicio
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param exercise
	 * @param db
	 * @return
	 */
	public List<MapPosition> findExerciseRoute(Exercise exercise, SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT m.{2}, m.{5}, m.{6}, m.{7}, m.{8}, m.{9}, m.{10}, m.{11} FROM {0} emp ");
		sb.append("JOIN {1} m ON emp.{2} = m.{2} AND emp.{3} = {4}");
		
		String selectSQL = MessageFormat.format(sb.toString(),
				ExerciseMapPositionDAOImpl.getTableName(),
				TABLE_NAME,
				TABLE_ID,
				ExerciseDAOImpl.getColumnNameTableId(),
				exercise.getId(),
				//Lista de columnas que se obtiene en la consulta
				LATITUDE,
				LONGITUDE,
				ALTITUDE,
				VELOCIDAD,
				RITMO,
				DISTANCIA,
				TIEMPO
				);
		
		Cursor c = db.rawQuery(selectSQL, null);
		List<MapPosition> route = null;
		if (c.moveToFirst()) {
			route = new ArrayList<>();
			do {
				MapPosition mapPosition = new MapPosition();
				mapPosition.setId(c.getLong(0));
				mapPosition.setLatitude(c.getDouble(1));
				mapPosition.setLongitude(c.getDouble(2));
				mapPosition.setAltitude(c.getDouble(3));
				mapPosition.setSpeed(c.getFloat(4));
				mapPosition.setSpeedPace(c.getFloat(5));
				mapPosition.setDistance(c.getFloat(6));
				mapPosition.setTime(c.getLong(7));
				route.add(mapPosition);
			} while (c.moveToNext());
		}
		c.close();
		return route;
	}
	
	/**
	 * Metodo que tansforma un objeto <code>Profile</code> a
	 * una lista de valores para pasarle a las sentencias de BBDD
	 * @param profile
	 * @return
	 */
	private ContentValues getContentValues(MapPosition mapPosition) {
		ContentValues params = new ContentValues();
		params.put(LATITUDE, mapPosition.getLatitude());
		params.put(LONGITUDE, mapPosition.getLongitude());
		params.put(ALTITUDE, mapPosition.getAltitude());
		params.put(VELOCIDAD, mapPosition.getSpeed());
		params.put(RITMO, mapPosition.getSpeedPace());
		params.put(DISTANCIA, mapPosition.getDistance());
		params.put(TIEMPO, mapPosition.getTime());
		return params;
	}
}
