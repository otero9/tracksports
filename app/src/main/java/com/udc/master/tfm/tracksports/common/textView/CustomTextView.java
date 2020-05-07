package com.udc.master.tfm.tracksports.common.textView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * TextView que permitir definir un valor por defecto al crearlo y un tipo de medida a mostrar en la vista
 * @author a.oteroc
 *
 */
public class CustomTextView extends TextView implements DefaultTextInterface {

	/** Medida utilizada */
	private String measureType;
	/** String utilizado por defecto */
	private String defaultText;
	
	/**
	 * Constructor de la clase
	 * @param context
	 */
	public CustomTextView(Context context) {
		super(context);
	}
	
	/**
	 * 
	 * @param context
	 * @param defaultText
	 * @param measureType
	 */
	public CustomTextView(Context context, String defaultText, String measureType) {
		super(context);
		this.measureType = measureType;
		this.defaultText = defaultText;
		setText(defaultText);
	}
	
	/**
	 * 
	 * @param context
	 * @param defaultText
	 */
	public CustomTextView(Context context, String defaultText) {
		super(context);
		this.defaultText = defaultText;
		setText(defaultText);
	}
	
	/**
	 * Constructor de la clase
	 * @param context
	 * @param attributeSet
	 */
	public CustomTextView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}
	
	/**
	 * Constructor de la clase
	 * @param context
	 * @param attributeSet
	 * @param defStyleAttr
	 */
	public CustomTextView(Context context, AttributeSet attributeSet, int defStyleAttr) {
		super(context, attributeSet, defStyleAttr);
	}
	
	/**
	 * Metodo que setea el texto del TextView
	 * @param text
	 */
	public void setText(String text) {
		if (measureType != null) {
			super.setText(text + " " + measureType);
		} else {
			super.setText(text);
		}
	}

	@Override
	public void setDefaultText() {
		if (defaultText != null) {
			this.setText(defaultText);
		} else {
			super.setText("");
		}
	}
}
