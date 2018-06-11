package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface EmailVerify {
	interface View extends MvpView {
		void emailNoVerify();
		void errorSession();
		void goMainActivity();
		void showProgressBar(boolean show);
	}

	interface Presenter extends MvpPresenter<View> {
		void startIsEmailIsVerify(String emailUser, String pass);
	}
}
