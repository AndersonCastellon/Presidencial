package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface EmailVerify {
	interface Vista extends MvpView {

	}

	interface Presentador extends MvpPresenter<EmailVerify.Vista> {

	}

	interface Modelo {

	}
}
