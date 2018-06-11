package com.papaprogramador.presidenciales.InterfacesMVP;

import com.google.firebase.database.DataSnapshot;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.papaprogramador.presidenciales.Adaptadores.CandidatoAdapter;

public interface CandidatosFragment {

	interface View extends MvpView{
		void setRecyclerview();
		void setAdapterRecyclerview(CandidatoAdapter candidatoAdapter);
	}

	interface Presenter extends MvpPresenter<CandidatosFragment.View>{
		void setListCandidatoAdapter();
		void getDataSnapshotCandidatosFirebase();
		void addDataSnapshotToListCandidatoAdapter(DataSnapshot dataSnapshot);
	}
}
