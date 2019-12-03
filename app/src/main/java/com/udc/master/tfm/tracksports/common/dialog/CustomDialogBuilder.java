package com.udc.master.tfm.tracksports.common.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import com.udc.master.tfm.tracksports.R;

/**
 * Clase para construir un dialogo propio
 * @author a.oteroc
 *
 */
public class CustomDialogBuilder extends Builder {
	
	private Context context;
	
	/**
	 * Constructor por defecto
	 * @param context
	 */
    public CustomDialogBuilder(Context context) {
    	super(new ContextThemeWrapper(context, R.style.dialogStyle));
    	this.context = context;
	}
    
    public AlertDialog show() {
    	AlertDialog alertDialog = super.show();
		// Set title divider color
    	setDividerColor(context, alertDialog);
		return alertDialog;
    }
    
    /**
     * Metodo que setea la linea divisoria con el color de la aplicacion a un <code>alertDialog</code>
     * @param context
     * @param alertDialog
     */
    public static void setDividerColor(Context context, AlertDialog alertDialog) {
		int titleDividerId = context.getResources().getIdentifier("titleDivider", "id", "android");
		View titleDivider = alertDialog.findViewById(titleDividerId);
		if (titleDivider != null) {
			titleDivider.setBackgroundColor(context.getResources().getColor(R.color.dark_green));
		}
    }
}
