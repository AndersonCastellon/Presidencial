package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.papaprogramador.presidenciales.Objetos.Candidato;

import java.util.List;

public interface CandidateFragment {

	interface View extends MvpLceView<List<Candidato>>{
	}

	interface Presenter extends MvpPresenter<CandidateFragment.View>{
		void getDataSnapshotCandidatosFirebase();
	}
}
