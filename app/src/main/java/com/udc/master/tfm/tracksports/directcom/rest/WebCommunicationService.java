package com.udc.master.tfm.tracksports.directcom.rest;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import com.udc.master.tfm.tracksports.directcom.rest.object.AutenticationResponseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.EditRequestWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.EditResponseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.ExerciseResponseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.ProfileExerciseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.RegistrationRequestWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.RegistrationResponseWS;

/**
 * Interfaz de acceso para la sincronizacion con el WebService
 * @author a.oteroc
 *
 */
public interface WebCommunicationService {

	/**
	 * Metodo para autenticar un perfil con la plataforma Web
	 * @param login
	 * @param enPassword
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @return
	 */
	public AutenticationResponseWS autenticate(String login, String enPassword) throws ClientProtocolException, IOException;
	
	/**
	 * Metodo para registrar un perfil contra la plataforma Web
	 * @param login
	 * @param enPassword
	 * @param request
	 * @throws ParseException
	 * @throws IOException
	 * @return
	 */
	public RegistrationResponseWS register(String login, String enPassword, RegistrationRequestWS requestContent) throws ParseException, IOException;
	
	/**
	 * Metodo para editar un perfil en la plataforma Web
	 * @param guid
	 * @param request
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @return
	 */
	public EditResponseWS edit(String guid, EditRequestWS requestContent) throws ClientProtocolException, IOException;
	
	/**
	 * Metodo para registrar un ejercicio en la plataforma Web de un perfil en concreto
	 * @param guid
	 * @param request
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @return
	 */
	public ExerciseResponseWS saveExercise(String guid, ProfileExerciseWS requestContent) throws ClientProtocolException, IOException;
}
