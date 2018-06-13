package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface DetailCandidato {
	interface View extends MvpView {

	}

	interface Presenter extends MvpPresenter<DetailCandidato.View> {

	}

	interface Model {

	}
}
