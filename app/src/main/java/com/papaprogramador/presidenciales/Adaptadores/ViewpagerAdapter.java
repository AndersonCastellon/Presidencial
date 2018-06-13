package com.papaprogramador.presidenciales.Adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.papaprogramador.presidenciales.Vistas.Fragmentos.CandidateFragmentView;
import com.papaprogramador.presidenciales.Vistas.Fragmentos.OpinionesFragment;
import com.papaprogramador.presidenciales.Vistas.Fragmentos.ResultadosFragment;

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
			return new CandidateFragmentView();
			case 1:
			return  new OpinionesFragment();
			case 2:
			return  new ResultadosFragment();
			default:
				return new CandidateFragmentView();

		}
	}

	@Override
	public int getCount() {
		return numerodetabs;
	}
}
