package com.udc.master.tfm.tracksports.common.dialog;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

/**
 * Objeto que representa un elemento de un dialogo de la aplicacion
 * informado con una imagen y un texto
 * @author a.oteroc
 *
 */
public class DialogItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Drawable image;
	private String text;
	
	/**
	 * Constructor vacio
	 */
	public DialogItem() {}
	
	/**
	 * Constructor de la clase
	 * @param image
	 * @param text
	 */
	public DialogItem(Drawable image, String text) {
		this.image = image;
		this.text = text;
	}

	/**
	 * @return the image
	 */
	public Drawable getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Drawable image) {
		this.image = image;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}
