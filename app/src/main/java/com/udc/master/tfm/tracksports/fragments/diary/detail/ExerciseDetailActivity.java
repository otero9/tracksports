package com.udc.master.tfm.tracksports.fragments.diary.detail;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.activity.Exercise;
import com.udc.master.tfm.tracksports.bbdd.activity.ExerciseDAO;
import com.udc.master.tfm.tracksports.bbdd.factory.DatabaseFactory;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;

/**
 * Actividad para mostrar el detalle de un ejercicio
 * @author a.oteroc
 *
 */
public class ExerciseDetailActivity extends FragmentActivity {

	/** Vista con los distintos fragmentos*/
	private FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_detail);
		
		mTabHost = (FragmentTabHost)findViewById(R.id.tabhost_exercise_detail);
		
		initialiteTabHost();
		
		//Se obtiene la ruta tomada durante el ejercicio
		Exercise exercise = (Exercise) getIntent().getSerializableExtra(ConstantsUtils.EXERCISE_PARAM);
		ExerciseDAO exerciseHelper = DatabaseFactory.getInstance().getExerciseDAO(this);
		exerciseHelper.findRoute(exercise);
	}
	
	/**
	 * Metodo que inicializa el conjunto de Tabs
	 */
	private void initialiteTabHost() {	
		mTabHost.setup(getApplicationContext(), getSupportFragmentManager(), android.R.id.tabcontent);
		TabSpec tabSummary = mTabHost.newTabSpec("tab1").setIndicator(getString(R.string.tab_exercise_summary));
		TabSpec tabGraph = mTabHost.newTabSpec("tab2").setIndicator(getString(R.string.tab_exercise_graph));
		TabSpec tabRoute = mTabHost.newTabSpec("tab3").setIndicator(getString(R.string.tab_exercise_route));
		
		mTabHost.addTab(tabSummary, ExerciseSummaryFragment.class, null);
		mTabHost.addTab(tabGraph, ExerciseGraphFragment.class, null);
		mTabHost.addTab(tabRoute, ExerciseRouteFragment.class, null);
		
		//Se inicializa el color de fondo
		TabWidget widget = mTabHost.getTabWidget();
		for(int i = 0; i < widget.getChildCount(); i++) {
		    View v = widget.getChildAt(i);

		    // Look for the title view to ensure this is an indicator and not a divider.
		    TextView tv = (TextView)v.findViewById(android.R.id.title);
		    if(tv == null) {
		        continue;
		    }
		    v.setBackgroundResource(R.drawable.tab_indicator_holo);
		}
	}
}
