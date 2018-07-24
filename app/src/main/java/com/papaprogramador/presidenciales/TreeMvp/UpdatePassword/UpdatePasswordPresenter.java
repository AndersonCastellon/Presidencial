package com.papaprogramador.presidenciales.TreeMvp.UpdatePassword;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Utils.StaticMethods.ReauthenticateUserFirebase;
import com.papaprogramador.presidenciales.Utils.StaticMethods.SharedPreferencesMethods;
import com.papaprogramador.presidenciales.Utils.StaticMethods.UpdatePassword;

public class UpdatePasswordPresenter extends MvpBasePresenter<UpdatePasswordContract.View>
		implements UpdatePasswordContract.Presenter {

	private boolean passwordIsValid;
	private String passCurrent;
	private String currentEmail;
	private Context context;

	UpdatePasswordPresenter(Context context) {
		this.context = context;
		currentEmail = SharedPreferencesMethods.getEmail(context);
		passCurrent = SharedPreferencesMethods.getPassword(context);
		passwordIsValid = true;
	}

	@Override
	public void passwordPreferencesIsNull() {
		if (passCurrent == null) {
			ifViewAttached(new ViewAction<UpdatePasswordContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordContract.View view) {
					view.passwordPreferencesIsNull();
				}
			});
		}
	}

	@Override
	public void validatePassword(String currentPassword, String newPassword, String repeatNewPassword) {

		if (currentPassword.isEmpty()) {
			ifViewAttached(new ViewAction<UpdatePasswordContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordContract.View view) {
					passwordIsValid = false;
					view.currentPasswordIsEmpty();
				}
			});
		} else if (!currentPassword.equals(passCurrent)) {
			ifViewAttached(new ViewAction<UpdatePasswordContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordContract.View view) {
					passwordIsValid = false;
					view.currentPasswordDoesNotMatch();
				}
			});
		}

		if (newPassword.isEmpty()) {
			ifViewAttached(new ViewAction<UpdatePasswordContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordContract.View view) {
					passwordIsValid = false;
					view.newPasswordIsEmpty();
				}
			});
		} else if (!UpdatePassword.validatePasswordLength(newPassword)) {
			ifViewAttached(new ViewAction<UpdatePasswordContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordContract.View view) {
					view.newPasswordNoValid();
				}
			});
		}

		if (repeatNewPassword.isEmpty()) {
			ifViewAttached(new ViewAction<UpdatePasswordContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordContract.View view) {
					passwordIsValid = false;
					view.repeatNewPasswordIsEmpty();
				}
			});
		} else if (!UpdatePassword.validatePasswordLength(repeatNewPassword)) {
			ifViewAttached(new ViewAction<UpdatePasswordContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordContract.View view) {
					view.newPasswordNoValid();
				}
			});
		}

		if (!newPassword.equals(repeatNewPassword)) {
			ifViewAttached(new ViewAction<UpdatePasswordContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordContract.View view) {
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
	public void updatePassword(String emailUser, String currentPassword, final String newPassword) {

		if (ReauthenticateUserFirebase.reauthenticateUser(emailUser, currentPassword)) {
			ifViewAttached(new ViewAction<UpdatePasswordContract.View>() {
				@Override
				public void run(@NonNull UpdatePasswordContract.View view) {
					view.showProgress(true);
					if (UpdatePassword.updatePasswordUser(newPassword)){
						SharedPreferencesMethods.setPassword(context, newPassword);
						view.showProgress(false);
						view.updatePasswordIsSuccessful();
					} else {
						ifViewAttached(new ViewAction<UpdatePasswordContract.View>() {
							@Override
							public void run(@NonNull UpdatePasswordContract.View view) {
								view.updatePasswordError();
							}
						});
					}
				}
			});


		}
	}
}
