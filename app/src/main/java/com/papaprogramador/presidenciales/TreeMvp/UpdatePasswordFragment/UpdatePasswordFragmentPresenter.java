package com.papaprogramador.presidenciales.TreeMvp.UpdatePasswordFragment;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Utils.StaticMethods.SharedPreferencesMethods;

public class UpdatePasswordFragmentPresenter extends MvpBasePresenter<UpdatePasswordFragmentContract.View>
		implements UpdatePasswordFragmentContract.Presenter {

	private Context context;

	public UpdatePasswordFragmentPresenter(Context context) {
		this.context = context;
	}

	@Override
	public void validatePassword(String currentPassword, String newPassword, String repeatNewPassword) {

		String passCurrent = SharedPreferencesMethods.getPassword(context);


	}
}
