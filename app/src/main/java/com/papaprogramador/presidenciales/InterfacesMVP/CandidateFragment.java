package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.papaprogramador.presidenciales.Obj.Candidate;

import java.util.List;

public interface CandidateFragment {

	interface View extends MvpLceView<List<Candidate>>{
	}

	interface Presenter extends MvpPresenter<CandidateFragment.View>{
		void getListCandidateFromFirebase(boolean pullToRefresh);
	}
}
