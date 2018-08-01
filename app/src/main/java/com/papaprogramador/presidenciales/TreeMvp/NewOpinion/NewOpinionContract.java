package com.papaprogramador.presidenciales.TreeMvp.NewOpinion;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface NewOpinionContract {

	interface View extends MvpView {
		void setToolbar();
		void setUserProfile(String userName, String uriPhotoProfile, String urlPoliticalFlag);
	}

	interface Presenter extends MvpPresenter<NewOpinionContract.View> {
		void setAuthListener();
		void removeAuthListener();
	}
}
