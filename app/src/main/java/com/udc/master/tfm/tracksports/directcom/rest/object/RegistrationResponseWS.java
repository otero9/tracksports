package com.udc.master.tfm.tracksports.directcom.rest.object;

import java.io.Serializable;

/**
 * Clase que representa la respuesta de registro contra el servicio Web
 * @author a.oteroc
 *
 */
public class RegistrationResponseWS implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Guid del usuario*/
	private String idUsuario;
	/** Codigo de error del usuario */
	private Integer codigoError;
	
	public RegistrationResponseWS() {}

	/**
	 * @return the idUsuario
	 */
	public String getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the codigoError
	 */
	public Integer getCodigoError() {
		return codigoError;
	}

	/**
	 * @param codigoError the codigoError to set
	 */
	public void setCodigoError(Integer codigoError) {
		this.codigoError = codigoError;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegistrationResponseWS [idUsuario=" + idUsuario
				+ ", codigoError=" + codigoError + "]";
	}
}
