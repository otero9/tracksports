package com.udc.master.tfm.tracksports.bbdd.profiles;

import java.util.List;

/**
 * Interfaz de acceso a los metodos sobre los perfiles
 * @author a.oteroc
 *
 */
public interface ProfileDAO {

	/**
	 * Metodo que inserta un perfil en la BBDD
	 * @param profile
	 * @return identificador del perfil insertado
	 */
	public int insert(Profile profile);
	
	/**
	 * Metodo que actualizar los datos de un perfil
	 * @param profile
	 */
	public void update(Profile profile);
	
	/**
	 * Metodo que elimina un perfil a partir de su nombre de usuario
	 * @param profile
	 */
	public void delete(Profile profile);
	
	/**
	 * Metodo que obtiene todos los perfiles almacenados en BBDD
	 * ordenados por nombre
	 * @return
	 */
	public List<Profile> findAllProfiles();
	
	/**
	 * Metodo que comprueba si existe un usuario a partir de su login
	 * @param username
	 * @return
	 */
	public boolean existsUser(String username);
}
