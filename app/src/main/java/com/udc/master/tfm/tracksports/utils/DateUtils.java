package com.udc.master.tfm.tracksports.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Clase de utilidades para la fechas de la aplicacion
 * @author a.oteroc
 *
 */
public class DateUtils {
	
	/** Locale por defecto de la aplicacion*/
	public static final Locale DEFAULT_LOCALE = new Locale("es", "ES");
	
	/** Formateador de fechas por defecto */
	private static DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", DEFAULT_LOCALE);

	/** Formateador de fechas con horas */
	private static DateFormat dateHourFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", DEFAULT_LOCALE);
	
	/** Formateador de fechas con horas y minutos */
	private static DateFormat dateHourWithoutSecondsFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", DEFAULT_LOCALE);
	
	/** Formateador de fechas con horas */
	private static DateFormat dayMontFormatter = new SimpleDateFormat("dd/MM", DEFAULT_LOCALE);
	
	/** Formateador de fecha para almacenar en BBDD */
	private static DateFormat bbddDateHourFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAULT_LOCALE);
	
	/** Formateador de fecha en la comunicacion por servicio Web */
	private static DateFormat webServiceDateFormatter = new SimpleDateFormat("dd-MM-yyyy", DEFAULT_LOCALE);
	
	private DateUtils() {}
	
	/**
	 * @return the dateFormatter
	 */
	public static DateFormat getDateFormatter() {
		return dateFormatter;
	}
	
	/**
	 * @return the dateHourFormatter
	 */
	public static DateFormat getDateHourFormatter() {
		return dateHourFormatter;
	}

	/**
	 * @return the dateHourWithoutSecondsFormatter
	 */
	public static DateFormat getDateHourWithoutSecondsFormatter() {
		return dateHourWithoutSecondsFormatter;
	}

	/**
	 * @return the dayMontFormatter
	 */
	public static DateFormat getDayMontFormatter() {
		return dayMontFormatter;
	}

	/**
	 * @return the bbddDateHourFormatter
	 */
	public static DateFormat getBbddDateHourFormatter() {
		return bbddDateHourFormatter;
	}

	/**
	 * @return the webServiceDateFormatter
	 */
	public static DateFormat getWebServiceDateFormatter() {
		return webServiceDateFormatter;
	}

	/**
	 * Metodo que devuelve la fecha actual como un array:
	 * [0]: dia
	 * [1]: mes
	 * [2]: ano
	 * @return
	 */
	public static int [] getCurrentDate() {
		return getDate(null);
	}

	/**
	 * Metodo que devuelve la fecha especificada como un array:
	 * [0]: dia
	 * [1]: mes
	 * [2]: ano
	 * @return
	 */
	public static int [] getDate(Date selectDate) {
		Calendar c = Calendar.getInstance();
		if (selectDate != null) {
			c.setTime(selectDate);
		}
		int actualYear = c.get(Calendar.YEAR);
		int actualMonth = c.get(Calendar.MONTH);
		int actualDay = c.get(Calendar.DAY_OF_MONTH);
		int [] date = new int[3];
		date[0] = actualDay; 
		date[1] = actualMonth;
		date[2] = actualYear;
		return date;
	}
	
	/**
	 * Metodo que devuelve la fecha especificada como un array:
	 * [0]: hora
	 * [1]: minuto
	 * @return
	 */
	public static int [] getCurrentTime() {
		return getTime(null);
	}
	
	/**
	 * Metodo que devuelve la fecha especificada como un array:
	 * [0]: hora
	 * [1]: minuto
	 * @return
	 */
	public static int [] getTime(Date selectDate) {
		Calendar c = Calendar.getInstance();
		if (selectDate != null) {
			c.setTime(selectDate);
		}
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int [] date = new int[2];
		date[0] = hour; 
		date[1] = minute;
		return date;
	}
	
	/**
	 * Metodo que devuelve una fecha (en ms) desglosada en horas/minutos/segundos/milisegundos
	 * [0]: hora
	 * [1]: minuto
	 * [2]: segundo
	 * [3]: milisegundo
	 * @param time
	 * @return
	 */
	public static long[] getTime (long time) {
		long [] result = new long[4];
		
		long miliSeconds = time;
		long seconds = 0;
		long minutes = 0;
		long hours = 0;
		
		//Se calculan milisegundos, segundos, minutos y horas 
		if (miliSeconds >= 1000) {
			seconds = miliSeconds / 1000;
			miliSeconds = miliSeconds % 1000;
			if (seconds >= 60) {
				minutes = seconds / 60;
				seconds = seconds % 60;
				if (minutes >= 60) {
					hours = minutes / 60;
					minutes = minutes % 60;
					//Si se llega a las 24 horas corriendo se reinicia a 0
					if (hours >= 24) {
						hours = 0;
					}
				}
			}
		}
		result[0] = hours;
		result[1] = minutes;
		result[2] = seconds;
		result[3] = miliSeconds;
		return result;
	}
	
	/**
	 * Metodo que devuelve una fecha en formato String a partir del dia, mes y ano.
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 * @return
	 */
	public static String getStringDate(int dayOfMonth, int monthOfYear, int year) {
		StringBuilder sb = new StringBuilder();
		sb.append(dayOfMonth);
		sb.append("/");
		sb.append(monthOfYear);
		sb.append("/");
		sb.append(year);
		return sb.toString();
	}
	
	/**
	 * Metodo que obtiene la edad a partir de la fecha de nacimiento
	 * @param birthDate
	 * @return
	 */
	public static Integer getAge(Date birthDate) {
		Calendar today = Calendar.getInstance();
		Calendar birthday = Calendar.getInstance();
		birthday.setTime(birthDate);
		
		int factor = 0;
		//Si el mes es menor al actual se resta un ano
		if (today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {
			factor = -1;
		//Si el mes el el actual pero es dia menor re resta un ano
		//Si es el mismo dia se supone que ya cumplio anos
		} else if (today.get(Calendar.MONTH) == birthday.get(Calendar.MONTH)) {
			if (today.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH)) {
				factor = -1;
			}
		}
		return today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR) + factor;
	}
}
