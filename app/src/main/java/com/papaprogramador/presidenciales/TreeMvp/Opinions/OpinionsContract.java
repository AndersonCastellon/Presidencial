package com.papaprogramador.presidenciales.TreeMvp.Opinions;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.papaprogramador.presidenciales.Obj.Opinions;

import java.util.List;

public interface OpinionsContract {

	interface View extends MvpLceView<List<Opinions>> {

	}

	interface Presenter extends MvpPresenter<OpinionsContract.View> {
		void getOpinionsList(boolean pullToRefresh);
	}
}
