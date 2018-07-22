package com.papaprogramador.presidenciales.TreeMvp.DeleteAccount;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

public class DeleteAccountView extends MvpFragment<DeleteAccountContract.View, DeleteAccountContract.Presenter>
		implements DeleteAccountContract.View {

	@BindView(R.id.delete_email_user)
	TextInputEditText deleteEmailUser;
	@BindView(R.id.delete_pass_user)
	TextInputEditText deletePassUser;
	@BindView(R.id.loadingView)
	ProgressBar loadingView;
	@BindView(R.id.delete_account_content)
	LinearLayout deleteAccountContent;

	private String emailCurrentUser = null;

	Unbinder unbinder;

	public DeleteAccountView() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_delete_account, container, false);

		unbinder = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getPresenter().getEmailCurrentUser();

		if (emailCurrentUser != null) {
			deleteEmailUser.setText(emailCurrentUser);
		}
	}

	@NonNull
	@Override
	public DeleteAccountContract.Presenter createPresenter() {
		return new DeleteAccountPresenter(getActivity());
	}

	@OnClick(R.id.delete_account)
	public void onViewClicked() {
		String emailUser = deleteEmailUser.getText().toString();
		String passwordUser = deletePassUser.getText().toString();

		getPresenter().validateCredentials(emailUser, passwordUser);
	}

	@Override
	public void setEmailCurrentUser(String emailCurrentUser) {
		this.emailCurrentUser = emailCurrentUser;
	}

	@Override
	public void credentialsUserError() {
		Snackbar.make(deleteEmailUser, getResources().getString(R.string.credencialesEmailIncorrectas),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void deleteUserError() {
		Snackbar.make(deleteEmailUser, getResources().getString(R.string.delete_user_error),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void deleteUserIsSuccessful() {

		Bundle bundle = new Bundle();
		bundle.putInt(Constans.PUT_DIALOG_OK_REQUEST_CODE, Constans.DIALOG_OK_SUCCESSFUL_CODE);
		bundle.putString(Constans.PUT_DIALOG_OK_MESSAGE,
				getResources().getString(R.string.delete_user_successful));

		DialogOk dialogOk = DialogOk.newInstance(bundle);
		dialogOk.show(Objects.requireNonNull(getFragmentManager()), "DialogOk");
	}

	@Override
	public void showProgress(boolean show) {
		if (show){
			deleteAccountContent.setVisibility(View.GONE);
			loadingView.setVisibility(View.VISIBLE);
		} else {
			deleteAccountContent.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
