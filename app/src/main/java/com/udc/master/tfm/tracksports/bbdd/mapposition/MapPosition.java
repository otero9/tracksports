package com.udc.master.tfm.tracksports.bbdd.mapposition;

import java.io.Serializable;

import com.udc.master.tfm.tracksports.directcom.rest.object.RoutePointWS;
import android.location.Location;

/**
 * Clase que representa una coordenada del mapa en la aplicacion
 * @author a.oteroc
 *
 */
public class MapPosition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Identificador de BBDD de la coordenada */
	private Long id;
	/** Latitud */
	private Double latitude;
	/** Longitud */
	private Double longitude;
	/** Altitud */
	private Double altitude;
	/** Velocidad en el punto actual (en km/h) */
	private Float speed;
	/** Ritmo en el punto actual (min/km) */
	private Float speedPace;
	/** Distancia recorrida hasta el punto actual (en m) */
	private Float distance;
	/** Tiempo en que se guarda la coordenada */
	private Long time;
	
	/**
	 * Constructor vacio
	 */
	public MapPosition() {}
	
	/**
	 * Constructor con parametros
	 * @param latitude
	 * @param longitude
	 */
	public MapPosition(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * Constructor a partir de una localizacion
	 * @param location
	 */
	public MapPosition(Location location) {
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
		this.altitude = location.getAltitude();
	}

	/**
	 * Constructor con parametros
	 * @param latitude
	 * @param longitude
	 * @param altitude
	 * @param speed
	 * @param distance
	 */
	public MapPosition(Double latitude, Double longitude, Double altitude,
			Float speed, Float speedPace, Float distance, Long time) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.speed = speed;
		this.speedPace = speedPace;
		this.distance = distance;
		this.time = time;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the altitude
	 */
	public Double getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude the altitude to set
	 */
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	/**
	 * @return the speed
	 */
	public Float getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	/**
	 * @return the speedPace
	 */
	public Float getSpeedPace() {
		return speedPace;
	}

	/**
	 * @param speedPace the speedPace to set
	 */
	public void setSpeedPace(Float speedPace) {
		this.speedPace = speedPace;
	}

	/**
	 * @return the distance
	 */
	public Float getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Float distance) {
		this.distance = distance;
	}

	/**
	 * @return the time
	 */
	public Long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Long time) {
		this.time = time;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MapPosition [id=" + id + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", altitude=" + altitude
				+ ", speed=" + speed + ", speedPace=" + speedPace
				+ ", distance=" + distance + ", time=" + time + "]";
	}
	
	/**
	 * Metodo que convierte el objeto actual a un <code>RoutePointWS</code> 
	 * @return
	 */
	public RoutePointWS getRoutePointWS() {
		RoutePointWS routePointWS = new RoutePointWS();
		routePointWS.setAltitud(getAltitude());
		routePointWS.setDistancia(getDistance());
		routePointWS.setLatitud(getLatitude());
		routePointWS.setLongitud(getLongitude());
		routePointWS.setRitmo(getSpeedPace());
		routePointWS.setTiempo(getTime());
		routePointWS.setVelocidad(getSpeedPace());
		return routePointWS;
	}
}
