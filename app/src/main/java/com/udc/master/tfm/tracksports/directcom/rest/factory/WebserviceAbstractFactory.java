package com.udc.master.tfm.tracksports.directcom.rest.factory;

import android.content.Context;
import com.udc.master.tfm.tracksports.directcom.rest.WebCommunicationService;

/**
 * Factoria abstracta para obtener el Servicio de comunicacion con la plataforma Web
 * @author a.oteroc
 *
 */
public abstract class WebserviceAbstractFactory {

	/**
	 * Metodo que obtiene el servicio para instanciar la comunicacion con la plataforma Web
	 * @param context
	 * @return
	 */
	public abstract WebCommunicationService getWebCommunicationService(Context context);
}
