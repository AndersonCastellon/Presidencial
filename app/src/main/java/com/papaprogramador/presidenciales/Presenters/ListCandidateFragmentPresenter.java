package com.papaprogramador.presidenciales.Presenters;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.CandidateFragment;
import com.papaprogramador.presidenciales.Modelos.CandidateListCallbackFirebase;
import com.papaprogramador.presidenciales.Objetos.Candidato;

import java.util.List;

public class ListCandidateFragmentPresenter extends MvpBasePresenter<CandidateFragment.View>
		implements CandidateFragment.Presenter {

	public ListCandidateFragmentPresenter() {
	}

	@Override
	public void getListCandidateFromFirebase(final boolean pullToRefresh) {
		ifViewAttached(new ViewAction<CandidateFragment.View>() {
			@Override
			public void run(@NonNull final CandidateFragment.View view) {
				new CandidateListCallbackFirebase(new CandidateListCallbackFirebase.ListCandidateListener() {
					@Override
					public void onSuccess(List<Candidato> candidatoList) {
						view.setData(candidatoList);
						view.showContent();
					}

					@Override
					public void onError(Exception e) {
						view.showError(e, pullToRefresh);
					}
				});
			}
		});
	}
}
