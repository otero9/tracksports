package com.udc.master.tfm.tracksports.directcom.rest.object;

import java.io.Serializable;

/**
 * Clase que representa la respuesta de edicion de un usuario
 * @author a.oteroc
 *
 */
public class EditResponseWS implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Resultado de la edicion del usuario */
	private Boolean resultado;
	/** Codigo de error del usuario */
	private Integer codigoError;
	
	public EditResponseWS() {}

	/**
	 * @return the resultado
	 */
	public Boolean getResultado() {
		return resultado;
	}

	/**
	 * @param resultado the resultado to set
	 */
	public void setResultado(Boolean resultado) {
		this.resultado = resultado;
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
		return "EditResponseWS [resultado=" + resultado + ", codigoError="
				+ codigoError + "]";
	}
}
