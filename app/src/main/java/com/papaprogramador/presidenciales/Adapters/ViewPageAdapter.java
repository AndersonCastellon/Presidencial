package com.papaprogramador.presidenciales.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.papaprogramador.presidenciales.View.Fragments.CandidateFragmentView;
import com.papaprogramador.presidenciales.TreeMvp.Opinions.OpinionsView;
import com.papaprogramador.presidenciales.View.Fragments.ResultadosFragment;

public class ViewPageAdapter extends FragmentStatePagerAdapter {

	private int tabSize;

	public ViewPageAdapter(FragmentManager fm, int tabSize) {
		super(fm);
		this.tabSize = tabSize;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position){
			case 0:
			return new CandidateFragmentView();
			case 1:
			return  new OpinionsView();
			default:
				return new CandidateFragmentView();
		}
	}

	@Override
	public int getCount() {
		return tabSize;
	}
}
