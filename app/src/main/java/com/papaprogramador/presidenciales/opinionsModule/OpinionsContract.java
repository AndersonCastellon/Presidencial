package com.papaprogramador.presidenciales.opinionsModule;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.papaprogramador.presidenciales.opinionsModule.events.LikeEvent;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.common.pojo.Like;

public interface OpinionsContract {

	interface View extends MvpView {
		void showProgress(boolean show);

		void add(Opinion opinion);
		void update(Opinion opinion);
		void remove(Opinion opinion);
		void onComplete();

		void addLike(Like like);
		void removeLike(Like like);

		void removeFail();
		void onShowError(int resMsg);
	}

	interface Presenter extends MvpPresenter<OpinionsContract.View> {
		void onCreate();
		void onPause();
		void onResume(long lastOpinion);
		void onDestroy();

		void getData(long lastOpinion);

		void onLikeClick(Opinion opinion);
		void remove (Opinion opinion);
		void onDataOpinionListener(OpinionEvent event);
		void onDataLikesListener(LikeEvent event);
	}

	interface Interactor {
		void subscribeToOpinions(long lastOpinion);
		void unsubscribeToOpinions();
		void subscribeToLikes();
		void unsubscribeToLikes();

		void removeOpinion(Opinion opinion);
		void likeAdd(Opinion opinion);
		void likeRemove(Opinion opinion);
	}
}
