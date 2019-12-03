package com.udc.master.tfm.tracksports.common.spinner;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.udc.master.tfm.tracksports.R;

/**
 * Adaptador para mostrar la informacion de un spinner
 * @author a.oteroc
 *
 */
public class SpinnerArrayAdapter extends ArrayAdapter<SpinnerItem> {
	/** Contexto de la aplicacion */
	private final Context context;
	/** Lista de elementos del spinner*/
	private List<SpinnerItem> items;
	
	/**
	 * Constructor de la clase
	 * @param context
	 * @param listItems
	 * @param mapItems
	 */
	public SpinnerArrayAdapter (Context context, List<SpinnerItem> items) {
		super(context, R.layout.spinner_element, items);
		this.context = context;
		this.items = items;
	}

    @Override
    public int getCount() {
        return this.items.size();
    }
 
    @Override
    public SpinnerItem getItem(int position) {
        return this.items.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    /**
	 * @return the items
	 */
	public List<SpinnerItem> getItems() {
		return items;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View rowView = convertView;

    	//Se crea la nueva vista para el elemento dentro de la lista
    	if (convertView == null) {
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		rowView = inflater.inflate(R.layout.spinner_element, parent, false);
    	}
        
    	//Se setea el elemento con la informacion del objeto
    	ImageView imageSpinner = (ImageView) rowView.findViewById(R.id.image_spinner);
    	TextView textTitle = (TextView) rowView.findViewById(R.id.textView_title_spinner);
    	TextView textContent = (TextView) rowView.findViewById(R.id.textView_content_spinner);
    	
    	SpinnerItem item = items.get(position);
    	textTitle.setText(item.getTitle());
    	imageSpinner.setImageDrawable(item.getImage());
    	textContent.setText(item.getTextView().getText());
    	
    	return rowView;
    }
    
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
    	return getView(position, convertView, parent);
    }
}
