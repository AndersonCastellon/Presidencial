package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface DetailCandidateContract {
	interface View extends MvpView {
		void onWebViewSettings();
		void getImgCandidate();
		void getToolbar();


	}

	interface Presenter extends MvpPresenter<DetailCandidateContract.View> {

	}

	interface Model {

	}
}
