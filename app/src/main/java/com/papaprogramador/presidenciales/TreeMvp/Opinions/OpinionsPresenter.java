package com.papaprogramador.presidenciales.TreeMvp.Opinions;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.UseCases.GetOpinionsList;
import com.papaprogramador.presidenciales.Utils.StaticMethods.CheckConnection;

import java.util.List;

public class OpinionsPresenter extends MvpBasePresenter<OpinionsContract.View> implements OpinionsContract.Presenter {

	private Context context;

	OpinionsPresenter(Context context) {
		this.context = context;
	}


	@Override
	public void getOpinionsList(final boolean pullToRefresh) {

		if (!CheckConnection.checkConnection(context)) {
			ifViewAttached(new ViewAction<OpinionsContract.View>() {
				@Override
				public void run(@NonNull OpinionsContract.View view) {
					view.showError(null, pullToRefresh);
				}
			});
		} else {
			ifViewAttached(new ViewAction<OpinionsContract.View>() {
				@Override
				public void run(@NonNull final OpinionsContract.View view) {
					GetOpinionsList opinionsList = new GetOpinionsList(new GetOpinionsList.OpinionListListener() {
						@Override
						public void onResult(List<Opinions> opinionsList) {
							view.setData(opinionsList);
							view.showContent();
						}

						@Override
						public void onError(DatabaseError error) {
							view.showError(null, pullToRefresh);
						}
					});
					opinionsList.getOpinionsList();
				}
			});
		}
	}
}
