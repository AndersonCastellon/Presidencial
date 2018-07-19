package com.papaprogramador.presidenciales.TreeMvp.DeleteAccount;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface DeleteAccountContract {

	interface View extends MvpView {
		void setEmailCurrentUser(String emailCurrentUser);
	}

	interface Presenter extends MvpPresenter<DeleteAccountContract.View> {
		void getEmailCurrentUser();
	}
}
