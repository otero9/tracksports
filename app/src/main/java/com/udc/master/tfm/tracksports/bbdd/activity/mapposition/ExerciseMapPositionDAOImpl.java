package com.udc.master.tfm.tracksports.bbdd.activity.mapposition;

import java.text.MessageFormat;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPositionDAOImpl;

/**
 * Implementacion de los metodos para el acceso a la relacion entre ejercicios
 * y coordenadas
 * @author a.oteroc
 *
 */
public class ExerciseMapPositionDAOImpl implements ExerciseMapPositionDAO {

	/** Nombre de la tabla*/
	private static final String TABLE_NAME = "ACTIVIDAD_COORDENADA";
	/** Lista de columnas de la tabla */
	private static final String TABLE_ID = "ID_ACTIVIDAD_COORDENADA";
	private static final String ORDEN = "ORDEN";
	
	/**
	 * Constructor de la clase
	 * @param context
	 */
	public ExerciseMapPositionDAOImpl() {}

	/**
	 * Metodo que devuelve el nombre de la tabla
	 * @return
	 */
	public static String getTableName() {
		return TABLE_NAME;
	}
	
	/**
	 * Metodo que devuelve la sentencia para crear la tabla
	 * @return
	 */
	public static String getCreateSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS {0} (");
		sb.append("{1} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
		sb.append("{2} INTEGER NOT NULL, ");
		sb.append("{3} INTEGER NOT NULL, ");
		sb.append("{4} BIGINT NOT NULL, ");
		sb.append("FOREIGN KEY ({2}) REFERENCES {5} ({2}), ");
		sb.append("FOREIGN KEY ({3}) REFERENCES {6} ({3})");
		sb.append(")");
		String createSQL = MessageFormat.format(sb.toString(), 
				TABLE_NAME,
				TABLE_ID,
				ExerciseDAOImpl.getColumnNameTableId(),
				MapPositionDAOImpl.getColumnNameTableId(),
				ORDEN,
				ExerciseDAOImpl.getTableName(),
				MapPositionDAOImpl.getTableName());
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
	 * Metodo que inserta la relacion entre una coordenada y un ejercicio.
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param exerciseId
	 * @param mapPositionId
	 * @param order
	 * @param db
	 * @return
	 */
	public int insert(Long exerciseId, Long mapPositionId, Long order, SQLiteDatabase db) {
		ContentValues params = new ContentValues();
		params.put(ExerciseDAOImpl.getColumnNameTableId(), exerciseId);
		params.put(MapPositionDAOImpl.getColumnNameTableId(), mapPositionId);
		params.put(ORDEN, order);
		long id = db.insert(TABLE_NAME, null, params);
		return Integer.valueOf(Long.valueOf(id).toString());
	}
	
	/**
	 * Metodo que elimina una lista de relaciones para un ejercicio
	 * <b>IMPORTANTE</b>: No se cierra la conexion con BBDD
	 * @param exerciseId
	 * @param db
	 */
	public void delete(Long exerciseId, SQLiteDatabase db) {
		String [] args = new String[1];
		args[0] = exerciseId.toString();
		db.delete(TABLE_NAME, ExerciseDAOImpl.getColumnNameTableId()+"=?", args);
	}
}
