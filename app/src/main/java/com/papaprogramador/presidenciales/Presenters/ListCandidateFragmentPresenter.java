package com.papaprogramador.presidenciales.Presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.CandidateFragment;
import com.papaprogramador.presidenciales.UseCases.CandidateListCallbackFirebase;
import com.papaprogramador.presidenciales.common.pojo.Candidate;
import com.papaprogramador.presidenciales.Utils.StaticMethods.CheckConnection;

import java.util.List;

public class ListCandidateFragmentPresenter extends MvpBasePresenter<CandidateFragment.View>
		implements CandidateFragment.Presenter {

	private Context context;

	public ListCandidateFragmentPresenter(Context context) {
		this.context = context;
	}

	@Override
	public void getListCandidateFromFirebase(final boolean pullToRefresh) {

		if (!CheckConnection.checkConnection(context)){
			ifViewAttached(new ViewAction<CandidateFragment.View>() {
				@Override
				public void run(@NonNull CandidateFragment.View view) {
					view.showError(new Exception("NullConnectionException"), pullToRefresh);
				}
			});
		}else {
			ifViewAttached(new ViewAction<CandidateFragment.View>() {
				@Override
				public void run(@NonNull final CandidateFragment.View view) {
					new CandidateListCallbackFirebase(new CandidateListCallbackFirebase.ListCandidateListener() {
						@Override
						public void onSuccess(List<Candidate> candidateList) {
							view.setData(candidateList);
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
}
