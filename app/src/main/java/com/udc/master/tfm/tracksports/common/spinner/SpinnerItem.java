package com.udc.master.tfm.tracksports.common.spinner;

import java.io.Serializable;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

public class SpinnerItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	private Drawable image;
	private TextView textView;
	
	public SpinnerItem() {}
	
	public SpinnerItem(String title, Drawable image, TextView textView) {
		this.title = title;
		this.image = image;
		this.textView = textView;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @return the textView
	 */
	public TextView getTextView() {
		return textView;
	}

	/**
	 * @param textView the textView to set
	 */
	public void setTextView(TextView textView) {
		this.textView = textView;
	}
	
	@Override
	public String toString() {
		return textView.getText().toString();
	}
}
