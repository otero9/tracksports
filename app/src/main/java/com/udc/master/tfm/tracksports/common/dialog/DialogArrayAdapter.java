package com.udc.master.tfm.tracksports.common.dialog;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.udc.master.tfm.tracksports.R;

public class DialogArrayAdapter extends ArrayAdapter<DialogItem> {

	/** Contexto de la aplicacion */
	private final Context context;
	/** Lista de items */
	private List<DialogItem> items;
	
	/**
	 * Constructor del adaptador
	 * @param context
	 * @param profiles
	 */
	public DialogArrayAdapter (Context context, List<DialogItem> items) {
		super(context, R.layout.dialog_element, items);
		this.context = context;
		this.items = items;
	}
	
    @Override
    public int getCount() {
        return this.items.size();
    }
 
    @Override
    public DialogItem getItem(int position) {
        return this.items.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View rowView = convertView;

    	//Se crea la nueva vista para el elemento dentro de la lista
    	if (convertView == null) {
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		rowView = inflater.inflate(R.layout.dialog_element, parent, false);
    	}
        
    	//Se setea el elemento con la informacion del objeto
    	ImageView imageDialog = (ImageView) rowView.findViewById(R.id.image_dialog);
    	TextView textDialog = (TextView) rowView.findViewById(R.id.textView_dialog);

    	DialogItem item = items.get(position);
    	imageDialog.setImageDrawable(item.getImage());
    	textDialog.setText(item.getText());
    	
    	return rowView;
    }
}
