package com.papaprogramador.presidenciales.TreeMvp.UpdatePassword;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface UpdatePasswordFragmentContract {

	interface View extends MvpView {
		void showProgress(boolean show);
		void currentPasswordIsEmpty();
		void passwordPreferencesIsNull();
		void currentPasswordDoesNotMatch();
		void newPasswordIsEmpty();
		void repeatNewPasswordIsEmpty();
		void newPasswordNoValid();
		void newPasswordDoesNotMatch();
		void updatePasswordIsSuccessful();
		void updatePasswordError();
	}

	interface Presenter extends MvpPresenter<UpdatePasswordFragmentContract.View> {
		void passwordPreferencesIsNull();
		void validatePassword(String currentPassword, String newPassword, String repeatNewPassword);
		void updatePassword(String emailUser, String currentPassword, String newPassword);
	}
}
