package com.papaprogramador.presidenciales.TreeMvp.Opinions;

import com.google.firebase.database.DataSnapshot;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.papaprogramador.presidenciales.Obj.Opinions;

import java.util.List;

public interface OpinionsContract {

	interface View extends MvpLceView<List<Opinions>> {
		void setRefreshing();
	}

	interface Presenter extends MvpPresenter<OpinionsContract.View> {
		void getOpinions(long opinionId, boolean pullToRefresh);
		void getDataOpinions(long opinionId, boolean pullToRefresh);
		void getListOpinions(DataSnapshot dataSnapshot);
	}
}
