package com.udc.master.tfm.tracksports.graph;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Date;

import com.udc.master.tfm.tracksports.utils.DateUtils;

/**
 * Formateador para mostrar la hora y el minuto en una grafica
 * @author a.oteroc
 *
 */
public class GraphTimeFormat extends Format {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Hora de inicio del ejercicio */
	private Long startTime;
	
	/**
	 * Constructor vacio
	 */
	public GraphTimeFormat() {}
	
	/**
	 * Constructor por defecto
	 * @param startTime
	 */
	public GraphTimeFormat(Date startTime) {
		this.startTime = startTime.getTime();
	}
	
	@Override
	public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
		long parameterTime = ((Number)object).longValue();
		long elapsedTime = parameterTime - (startTime != null ? startTime : 0);
		long[] time = DateUtils.getTime(elapsedTime);
		if (buffer == null) {
			buffer = new StringBuffer();
		}
		buffer.append(time[0]);
		buffer.append(":");
		buffer.append(time[1]);
		return buffer;
	}

	@Override
	public Object parseObject(String string, ParsePosition position) {
		return null;
	}

}
