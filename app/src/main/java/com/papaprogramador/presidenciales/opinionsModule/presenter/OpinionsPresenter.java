package com.papaprogramador.presidenciales.opinionsModule.presenter;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseUserAPI;
import com.papaprogramador.presidenciales.common.pojo.Like;
import com.papaprogramador.presidenciales.opinionsModule.events.LikeEvent;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.opinionsModule.OpinionsContract;
import com.papaprogramador.presidenciales.opinionsModule.model.OpinionsInteractor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class OpinionsPresenter extends MvpBasePresenter<OpinionsContract.View>
		implements OpinionsContract.Presenter {

	private OpinionsInteractor opinionsInteractor;
	private FirebaseUserAPI firebaseUserAPI;

	public OpinionsPresenter() {
		this.opinionsInteractor = new OpinionsInteractor();
		firebaseUserAPI = FirebaseUserAPI.getInstance();
	}

	@Override
	public void onCreate() {
		EventBus.getDefault().register(this);
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void addOpinionNotifiers() {
		opinionsInteractor.addOpinionNotifier();
	}

	@Override
	public void removeOpinionNotifiers() {
		opinionsInteractor.removeOpinionNotifier();
	}

	@Override
	public void getData(final long timeStamp) {
		ifViewAttached(new ViewAction<OpinionsContract.View>() {
			@Override
			public void run(@NonNull OpinionsContract.View view) {
				opinionsInteractor.getOpinions(timeStamp);
			}
		});
	}

	@Override
	public void addLikeNotifiers(String opinionId) {
		opinionsInteractor.addLikeNotifier(opinionId);
	}

	@Override
	public void removeLikeNotifiers(String opinionId) {
		opinionsInteractor.removeLikeNotifier(opinionId);
	}

	@Override
	public void onLikeClick(Opinion opinion) {
		Like like = Like.Builder().opinionId(opinion.getOpinionId())
				.userId(firebaseUserAPI.getUserId())
				.build();

		opinionsInteractor.toggleLike(like);
	}

	@Override
	public void deleteOpinion(final Opinion opinion) {
		opinionsInteractor.deleteOpinion(opinion);
	}

	@Subscribe
	@Override
	public void updateOpinionLike(final LikeEvent event) {
		ifViewAttached(new ViewAction<OpinionsContract.View>() {
			@Override
			public void run(@NonNull OpinionsContract.View view) {
				String opinionId = event.getLike().getOpinionId();
				String userId = event.getLike().getUserId();
				switch (event.getTypeEvent()) {
					case LikeEvent.LIKE:
						view.updateOpinionLikeCounter(opinionId, userId, true);
						break;
					case LikeEvent.DISLIKE:
						view.updateOpinionLikeCounter(opinionId, userId, false);
						break;
					case LikeEvent.ERROR:
						view.onShowError(event.getResMsg());
						break;
				}
			}
		});
	}

	@Subscribe
	@Override
	public void onDataOpinionListener(final OpinionEvent event) {
		ifViewAttached(new ViewAction<OpinionsContract.View>() {
			@Override
			public void run(@NonNull OpinionsContract.View view) {
				view.showProgress(false);
				switch (event.getTypeEvent()) {
					case OpinionEvent.INITIAL_DATA:
						view.addAll(event.getOpinions());
						view.requestAddOpinionNotifiers();
						view.requestAddLikeNotifiers();
						break;
					case OpinionEvent.SUCCES_ADD:
						view.requestRemoveLikeNotifiers();
						view.add(event.getOpinion());
						view.requestRemoveLikeNotifiers();
						view.onComplete();
						break;
					case OpinionEvent.SUCCES_UPDATE:
						view.requestRemoveLikeNotifiers();
						view.update(event.getOpinion());
						view.requestAddLikeNotifiers();
						view.onComplete();
						break;
					case OpinionEvent.SUCCES_REMOVE:
						view.requestRemoveLikeNotifiers();
						view.remove(event.getOpinion());
						view.requestAddLikeNotifiers();
						view.onComplete();
						break;
					case OpinionEvent.ERROR_SERVER:
						view.onShowError(event.getResMsg());
						view.onComplete();
						break;
					case OpinionEvent.ERROR_TO_REMOVE:
						view.removeFail();
						view.onComplete();
						break;
					case OpinionEvent.ON_COMPLETE:
						view.showProgress(false);
						view.onComplete();
						break;
				}
			}
		});
	}
}
