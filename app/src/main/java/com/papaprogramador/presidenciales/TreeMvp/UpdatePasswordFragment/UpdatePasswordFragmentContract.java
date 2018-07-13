package com.papaprogramador.presidenciales.TreeMvp.UpdatePasswordFragment;

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
		void newPasswordDoesNotMatch();
		void goResetPasswordView(String emailUser);
		void updatePasswordIsSuccessful();
	}

	interface Presenter extends MvpPresenter<UpdatePasswordFragmentContract.View> {
		void validatePassword(String currentPassword, String newPassword, String repeatNewPassword);
		void updatePassword(String emailUser, String currentPassword, String newPassword);
		void goResetPasswordView();
	}
}
