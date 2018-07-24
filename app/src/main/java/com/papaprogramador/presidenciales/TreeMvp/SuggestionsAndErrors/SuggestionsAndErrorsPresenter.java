package com.papaprogramador.presidenciales.TreeMvp.SuggestionsAndErrors;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Utils.Constans;

public class SuggestionsAndErrorsPresenter extends MvpBasePresenter<SuggestionsAndErrorsContract.View>
		implements SuggestionsAndErrorsContract.Presenter {

	SuggestionsAndErrorsPresenter() {
	}

	@Override
	public void setMailSuggestions(final String subject) {
		ifViewAttached(new ViewAction<SuggestionsAndErrorsContract.View>() {
			@Override
			public void run(@NonNull SuggestionsAndErrorsContract.View view) {
				if (subject.equals("")){
					view.selectSuggestionSubject();
				} else {
					view.sendSuggestions(Constans.TO, subject);
				}
			}
		});
	}
}
