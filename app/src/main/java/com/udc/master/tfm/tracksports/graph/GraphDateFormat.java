package com.udc.master.tfm.tracksports.graph;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Date;

import com.udc.master.tfm.tracksports.utils.DateUtils;

/**
 * Formateador para mostrar el dia y el mes en una grafica
 * @author a.oteroc
 *
 */
public class GraphDateFormat extends Format {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
		long parameterTime = ((Number)object).longValue();
		Date date = new Date(parameterTime);
		return DateUtils.getDayMontFormatter().format(date, buffer, field);
	}

	@Override
	public Object parseObject(String string, ParsePosition position) {
		return null;
	}

}
