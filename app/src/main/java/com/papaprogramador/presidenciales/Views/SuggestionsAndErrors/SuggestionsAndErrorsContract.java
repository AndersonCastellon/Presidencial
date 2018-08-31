package com.papaprogramador.presidenciales.Views.SuggestionsAndErrors;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface SuggestionsAndErrorsContract {

	interface View extends MvpView {
		void selectSuggestionSubject();
		void sendSuggestions(String[] TO, String subject);
	}

	interface Presenter extends MvpPresenter<SuggestionsAndErrorsContract.View> {
		void setMailSuggestions(String subject);
	}
}
