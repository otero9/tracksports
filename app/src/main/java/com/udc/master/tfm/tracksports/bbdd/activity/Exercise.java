package com.udc.master.tfm.tracksports.bbdd.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.directcom.rest.object.ExerciseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.RoutePointWS;
import com.udc.master.tfm.tracksports.utils.DateUtils;

/**
 * Clase que representa una actividad de recorrido
 * @author a.oteroc
 *
 */
public class Exercise implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Identificador del ejercicio en BBDD */
	private Long id;
	/** Hora de inicio del ejercicio */
	private Date startTime;
	/** Duracion total del ejercicio */
	private Long duration = 0L;
	/** Distancia total recorrida (en m) */
	private Float distance = 0F;
	/** Velocidad media del recorrido (en km/h) */
	private Float speedAvg = 0F;
	/** Velocidad maxima del recorrido (en km/h) */
	private Float speedMax = 0F;
	/** Ritmo medio del recorrido (en km/h) */
	private Float speedPace = 0F;
	/** Maxima altitud del recorrido (en m) */
	private Double altitudeMin = 0D;
	/** Minima altitud del recorrido (en m) */
	private Double altitudeMax = 0D;
	/** Calorias quemadas durante el ejercicio */
	private Float caloriesBurned = 0F;
	/** Pasos caminados en el ejercicio */
	private Long steps = 0L;
	/** Estado de la sincronizacion del ejercicio contra el servicio Web */
	private HttpStatusType syncStatus;
	/** Lista de puntos con la ruta recorrida */
	private List<MapPosition> route = new ArrayList<MapPosition>();
	/** Perfil al que se asocia el ejercicio */
	private Profile profile;
	
	/**
	 * Constructor de la clase
	 */
	public Exercise() {}

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
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * 
	 * @return the startTime in text format
	 */
	public String getTextStartTime() {
		return DateUtils.getDateHourFormatter().format(startTime);
	}
	
	/**
	 * 
	 * @return the startTime in text format
	 */
	public String getBbddTextStartTime() {
		return DateUtils.getBbddDateHourFormatter().format(startTime);
	}

	/**
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
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
	 * @return the speedAvg
	 */
	public Float getSpeedAvg() {
		return speedAvg;
	}

	/**
	 * @param speedAvg the speedAvg to set
	 */
	public void setSpeedAvg(Float speedAvg) {
		this.speedAvg = speedAvg;
	}

	/**
	 * @return the speedMax
	 */
	public Float getSpeedMax() {
		return speedMax;
	}

	/**
	 * @param speedMax the speedMax to set
	 */
	public void setSpeedMax(Float speedMax) {
		this.speedMax = speedMax;
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
	 * @return the altitudeMin
	 */
	public Double getAltitudeMin() {
		return altitudeMin;
	}

	/**
	 * @param altitudeMin the altitudeMin to set
	 */
	public void setAltitudeMin(Double altitudeMin) {
		this.altitudeMin = altitudeMin;
	}

	/**
	 * @return the altitudeMax
	 */
	public Double getAltitudeMax() {
		return altitudeMax;
	}

	/**
	 * @param altitudeMax the altitudeMax to set
	 */
	public void setAltitudeMax(Double altitudeMax) {
		this.altitudeMax = altitudeMax;
	}

	/**
	 * @return the caloriesBurned
	 */
	public Float getCaloriesBurned() {
		return caloriesBurned;
	}

	/**
	 * @param caloriesBurned the caloriesBurned to set
	 */
	public void setCaloriesBurned(Float caloriesBurned) {
		this.caloriesBurned = caloriesBurned;
	}

	/**
	 * @return the steps
	 */
	public Long getSteps() {
		return steps;
	}

	/**
	 * @param steps the steps to set
	 */
	public void setSteps(Long steps) {
		this.steps = steps;
	}

	/**
	 * @return the syncStatus
	 */
	public HttpStatusType getSyncStatus() {
		return syncStatus;
	}

	/**
	 * @param syncStatus the syncStatus to set
	 */
	public void setSyncStatus(HttpStatusType syncStatus) {
		this.syncStatus = syncStatus;
	}

	/**
	 * @return the route
	 */
	public List<MapPosition> getRoute() {
		return route;
	}

	/**
	 * @param route the route to set
	 */
	public void setRoute(List<MapPosition> route) {
		this.route = route;
	}
	
	/**
	 * Metodo que anade un punto a la ruta
	 * @param mapPosition
	 */
	public void addRoutePosition(MapPosition mapPosition) {
		if (this.route == null) {
			this.route = new ArrayList<MapPosition>();
		}
		this.route.add(mapPosition);
	}

	/**
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Exercise [id=" + id + ", startTime=" + startTime
				+ ", duration=" + duration + ", distance=" + distance
				+ ", speedAvg=" + speedAvg + ", speedMax=" + speedMax
				+ ", speedPace=" + speedPace + ", altitudeMin=" + altitudeMin
				+ ", altitudeMax=" + altitudeMax + ", caloriesBurned="
				+ caloriesBurned + ", steps=" + steps + ", syncStatus=" + syncStatus
				+ ", route=" + route + ", profile=" + profile + "]";
	}
	
	/**
	 * Metodo que convierte el objeto actual a un <code>ExerciseWS</code>
	 * @return
	 */
	public ExerciseWS getExerciseWS() {
		ExerciseWS exerciseWS = new ExerciseWS();
		exerciseWS.setAltitudMaxima(getAltitudeMax());
		exerciseWS.setAltitudMinima(getAltitudeMin());
		exerciseWS.setCaloriasQuemadas(getCaloriesBurned());
		exerciseWS.setDistancia(getDistance());
		exerciseWS.setDuracion(getDuration());
		exerciseWS.setPasos(getSteps());
		exerciseWS.setRitmoMedio(getSpeedPace());
		exerciseWS.setVelocidadMaxima(getSpeedMax());
		exerciseWS.setVelocidadMedia(getSpeedAvg());
		exerciseWS.setHoraInicio(DateUtils.getDateHourFormatter().format(getStartTime()));
		if (route != null && !route.isEmpty()) {
			List<RoutePointWS> routePoints = new ArrayList<RoutePointWS>();
			for (MapPosition mapPosition : route) {
				routePoints.add(mapPosition.getRoutePointWS());
			}
			exerciseWS.setRuta(routePoints);
		}
		return exerciseWS;
	}
}
