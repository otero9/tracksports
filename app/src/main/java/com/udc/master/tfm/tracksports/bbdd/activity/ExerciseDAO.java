package com.udc.master.tfm.tracksports.bbdd.activity;

import java.util.List;

import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;

/**
 * Interfaz de acceso a los metodos sobre los ejercicios
 * @author a.oteroc
 *
 */
public interface ExerciseDAO {

	/**
	 * Metodo que inserta un ejercicio en la BBDD
	 * @param profile
	 * @param percentPoints Porcentaje de puntos que se almacenan
	 * @return identificador del perfil insertado
	 */
	public int insert(Exercise exercise, int percentPoints);
	
	/**
	 * Metodo que actualizar los datos de un ejercicio
	 * @param profile
	 */
	public void update(Exercise exercise);
	
	/**
	 * Metodo que elimina un ejercicio y sus puntos asociados
	 * @param exercise
	 */
	public void delete(Exercise exercise);
	
	/**
	 * Metodo que obtiene todos los ejercicios asociados a un usuario de los ultimos dias
	 * ordenados por fecha de alta descendente.
	 * En caso de que el usuario no existe se recuperaran los ejercicios
	 * que no estan asociados a ningun usuario
	 * @param profile
	 * @param days
	 * @return
	 */
	public List<Exercise> findExercisesByProfile(Profile profile, Integer days);
	
	/**
	 * Metodo que obtiene la lista de coordenadas de un ejercicio.
	 * Se setean la ruta al objeto pasado por parametro
	 * @param exercise
	 * @return
	 */
	public List<MapPosition> findRoute(Exercise exercise);
	
}
