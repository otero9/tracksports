package com.udc.master.tfm.tracksports;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

/**
 * Factoria para la creacion de los Tabs
 * @author a.oteroc
 *
 */
public class TabFactory implements TabContentFactory {

    private final Context mContext;
    
    /**
     * Construcotr de la factoria
     * @param context
     */
    public TabFactory(Context context) {
        mContext = context;
    }
	
	@Override
	public View createTabContent(String arg0) {
        View v = new View(mContext);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        return v;
	}
}
