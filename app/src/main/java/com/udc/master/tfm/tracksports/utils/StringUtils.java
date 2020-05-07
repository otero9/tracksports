package com.udc.master.tfm.tracksports.utils;

/**
 * Clase de utilidad para el tratamiento de Strings
 * @author a.oteroc
 *
 */
public class StringUtils {

	private StringUtils() {}
	
	/**
	 * Metodo que comprueba si un string es vacio
	 * @param text
	 * @return
	 */
	public static boolean isBlank(String text) {
		return text == null || text.trim().length() == 0;
	}
	
	/**
	 * Metodo que comprueba is un conjunto de caracteres son vacios
	 * @param charSequence
	 * @return
	 */
	public static boolean isBlank(CharSequence charSequence) {
		return isBlank(charSequence.toString());
	}
}
