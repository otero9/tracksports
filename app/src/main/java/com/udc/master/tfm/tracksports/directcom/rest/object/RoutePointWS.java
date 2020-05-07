package com.udc.master.tfm.tracksports.directcom.rest.object;

import java.io.Serializable;

/**
 * Clase que representa un punto de la ruta en la comunicacion del webService
 * @author a.oteroc
 *
 */
public class RoutePointWS implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Latitud */
	private Double latitud;
	/** Longitud */
	private Double longitud;
	/** Altitud */
	private Double altitud;
	/** Velocidad */
	private Float velocidad;
	/** Ritmo */
	private Float ritmo;
	/** Distancia */
	private Float distancia;
	/** Tiempo */
	private Long tiempo;
	
	public RoutePointWS() {}

	/**
	 * @return the latitud
	 */
	public Double getLatitud() {
		return latitud;
	}

	/**
	 * @param latitud the latitud to set
	 */
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	/**
	 * @return the longitud
	 */
	public Double getLongitud() {
		return longitud;
	}

	/**
	 * @param longitud the longitud to set
	 */
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	/**
	 * @return the altitud
	 */
	public Double getAltitud() {
		return altitud;
	}

	/**
	 * @param altitud the altitud to set
	 */
	public void setAltitud(Double altitud) {
		this.altitud = altitud;
	}

	/**
	 * @return the velocidad
	 */
	public Float getVelocidad() {
		return velocidad;
	}

	/**
	 * @param velocidad the velocidad to set
	 */
	public void setVelocidad(Float velocidad) {
		this.velocidad = velocidad;
	}

	/**
	 * @return the ritmo
	 */
	public Float getRitmo() {
		return ritmo;
	}

	/**
	 * @param ritmo the ritmo to set
	 */
	public void setRitmo(Float ritmo) {
		this.ritmo = ritmo;
	}

	/**
	 * @return the distancia
	 */
	public Float getDistancia() {
		return distancia;
	}

	/**
	 * @param distancia the distancia to set
	 */
	public void setDistancia(Float distancia) {
		this.distancia = distancia;
	}

	/**
	 * @return the tiempo
	 */
	public Long getTiempo() {
		return tiempo;
	}

	/**
	 * @param tiempo the tiempo to set
	 */
	public void setTiempo(Long tiempo) {
		this.tiempo = tiempo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RoutePointWS [latitud=" + latitud + ", longitud=" + longitud
				+ ", altitud=" + altitud + ", velocidad=" + velocidad
				+ ", ritmo=" + ritmo + ", distancia=" + distancia + ", tiempo="
				+ tiempo + "]";
	}
}
