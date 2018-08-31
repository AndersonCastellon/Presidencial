package com.papaprogramador.presidenciales.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.papaprogramador.presidenciales.View.Fragments.CandidateFragmentView;
import com.papaprogramador.presidenciales.opinionsModule.view.OpinionsView;

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

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}
}
