package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ResetPassword {

	interface View extends MvpView {
		void resetIsSuccesful();
		void emailNoExist();
		void emailIsEmpty();
		void emailIsInvalid();
	}

	interface Presenter extends MvpPresenter<View>{
		void emailUserProcess(String emailUsuario);
	}
}
