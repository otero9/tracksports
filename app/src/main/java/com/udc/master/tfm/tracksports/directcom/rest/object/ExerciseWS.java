package com.udc.master.tfm.tracksports.directcom.rest.object;

import java.io.Serializable;
import java.util.List;

/**
 * Clase que representa un ejecicio en la comunicacion webService
 * @author a.oteroc
 *
 */
public class ExerciseWS implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Hora de inicio */
	private String horaInicio;
	/** Duracion */
	private Long duracion;
	/** Distancia */
	private Float distancia;
	/** Velocidad media */
	private Float velocidadMedia;
	/** Velocidad maxima */
	private Float velocidadMaxima;
	/** Ritmo medio */
	private Float ritmoMedio;
	/** Altitud minima */
	private Double altitudMinima;
	/** Altitud maxima */
	private Double altitudMaxima;
	/** Calorias quemadas */
	private Float caloriasQuemadas;
	/** Pasos */
	private Long pasos;
	/** Ruta */
	private List<RoutePointWS> ruta;
	
	public ExerciseWS() {}

	/**
	 * @return the horaInicio
	 */
	public String getHoraInicio() {
		return horaInicio;
	}

	/**
	 * @param horaInicio the horaInicio to set
	 */
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * @return the duracion
	 */
	public Long getDuracion() {
		return duracion;
	}

	/**
	 * @param duracion the duracion to set
	 */
	public void setDuracion(Long duracion) {
		this.duracion = duracion;
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
	 * @return the velocidadMedia
	 */
	public Float getVelocidadMedia() {
		return velocidadMedia;
	}

	/**
	 * @param velocidadMedia the velocidadMedia to set
	 */
	public void setVelocidadMedia(Float velocidadMedia) {
		this.velocidadMedia = velocidadMedia;
	}

	/**
	 * @return the velocidadMaxima
	 */
	public Float getVelocidadMaxima() {
		return velocidadMaxima;
	}

	/**
	 * @param velocidadMaxima the velocidadMaxima to set
	 */
	public void setVelocidadMaxima(Float velocidadMaxima) {
		this.velocidadMaxima = velocidadMaxima;
	}

	/**
	 * @return the ritmoMedio
	 */
	public Float getRitmoMedio() {
		return ritmoMedio;
	}

	/**
	 * @param ritmoMedio the ritmoMedio to set
	 */
	public void setRitmoMedio(Float ritmoMedio) {
		this.ritmoMedio = ritmoMedio;
	}

	/**
	 * @return the altitudMinima
	 */
	public Double getAltitudMinima() {
		return altitudMinima;
	}

	/**
	 * @param altitudMinima the altitudMinima to set
	 */
	public void setAltitudMinima(Double altitudMinima) {
		this.altitudMinima = altitudMinima;
	}

	/**
	 * @return the altitudMaxima
	 */
	public Double getAltitudMaxima() {
		return altitudMaxima;
	}

	/**
	 * @param altitudMaxima the altitudMaxima to set
	 */
	public void setAltitudMaxima(Double altitudMaxima) {
		this.altitudMaxima = altitudMaxima;
	}

	/**
	 * @return the caloriasQuemadas
	 */
	public Float getCaloriasQuemadas() {
		return caloriasQuemadas;
	}

	/**
	 * @param caloriasQuemadas the caloriasQuemadas to set
	 */
	public void setCaloriasQuemadas(Float caloriasQuemadas) {
		this.caloriasQuemadas = caloriasQuemadas;
	}

	/**
	 * @return the pasos
	 */
	public Long getPasos() {
		return pasos;
	}

	/**
	 * @param pasos the pasos to set
	 */
	public void setPasos(Long pasos) {
		this.pasos = pasos;
	}

	/**
	 * @return the ruta
	 */
	public List<RoutePointWS> getRuta() {
		return ruta;
	}

	/**
	 * @param ruta the ruta to set
	 */
	public void setRuta(List<RoutePointWS> ruta) {
		this.ruta = ruta;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExerciseWS [horaInicio=" + horaInicio + ", duracion="
				+ duracion + ", distancia=" + distancia + ", velocidadMedia="
				+ velocidadMedia + ", velocidadMaxima=" + velocidadMaxima
				+ ", ritmoMedio=" + ritmoMedio + ", altitudMinima="
				+ altitudMinima + ", altitudMaxima=" + altitudMaxima
				+ ", caloriasQuemadas=" + caloriasQuemadas + ", pasos=" + pasos
				+ ", ruta=" + ruta + "]";
	}
}
