package com.papaprogramador.presidenciales.TreeMvp.NewOpinion;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface NewOpinionContract {

	interface View extends MvpView {

	}

	interface Presenter extends MvpPresenter<NewOpinionContract.View> {

	}
}
