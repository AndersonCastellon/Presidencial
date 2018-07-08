package com.papaprogramador.presidenciales.Utils.ViewStateActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState;
import com.papaprogramador.presidenciales.InterfacesMVP.MainViewContrat;

public class CustomViewStateActivity implements RestorableViewState<MainViewContrat.View> {

	private final String KEY_STATE = "CustomViewStateActivity-flag";

	boolean hideCandidateView = false;

	@Override
	public void saveInstanceState(@NonNull Bundle out) {
		out.putBoolean(KEY_STATE, hideCandidateView);
	}

	@Override
	public RestorableViewState<MainViewContrat.View> restoreInstanceState(Bundle in) {
		if (in == null){
			return null;
		}
		in.getBoolean(KEY_STATE);
		return this;
	}

	@Override
	public void apply(MainViewContrat.View view, boolean retained) {
		if (hideCandidateView){
			view.hideMainView(true);
		} else {
			view.hideMainView(false);
		}
	}

	public void setHideCandidateView(boolean hideCandidateView) {
		this.hideCandidateView = hideCandidateView;
	}
}
