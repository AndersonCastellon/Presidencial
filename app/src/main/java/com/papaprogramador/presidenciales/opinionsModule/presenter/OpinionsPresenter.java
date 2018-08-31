package com.papaprogramador.presidenciales.opinionsModule.presenter;

import android.content.Context;
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
	public void onResume() {
	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void remove(Opinion opinion) {

	}

	@Subscribe
	@Override
	public void onEventListener(OpinionEvent event) {

	}
}
