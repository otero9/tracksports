package com.udc.master.tfm.tracksports.directcom.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;
import android.content.Context;

/**
 * Clase de utilidad para las conexiones HTTPS
 * @author a.oteroc
 *
 */
public class HttpUtils {

//	public static final String SERVICE_URL = "https://oz49.udc.es/";
//	public static final String SERVICE_URL = "http://demo6569069.mockable.io/";
	private static final String AUTENTICATION_SERVICE_URL = "AutenticarUsuario/";
	private static final String REGISTER_URL = "RegistrarUsuario/";
	private static final String EDIT_URL = "EditarUsuario/";
	private static final String SAVE_EXERCISE_URL = "GuardarEjercicio";
	
	/** GUID que devuelve la peticion en caso de un error en la autenticacion */
	public static final String GUID_NOT_VALID = "00000000-0000-0000-0000-000000000000";
	
	/**
	 * Metodo que obtiene la url del servicio de autenticacion
	 * @param context
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getAutenticationServiceUrl(Context context) {
		try {
			String syncUrl = PreferencesUtils.getPreferences(PreferencesTypes.SYNC_URL, String.class, context);
			return URLDecoder.decode(syncUrl, "UTF-8") + AUTENTICATION_SERVICE_URL;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Metodo que obtiene la url del servicio de registro
	 * @param context
	 * @return
	 */
	public static String getRegisterServiceUrl(Context context) {
		try {
			String syncUrl = PreferencesUtils.getPreferences(PreferencesTypes.SYNC_URL, String.class, context);
			return URLDecoder.decode(syncUrl, "UTF-8") + REGISTER_URL;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Metodo que obtiene la url del servicio de edicion de usuario
	 * @param context
	 * @return
	 */
	public static String getEditServiceUrl(Context context) {
		try {
			String syncUrl = PreferencesUtils.getPreferences(PreferencesTypes.SYNC_URL, String.class, context);
			return URLDecoder.decode(syncUrl, "UTF-8") + EDIT_URL;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Metodo que obtiene la url del servicio de sincronizacion de ejercicios
	 * @param context
	 * @return
	 */
	public static String getSaveExerciseServiceUrl(Context context) {
		try {
			String syncUrl = PreferencesUtils.getPreferences(PreferencesTypes.SYNC_URL, String.class, context);
			return URLDecoder.decode(syncUrl, "UTF-8") + SAVE_EXERCISE_URL;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Metodo que obtiene un cliente http
	 * @param context
	 * @return
	 */
	private static HttpClient getHttpClient(Context context) {
		try {
	    	HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	        Integer timeout = PreferencesUtils.getPreferences(PreferencesTypes.SYNC_TIMEOUT, Integer.class, context);
	        HttpConnectionParams.setConnectionTimeout(params, timeout * 1000);
	        HttpConnectionParams.setSoTimeout(params, timeout * 1000);
	        
	        return new DefaultHttpClient(params);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Metodo que obtiene un cliente https
	 * @param context
	 * @return
	 */
	private static HttpClient getHttpsClient(Context context) {
	     try {
			   X509TrustManager x509TrustManager = new X509TrustManager() { 	           
					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
	 
					@Override
					public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException {}
	 
					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
		        };
		        
		        HttpClient client = getHttpClient(context);
		        SSLContext sslContext = SSLContext.getInstance("TLS");
		        sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
		        SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
		        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		        ClientConnectionManager clientConnectionManager = client.getConnectionManager();
		        SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
		        schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
		        return new DefaultHttpClient(clientConnectionManager, client.getParams());
	     } catch (Exception ex) {
	    	 return null;
		 }
	}
	
	/**
	 * Metodo que obtiene el cliente http para la conexion con el servicio Web
	 * @param context
	 * @return
	 */
	public static HttpClient getClient(Context context) {
		if (Boolean.TRUE.equals(PreferencesUtils.getPreferences(PreferencesTypes.SYNC_HTTPS, Boolean.class, context))) {
			return getHttpsClient(context);
		} else {
			return getHttpClient(context);
		}
	}
}
