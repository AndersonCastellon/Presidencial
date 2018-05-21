package com.papaprogramador.presidenciales2019.Adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.papaprogramador.presidenciales2019.Vista.Fragmentos.CandidatosFragment;
import com.papaprogramador.presidenciales2019.Vista.Fragmentos.OpinionesFragment;
import com.papaprogramador.presidenciales2019.Vista.Fragmentos.ResultadosFragment;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {

	private int numerodetabs;

	public ViewpagerAdapter(FragmentManager fm, int numerodetabs) {
		super(fm);
		this.numerodetabs = numerodetabs;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position){
			case 0:
			return new CandidatosFragment();
			case 1:
			return  new OpinionesFragment();
			case 2:
			return  new ResultadosFragment();
			default:
				return new CandidatosFragment();

		}
	}

	@Override
	public int getCount() {
		return numerodetabs;
	}
}
