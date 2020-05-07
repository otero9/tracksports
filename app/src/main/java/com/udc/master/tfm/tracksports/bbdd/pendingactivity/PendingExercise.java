package com.udc.master.tfm.tracksports.bbdd.pendingactivity;

import java.io.Serializable;
import java.util.Date;

import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.utils.DateUtils;

/**
 * Clase que representa una actividad para ejecutar posteriormente
 * @author a.oteroc
 *
 */
public class PendingExercise implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Identificador del ejercicio pendiente en BBDD */
	private Long id;
	/** Hora de inicio del ejercicio pendiente */
	private Date startTime;
	/** Duracion total del ejercicio pendiente (en minutos) */
	private Long duration;
	/** Distancia total recorrida (en km) */
	private Float distance;
	/** Comentarios del ejercicio */
	private String comments;
	/** Perfil al que se asocia el ejercicio pendiente */
	private Profile profile;
	
	public PendingExercise() {}

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
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
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
		return "PendingExercise [id=" + id + ", startTime=" + startTime
				+ ", duration=" + duration + ", distance=" + distance
				+ ", comments=" + comments + ", profile=" + profile + "]";
	}
}
