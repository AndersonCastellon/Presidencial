package com.papaprogramador.presidenciales.TreeMvp.UpdatePasswordFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.View.Fragments.DialogFragment.DialogOk;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UpdatePasswordView extends MvpFragment<UpdatePasswordFragmentContract.View, UpdatePasswordFragmentContract.Presenter>
		implements UpdatePasswordFragmentContract.View {

	@BindView(R.id.current_password)
	TextInputEditText currentPassword;
	@BindView(R.id.new_password)
	TextInputEditText newPassword;
	@BindView(R.id.repeat_new_password)
	TextInputEditText repeatNewPassword;
	@BindView(R.id.content_reset_pass_view_main)
	LinearLayout contentResetPassViewMain;
	@BindView(R.id.loadingView)
	ProgressBar loadingView;

	Unbinder unbinder;

	public UpdatePasswordView() {
	}


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_update_password, container, false);

		unbinder = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getPresenter().passwordPreferencesIsNull();
	}

	@OnClick(R.id.update_password)
	public void onViewClicked() {

		String currentPass = currentPassword.getText().toString();
		String newPass = newPassword.getText().toString();
		String repeatNewPass = repeatNewPassword.getText().toString();

		getPresenter().validatePassword(currentPass, newPass, repeatNewPass);
	}

	@NonNull
	@Override
	public UpdatePasswordFragmentContract.Presenter createPresenter() {
		return new UpdatePasswordFragmentPresenter(getActivity());
	}

	@Override
	public void showProgress(boolean show) {
		if (show) {
			contentResetPassViewMain.setVisibility(View.GONE);
			loadingView.setVisibility(View.VISIBLE);
		} else {
			contentResetPassViewMain.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
		}
	}

	@Override
	public void currentPasswordIsEmpty() {
		currentPassword.setError(getResources().getString(R.string.current_password_long_text));
	}

	@Override
	public void passwordPreferencesIsNull() {

		Bundle bundle = new Bundle();
		bundle.putInt(Constans.PUT_DIALOG_OK_REQUEST_CODE, Constans.CURRENT_PASSWORD_IS_NULL);
		bundle.putString(Constans.PUT_DIALOG_OK_MESSAGE,
				getResources().getString(R.string.current_password_is_null_dialog_text));

		DialogOk dialogOk = DialogOk.newInstance(bundle);
		dialogOk.show(Objects.requireNonNull(getFragmentManager()), "DialogOk");
	}

	@Override
	public void currentPasswordDoesNotMatch() {
		currentPassword.setError(getResources().getString(R.string.current_password_not_match));
	}

	@Override
	public void newPasswordIsEmpty() {
		newPassword.setError(getResources().getString(R.string.new_password_is_empty_text));
	}

	@Override
	public void repeatNewPasswordIsEmpty() {
		repeatNewPassword.setError(getResources().getString(R.string.repeat_new_password_please_text));
	}

	@Override
	public void newPasswordNoValid() {
		newPassword.setError(getResources().getString(R.string.password_length_is_invalid));
		repeatNewPassword.setError(getResources().getString(R.string.password_length_is_invalid));

		newPassword.setText("");
		repeatNewPassword.setText("");

	}

	@Override
	public void newPasswordDoesNotMatch() {
		repeatNewPassword.setError(getResources().getString(R.string.new_password_not_match));
	}

	@Override
	public void updatePasswordIsSuccessful() {

		Bundle bundle = new Bundle();
		bundle.putInt(Constans.PUT_DIALOG_OK_REQUEST_CODE, Constans.UPDATE_PASSWORD_SUCCESSFUL_CODE);
		bundle.putString(Constans.PUT_DIALOG_OK_MESSAGE,
				getResources().getString(R.string.update_password_successful));

		DialogOk dialogOk = DialogOk.newInstance(bundle);
		dialogOk.show(Objects.requireNonNull(getFragmentManager()), "DialogOk");
	}

	@Override
	public void updatePasswordError() {

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
