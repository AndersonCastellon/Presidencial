package com.papaprogramador.presidenciales.opinionsModule.presenter;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.opinionsModule.OpinionsContract;
import com.papaprogramador.presidenciales.opinionsModule.model.OpinionsInteractor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class OpinionsPresenter extends MvpBasePresenter<OpinionsContract.View>
		implements OpinionsContract.Presenter {

	private OpinionsInteractor interactor;

	public OpinionsPresenter() {
		this.interactor = new OpinionsInteractor();
	}

	@Override
	public void onCreate() {
		EventBus.getDefault().register(this);
	}

	@Override
	public void onPause() {
		interactor.unsubscribeToOpinions();
	}

	@Override
	public void onResume(final long lastOpinion) {
		interactor.subscribeToOpinions(lastOpinion);
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void getData(final long lastOpinion) {
		ifViewAttached(new ViewAction<OpinionsContract.View>() {
			@Override
			public void run(@NonNull OpinionsContract.View view) {
				interactor.subscribeToOpinions(lastOpinion);
			}
		});
	}

	@Override
	public void onLikeClick(Opinion opinion) {

	}

	@Override
	public void onCommentClick(Opinion opinion) {

	}

	@Override
	public void onShareClick(Opinion opinion) {

	}

	@Override
	public void remove(final Opinion opinion) {
		ifViewAttached(new ViewAction<OpinionsContract.View>() {
			@Override
			public void run(@NonNull OpinionsContract.View view) {
				view.showProgress(true);
				interactor.removeOpinion(opinion);
			}
		});
	}

	@Subscribe
	@Override
	public void onDataEventListener(final OpinionEvent event) {
		ifViewAttached(new ViewAction<OpinionsContract.View>() {
			@Override
			public void run(@NonNull OpinionsContract.View view) {
				view.showProgress(false);
				switch (event.getTypeEvent()) {
					case OpinionEvent.SUCCES_ADD:
						view.add(event.getOpinion());
						view.onComplete();
						break;
					case OpinionEvent.SUCCES_UPDATE:
						view.update(event.getOpinion());
						view.onComplete();
						break;
					case OpinionEvent.SUCCES_REMOVE:
						view.remove(event.getOpinion());
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
