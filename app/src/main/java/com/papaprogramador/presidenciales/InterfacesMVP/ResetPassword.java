package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ResetPassword {

	interface Vista extends MvpView {
		void resetIsSuccesful();

		void emailNoExist();

		void emailIsEmpty();

		void emailIsInvalid();
	}

	interface Presentador extends MvpPresenter<ResetPassword.Vista>{
		void emailUserProcess(String emailUsuario);
	}
}
