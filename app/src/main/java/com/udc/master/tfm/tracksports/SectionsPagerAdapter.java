package com.udc.master.tfm.tracksports;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;
import java.util.List;

/**
 * Adaptador encargar de crear y devolver los fragmentos para que los trate
 * el <code>ViewPager</code>
 * @author a.oteroc
 *
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	/** Lista de fragmentos que componen la aplicacion */
	private List<Fragment> fragments;
	
	/**
	 * Constructor del adaptador
	 * @param fm
	 * @param fragments
	 */
	public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public String getPageTitle(int position) {
		return fragments.get(position).getClass().getSimpleName();
    }
}
