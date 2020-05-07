package com.udc.master.tfm.tracksports.bbdd.pendingactivity;

import java.util.List;

import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;

/**
 * Interfaz de acceso a los metodos sobre los ejercicios pendientes
 * @author a.oteroc
 *
 */
public interface PendingExerciseDAO {

	/**
	 * Metodo que inserta un ejercicio pendiente en la BBDD
	 * @param pendingExercise
	 * @return identificador del perfil insertado
	 */
	public int insert(PendingExercise pendingExercise);
	
	/**
	 * Metodo que elimina un ejercicio pendiente
	 * @param pendingExercise
	 */
	public void delete(PendingExercise pendingExercise);
	
	/**
	 * Metodo que obtiene el proximo ejercicio a ejecutar asociados a un usuario.
	 * En caso de que el usuario no existe se recuperaran los ejercicios pendientes
	 * que no estan asociados a ningun usuario
	 * @param profile
	 * @param db
	 * @return
	 */
	public PendingExercise findNextPendingExerciseByProfile(Profile profile);
	
	/**
	 * Metodo que obtiene todos los ejercicios pendientes asociados a un usuario 
	 * En caso de que el usuario no existe se recuperaran los ejercicios pendientes
	 * que no estan asociados a ningun usuario
	 * @param profile
	 * @return
	 */
	public List<PendingExercise> findPendingExercisesByProfile(Profile profile);
}
