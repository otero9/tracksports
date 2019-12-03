package com.udc.master.tfm.tracksports.utils;


/**
 * Clase de utilidad para el formateo de los valores a mostrar
 * @author a.oteroc
 *
 */
public class FormatUtils {

	private FormatUtils() {}
	
	/**
	 * Metodo que formatea la distancia
	 * @param distance
	 * @param kilometersMeasure
	 * @param metersMeasure
	 * @return
	 */
	public static String formatDistance(Float distance, String kilometersMeasure, String metersMeasure) {
    	StringBuilder sbDistance = new StringBuilder();
    	Integer meters = Integer.valueOf(String.format(DateUtils.DEFAULT_LOCALE, "%.0f", distance));
    	Integer kilometers = meters / 1000;
    	if (kilometers > 1) {
    		meters = meters % 1000;
    		sbDistance.append(kilometers);
    		sbDistance.append(" ");
    		sbDistance.append(kilometersMeasure);
    		sbDistance.append(" ");
    	}
    	sbDistance.append(meters);
    	sbDistance.append(" ");
    	sbDistance.append(metersMeasure);
    	return sbDistance.toString();
	}
	
	/**
	 * Metodo que formatea el tiempo
	 * @param time
	 * @param showSeconds
	 * @param hour
	 * @param min
	 * @param sec
	 * @return
	 */
	public static String formatTime(Long time, boolean showSeconds, String hour, String min, String sec) {
    	long[] duration = DateUtils.getTime(time);
    	long hours = duration[0];
    	long minutes = duration[1];
    	long seconds = duration[2];
    	
    	StringBuilder sbDuration = new StringBuilder();
    	boolean enter = false;
    	if (hours > 0) {
    		sbDuration.append(hours + " " + hour);
    		enter = true;
    	}
    	if (minutes > 0) {
    		if (enter) {
    			sbDuration.append(" ");
    		}
    		sbDuration.append(minutes + " " + min);
    		enter = true;
    	}
    	if (seconds > 0 && showSeconds) {
    		if (enter) {
    			sbDuration.append(" ");
    		}
    		sbDuration.append(seconds + " " + sec);
    		enter = true;
    	}
    	if (!enter) {
    		sbDuration.append("0 ");
    		if (showSeconds) {
    			sbDuration.append(sec);
    		} else {
    			sbDuration.append(min);
    		}
    	}
    	return sbDuration.toString();
	}
	
	/**
	 * Metodo que formatea un tiempo en MM:SS
	 * @param time
	 * @param showMiliseconds
	 * @return
	 */
	public static String formatTime(Long time, boolean showMiliseconds) {
    	long[] duration = DateUtils.getTime(time);
    	long minutes = duration[1];
    	long seconds = duration[2];
    	long miliseconds = duration[3];
    	
    	StringBuilder sbDuration = new StringBuilder();
    	if (minutes > 0) {
    		if (minutes < 10) {
    			sbDuration.append("0");
    		}
    		sbDuration.append(minutes);
    	} else {
    		sbDuration.append("00");
    	}
    	sbDuration.append(":");
    	if (seconds > 0) {
    		if (seconds < 10) {
    			sbDuration.append("0");
    		}
    		sbDuration.append(seconds);
    	} else {
    		sbDuration.append("00");
    	}
    	if (showMiliseconds) {
    		sbDuration.append(".");
        	if (miliseconds > 0) {
        		if (miliseconds >= 10 && miliseconds < 100) {
        			sbDuration.append("0");
        		} else if (miliseconds < 10) {
        			sbDuration.append("00");
        		}
        		sbDuration.append(miliseconds);
        	} else {
        		sbDuration.append("000");
        	}
    	}
    	return sbDuration.toString();
	}
}
