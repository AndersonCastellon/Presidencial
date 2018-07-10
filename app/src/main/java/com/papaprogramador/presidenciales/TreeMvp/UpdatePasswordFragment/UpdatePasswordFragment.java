package com.papaprogramador.presidenciales.TreeMvp.UpdatePasswordFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.papaprogramador.presidenciales.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UpdatePasswordFragment extends MvpFragment<UpdatePasswordFragmentContract.View, UpdatePasswordFragmentContract.Presenter>
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

	public UpdatePasswordFragment() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_update_password, container, false);

		unbinder = ButterKnife.bind(this, view);
		return view;
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
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
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
}
