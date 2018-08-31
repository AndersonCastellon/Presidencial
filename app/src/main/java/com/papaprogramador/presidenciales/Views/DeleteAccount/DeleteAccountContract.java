package com.papaprogramador.presidenciales.Views.DeleteAccount;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface DeleteAccountContract {

	interface View extends MvpView {
		void setEmailCurrentUser(String emailCurrentUser);
		void credentialsUserError();
		void deleteUserError();
		void deleteUserIsSuccessful();
		void showProgress(boolean show);
	}

	interface Presenter extends MvpPresenter<DeleteAccountContract.View> {
		void getEmailCurrentUser();
		void validateCredentials(String emailUser, String passwordUser);
		void goDeleteAccount();
		void getVoteByCandidate();
		void deleteVoteIntoCandidate(String idCandidate);
		void deleteUserInfo();
	}
}
