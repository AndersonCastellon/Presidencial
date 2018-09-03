package com.papaprogramador.presidenciales.opinionsModule;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;
import com.papaprogramador.presidenciales.common.pojo.Opinion;

public interface OpinionsContract {

	interface View extends MvpView {
		void showProgress(boolean show);

		void add(Opinion opinion);
		void update(Opinion opinion);
		void remove(Opinion opinion);
		void onComplete();

		void removeFail();
		void onShowError(int resMsg);
	}

	interface Presenter extends MvpPresenter<OpinionsContract.View> {
		void onCreate();
		void onPause();
		void onResume(long lastOpinion);
		void onDestroy();

		void getData(long lastOpinion);

		void remove (Opinion opinion);
		void onDataEventListener(OpinionEvent event);
	}

	interface Interactor {
		void subscribeToOpinions(long lastOpinion);
		void unsubscribeToOpinions();

		void removeOpinion(Opinion opinion);
	}
}