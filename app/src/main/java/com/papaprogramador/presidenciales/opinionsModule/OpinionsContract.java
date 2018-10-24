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

		void addAll(List<Opinion> opinions);
		void add(Opinion opinion);
		void update(Opinion opinion);
		void remove(Opinion opinion);
		void updateOpinionLikeCounter(String opinionId, String userId, boolean result);
		void onComplete();

		void requestAddOpinionNotifiers();
		void requestRemoveOpinionNotifiers();
		void requestAddLikeNotifiers();
		void requestRemoveLikeNotifiers();

		void removeFail();
		void onShowError(int resMsg);
	}

	interface Presenter extends MvpPresenter<OpinionsContract.View> {
		void onCreate();
		void onResume();
		void onDestroy();

		void getData(long timeStamp);
		void addOpinionNotifiers();
		void removeOpinionNotifiers();
		void addLikeNotifiers(String opinionId);
		void removeLikeNotifiers(String opinionId);
		void updateOpinionLike(LikeEvent event);

		void onLikeClick(Opinion opinion);
		void deleteOpinion(Opinion opinion);
		void onDataOpinionListener(OpinionEvent event);
	}

	interface Interactor {
		void getOpinions(long timeStamp);

		void addOpinionNotifier();

		void removeOpinionNotifier();

		void toggleLike(Like like);

		void addLikeNotifier(String opinionId);

		void removeLikeNotifier(String opinionId);
		void deleteOpinion(Opinion opinion);
	}
}
