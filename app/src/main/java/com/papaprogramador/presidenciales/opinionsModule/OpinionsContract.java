package com.papaprogramador.presidenciales.opinionsModule;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.papaprogramador.presidenciales.common.pojo.Like;
import com.papaprogramador.presidenciales.opinionsModule.events.LikeEvent;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;
import com.papaprogramador.presidenciales.common.pojo.Opinion;

import java.util.List;

public interface OpinionsContract {

	interface View extends MvpView {
		void showProgress(boolean show);

		void add(Opinion opinion);
		void update(Opinion opinion);
		void remove(Opinion opinion);
		void updateOpinionLike(String opinionId, String userId, boolean result);
		void updateOpinionLikeCounter(String opinionId, String userId, boolean result);
		void onComplete();

		void requestAddLikeNotifiers();
		void requestRemoveLikeNotifiers();

		void removeFail();
		void onShowError(int resMsg);
	}

	interface Presenter extends MvpPresenter<OpinionsContract.View> {
		void onCreate();
		void onPause();
		void onResume(long lastOpinion, List<Opinion> opinionList);
		void onDestroy();

		void getData(long lastOpinion, List<Opinion> opinionList);
		void requestAddLikeNotifiers(String opinionId);
		void requestRemoveLikeNotifiers(String opinionId);
		void updateOpinionLike(LikeEvent event);

		void onLikeClick(Opinion opinion);
		void removeOpinion(Opinion opinion);
		void onDataOpinionListener(OpinionEvent event);
	}

	interface Interactor {
		void subscribeToOpinions(long lastOpinion, List<Opinion> opinionList);
		void unsubscribeToOpinions();

		void requestAddLikeNotifiers(String opinionId);
		void requestRemoveLikeNotifiers(String opinionId);
		void unsubscribeToLikes();

		void removeOpinion(Opinion opinion);
		void onClickLike(Like like);
	}
}
