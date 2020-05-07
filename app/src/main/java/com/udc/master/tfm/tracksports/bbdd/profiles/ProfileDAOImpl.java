package com.udc.master.tfm.tracksports.bbdd.profiles;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.udc.master.tfm.tracksports.bbdd.DatabaseHelper;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPositionDAOImpl;
import com.udc.master.tfm.tracksports.bbdd.pendingactivity.PendingExerciseDAOImpl;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.utils.DateUtils;

/**
 * Implementacion de los metodos para el acceso a la tabla de perfiles
 * @author a.oteroc
 *
 */
public class ProfileDAOImpl implements ProfileDAO {

	/** Nombre de la tabla*/
	private static final String TABLE_NAME = "PERFIL";
	/** Lista de columnas de la tabla */
	private static final String TABLE_ID = "ID_PERFIL";
	private static final String USER = "USUARIO";
	private static final String ENCRIPTED_PASSWORD = "CONTRASENA_ENCRIPTADA";
	private static final String NAME = "NOMBRE";
	private static final String BIRTHDAY = "FECHA_NACIMIENTO";
	private static final String HEIGHT = "ALTURA";
	private static final String WEIGHT = "PESO";
	private static final String GENDER = "SEXO";
	private static final String STEP_LENGTH = "LONGITUD_PASO";
	private static final String GUID = "GUID";
	private static final String SYNC_STATUS = "ESTADO_SINCRONIZACION";
	private static final String IMAGE_PATH = "URL_IMAGEN";
	
	private DatabaseHelper databaseHelper;
	
	/**
	 * Constructor de la clase
	 * @param context
	 */
	public ProfileDAOImpl(Context context) {
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
		sb.append("{2} VARCHAR(128) NOT NULL,");
		sb.append("{3} VARCHAR(128) NOT NULL,");
		sb.append("{4} VARCHAR(128) NOT NULL,");
		sb.append("{5} DATE NOT NULL,");
		sb.append("{6} SMALLINT NOT NULL,");
		sb.append("{7} SMALLINT NOT NULL,");
		sb.append("{8} DECIMAL(0,1) NOT NULL, ");
		sb.append("{9} SMALLINT NOT NULL, ");
		sb.append("{10} VARCHAR(500), ");
		sb.append("{11} SMALLINT, ");
		sb.append("{12} VARCHAR(2000), ");
		sb.append("{13} INTEGER, ");
		sb.append("FOREIGN KEY ({13}) REFERENCES {14} ({13})");
		sb.append(")");
		String createSQL = MessageFormat.format(sb.toString(), 
				TABLE_NAME,
				TABLE_ID,
				USER,
				ENCRIPTED_PASSWORD,
				NAME,
				BIRTHDAY,
				HEIGHT,
				WEIGHT,
				GENDER,
				STEP_LENGTH,
				GUID,
				SYNC_STATUS,
				IMAGE_PATH,
				MapPositionDAOImpl.getColumnNameTableId(),
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

	@Override
	public int insert(Profile profile) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		Long foreignId = null;
		//Si esta informada la coordenada de la casa se inserta
		if (profile.getMapPosition() != null) {
			MapPositionDAOImpl mapPositionHelper = new MapPositionDAOImpl();
			foreignId = mapPositionHelper.insert(profile.getMapPosition(), db);
		}
		ContentValues params = getContentValues(profile, foreignId);
		long id = db.insert(TABLE_NAME, null, params);
		db.close();
		return Integer.valueOf(Long.valueOf(id).toString());
	}
	
	@Override
	public void update(Profile profile) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		Long foreignId = null;
		//Si esta informada la coordenada de la casa se actualiza
		if (profile.getMapPosition() != null) {
			MapPositionDAOImpl mapPositionHelper = new MapPositionDAOImpl();
			//Si no existia una coordenada previa se inserta
			if (profile.getMapPosition().getId() != null) {
				mapPositionHelper.update(profile.getMapPosition(), db);
				foreignId = profile.getMapPosition().getId();
			} else {
				foreignId = mapPositionHelper.insert(profile.getMapPosition(), db);
			}
		}
		ContentValues params = getContentValues(profile, foreignId);
		String [] args = new String[1];
		args[0] = profile.getId().toString();
		db.update(TABLE_NAME, params, TABLE_ID+"=?", args);
		db.close();
	}
	
	@Override
	public void delete(Profile profile) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		//Si esta informada la coordenada de la casa se elimina
		if (profile.getMapPosition() != null) {
			MapPositionDAOImpl mapPositionHelper = new MapPositionDAOImpl();
			mapPositionHelper.delete(profile.getMapPosition(), db);
		}
		
		//Se eliminan los ejercicios asociados al perfil
		ExerciseDAOImpl exerciseHelper = new ExerciseDAOImpl();
		exerciseHelper.deleteByProfile(profile, db);
		
		//Se eliminan los ejercicios pendientes asociados al perfil
		PendingExerciseDAOImpl pendingExerciseHelper = new PendingExerciseDAOImpl();
		pendingExerciseHelper.deleteByProfile(profile, db);
		
		//Se elimina el perfil
		String [] args = new String[1];
		args[0] = profile.getUser();
		db.delete(TABLE_NAME, USER+"=?", args);
		db.close();
	}
	
	@Override
	public List<Profile> findAllProfiles() {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.{3}, p.{4}, p.{5}, p.{6}, p.{7}, p.{8}, p.{9}, p.{10}, p.{11}, p.{12}, p.{13}, p.{14}, c.{15}, c.{16}, c.{17} FROM {0} p ");
		sb.append("LEFT JOIN {1} c ON p.{2}=c.{2} ");
		sb.append("ORDER BY p.{6} ASC");
		
		String selectSQL = MessageFormat.format(sb.toString(), 
				TABLE_NAME, //Nombre de la tabla principal
				MapPositionDAOImpl.getTableName(), //Nombre de la tabla con la que se hace el JOIN
				MapPositionDAOImpl.getColumnNameTableId(), //Columna por la que se hace el JOIN
				//Lista de columnas que se obtiene en la consulta
				TABLE_ID,
				USER,
				ENCRIPTED_PASSWORD,
				NAME,
				BIRTHDAY,
				HEIGHT,
				WEIGHT,
				GENDER,
				STEP_LENGTH,
				GUID,
				SYNC_STATUS,
				IMAGE_PATH,
				MapPositionDAOImpl.getColumnNameTableId(),
				MapPositionDAOImpl.getColumNameLatitude(),
				MapPositionDAOImpl.getColumNameLongitude()
				);
		
		Cursor c = db.rawQuery(selectSQL, null);
		List<Profile> profiles = null;
		if (c.moveToFirst()) {
			profiles = new ArrayList<>();
			do {
				Profile profile = new Profile();
				profile.setId(c.getInt(0));
				profile.setUser(c.getString(1));
				profile.setEnPass(c.getString(2));
				profile.setName(c.getString(3));
				try {
					profile.setBirthday(DateUtils.getDateFormatter().parse(c.getString(4)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				profile.setHeight(c.getShort(5));
				profile.setWeight(c.getShort(6));
				profile.setGender(Gender.valueOf(c.getShort(7)));
				profile.setStepLength(c.getShort(8));
				if (!c.isNull(9)) {
					profile.setGuid(c.getString(9));
				}
				if (!c.isNull(10)) {
					profile.setSyncStatus(HttpStatusType.valueOf(c.getInt(10)));
				}
				
				if (!c.isNull(11)) {
					profile.setImagePath(c.getString(11));
				}
				MapPosition mapPosition = null;
				if (!c.isNull(12)) {
					mapPosition = new MapPosition();
					mapPosition.setId(c.getLong(12));
					profile.setMapPosition(mapPosition);
				}
				if (!c.isNull(13) && mapPosition != null) {
					mapPosition.setLatitude(c.getDouble(13));
				}
				if (!c.isNull(14) && mapPosition != null) {
					mapPosition.setLongitude(c.getDouble(14));
				}
				profiles.add(profile);
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return profiles;
	}
	
	@Override
	public boolean existsUser(String username) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(1) FROM {0} p ");
		sb.append("WHERE p.{1} = ''{2}''");
		String selectSQL = MessageFormat.format(sb.toString(), 
				TABLE_NAME,
				USER,
				username
				);
		Cursor c = db.rawQuery(selectSQL, null);
		if (c.moveToFirst()) {
			return !(c.getInt(0) == 0);
		}
		return false;
	}
	
	/**
	 * Metodo que tansforma un objeto <code>Profile</code> a
	 * una lista de valores para pasarle a las sentencias de BBDD
	 * @param profile
	 * @return
	 */
	private ContentValues getContentValues(Profile profile, Long mapPositionId) {
		ContentValues params = new ContentValues();
		params.put(USER, profile.getUser());
		params.put(ENCRIPTED_PASSWORD, profile.getEnPass());
		params.put(NAME, profile.getName());
		params.put(BIRTHDAY, profile.getTextBirthday());
		params.put(HEIGHT, profile.getHeight());
		params.put(WEIGHT, profile.getWeight());
		params.put(GENDER, profile.getGender().getId());
		params.put(STEP_LENGTH, profile.getStepLength());
		params.put(GUID, profile.getGuid());
		params.put(SYNC_STATUS, (profile.getSyncStatus() != null)? profile.getSyncStatus().getCode():null);
		params.put(IMAGE_PATH, profile.getImagePath());
		params.put(MapPositionDAOImpl.getColumnNameTableId(), mapPositionId);
		return params;
	}
}
