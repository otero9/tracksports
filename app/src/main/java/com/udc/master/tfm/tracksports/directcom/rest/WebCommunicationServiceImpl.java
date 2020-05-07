package com.udc.master.tfm.tracksports.directcom.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.udc.master.tfm.tracksports.directcom.rest.object.AutenticationResponseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.EditRequestWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.EditResponseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.ExerciseResponseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.ProfileExerciseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.RegistrationRequestWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.RegistrationResponseWS;
import com.udc.master.tfm.tracksports.utils.CryptoUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebCommunicationServiceImpl implements WebCommunicationService {

	private Context context;
	
	public WebCommunicationServiceImpl(Context context) {
		this.context = context;
	}
	
	@Override
	public AutenticationResponseWS autenticate(String login, String enPassword) throws ClientProtocolException, IOException {
		//Se construye la peticion
        StringBuilder sb = new StringBuilder();
        sb.append(HttpUtils.getAutenticationServiceUrl(context));
        sb.append(login);
        sb.append("/");
        sb.append(getPassWS(enPassword));
        
		HttpGet request = new HttpGet(sb.toString());
		request.setHeader("content-type", "application/json");

		//Se realiza la peticion
		HttpClient httpClient = HttpUtils.getClient(context);
    	HttpResponse response = httpClient.execute(request);
    	String respStr = parseResponse(response);

    	//Se obtienen los parametros de la respuesta
		Gson gson = new Gson();
		return gson.fromJson(respStr, AutenticationResponseWS.class);
	}

	@Override
	public RegistrationResponseWS register(String login, String enPassword, RegistrationRequestWS requestContent) throws ParseException, IOException {
        //Se construye la peticion
        StringBuilder sb = new StringBuilder();
        sb.append(HttpUtils.getRegisterServiceUrl(context));
        sb.append(login);
        sb.append("/");
        sb.append(getPassWS(enPassword));
        
		HttpPost request = new HttpPost(sb.toString());
		request.setHeader("content-type", "application/json");
		String json = null;
		Gson gsonRequest = new Gson();
		json = gsonRequest.toJson(requestContent);
		request.setEntity(new StringEntity(json));
		
		//Se realiza la peticion
		HttpClient httpClient = HttpUtils.getClient(context);
    	HttpResponse response = httpClient.execute(request);
    	String respStr = parseResponse(response);

    	//Se obtienen los parametros de la respuesta
		Gson gsonResponse = new Gson();
		return gsonResponse.fromJson(respStr, RegistrationResponseWS.class);
	}

	@Override
	public EditResponseWS edit(String guid, EditRequestWS requestContent) throws ClientProtocolException, IOException {
		//Se construye la peticion
        StringBuilder sb = new StringBuilder();
        sb.append(HttpUtils.getEditServiceUrl(context));
        sb.append(guid);
		HttpPost request = new HttpPost(sb.toString());
		request.setHeader("content-type", "application/json");
		String json = null;
		Gson gsonRequest = new Gson();
		json = gsonRequest.toJson(requestContent);
		request.setEntity(new StringEntity(json));

		//Se realiza la peticion
		HttpClient httpClient = HttpUtils.getClient(context);
    	HttpResponse response = httpClient.execute(request);
    	String respStr = parseResponse(response);

    	//Se obtienen los parametros de la respuesta
		Gson gsonResponse = new Gson();
		return gsonResponse.fromJson(respStr, EditResponseWS.class);
	}

	@Override
	public ExerciseResponseWS saveExercise(String guid, ProfileExerciseWS requestContent) throws ClientProtocolException, IOException {
		//Se construye la peticion
        StringBuilder sb = new StringBuilder();
        sb.append(HttpUtils.getSaveExerciseServiceUrl(context));
 		HttpPost request = new HttpPost(sb.toString());
		request.setHeader("content-type", "application/json");
		String json = null;
		Gson gson = new Gson();
		json = gson.toJson(requestContent);
		request.setEntity(new StringEntity(json));

		//Se envia la peticion
		HttpClient httpClient = HttpUtils.getClient(context);
    	HttpResponse response = httpClient.execute(request);
    	String respStr = parseResponse(response);

    	//Se obtienen los parametros de la respuesta
		Gson gsonResponse = new Gson();
		return gsonResponse.fromJson(respStr, ExerciseResponseWS.class);
	}

	/**
	 * Metodo que obtiene la contrasena para notificar a traves del servicio Web.
	 * Dependiendo de la configuracion se enviara encriptada o en claro
	 * @param enPassword
	 * @return
	 */
	private String getPassWS(String enPassword) {
        if (Boolean.TRUE.equals(PreferencesUtils.getPreferences(PreferencesTypes.SYNC_CRYPT_PASS, Boolean.class, context))) {
        	try {
				return URLEncoder.encode(enPassword, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return enPassword;
			}
        } else {
        	String pass = CryptoUtils.decrypt(enPassword);
        	try {
        		return URLEncoder.encode(pass, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return pass;
			}
        }
	}
	
	/**
	 * Metodo que parsea la respuesta del servicio Web
	 * @param response
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private String parseResponse(HttpResponse response) throws ParseException, IOException {
    	String respStr = EntityUtils.toString(response.getEntity());
    	respStr = respStr.replace("\\", "");
    	
    	if (!respStr.startsWith("{") && !respStr.endsWith("}")) {
    		respStr = respStr.substring(1,respStr.length()-1);
    	}
    	return respStr;
	}
}
