package com.papaprogramador.presidenciales.TreeMvp.UpdatePasswordFragment;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Utils.StaticMethods.SharedPreferencesMethods;

public class UpdatePasswordFragmentPresenter extends MvpBasePresenter<UpdatePasswordFragmentContract.View>
		implements UpdatePasswordFragmentContract.Presenter {

	private boolean passwordIsValid;
	private String passCurrent;
	private String currentEmail;

	UpdatePasswordFragmentPresenter(Context context) {
		currentEmail = SharedPreferencesMethods.getEmail(context);
		passCurrent = SharedPreferencesMethods.getPassword(context);
		passwordIsValid = true;
	}

	@Override
	public void validatePassword(String currentPassword, String newPassword, String repeatNewPassword) {

		if (passCurrent == null) {
			ifViewAttached(new ViewAction<UpdatePasswordFragmentContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordFragmentContract.View view) {
					passwordIsValid = false;
					view.passwordPreferencesIsNull();
				}
			});
		}

		if (currentPassword.isEmpty()) {
			ifViewAttached(new ViewAction<UpdatePasswordFragmentContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordFragmentContract.View view) {
					passwordIsValid = false;
					view.currentPasswordIsEmpty();
				}
			});
		} else if (!currentPassword.equals(passCurrent)) {
			ifViewAttached(new ViewAction<UpdatePasswordFragmentContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordFragmentContract.View view) {
					passwordIsValid = false;
					view.currentPasswordDoesNotMatch();
				}
			});
		}

		if (newPassword.isEmpty()) {
			ifViewAttached(new ViewAction<UpdatePasswordFragmentContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordFragmentContract.View view) {
					passwordIsValid = false;
					view.newPasswordIsEmpty();
				}
			});
		}

		if (repeatNewPassword.isEmpty()) {
			ifViewAttached(new ViewAction<UpdatePasswordFragmentContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordFragmentContract.View view) {
					passwordIsValid = false;
					view.repeatNewPasswordIsEmpty();
				}
			});
		}

		if (!newPassword.equals(repeatNewPassword)) {
			ifViewAttached(new ViewAction<UpdatePasswordFragmentContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordFragmentContract.View view) {
					passwordIsValid = false;
					view.newPasswordDoesNotMatch();
				}
			});
		}

		if (passwordIsValid) {
			updatePassword(currentEmail, passCurrent, newPassword);
		}


	}

	@Override
	public void updatePassword(String emailUser, String currentPassword, String newPassword) {

	}

	@Override
	public void goResetPasswordView() {

		ifViewAttached(new ViewAction<UpdatePasswordFragmentContract.View>() {
			@Override
			public void run(@NonNull UpdatePasswordFragmentContract.View view) {
				view.goResetPasswordView(currentEmail);
			}
		});
	}
}
