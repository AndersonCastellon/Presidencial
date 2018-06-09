package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ListCandidatos {
	interface Vista extends MvpView {

	}

	interface Presentador extends MvpPresenter<ListCandidatos.Vista> {

	}

	interface Modelo {

	}
}
