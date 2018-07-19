package com.papaprogramador.presidenciales.TreeMvp.DeleteAccount;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.papaprogramador.presidenciales.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DeleteAccountFragment extends MvpFragment<DeleteAccountContract.View, DeleteAccountContract.Presenter>
		implements DeleteAccountContract.View {

	@BindView(R.id.delete_email_user)
	TextInputEditText deleteEmailUser;
	@BindView(R.id.delete_pass_user)
	TextInputEditText deletePassUser;

	private String emailCurrentUser = null;

	Unbinder unbinder;

	public DeleteAccountFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_delete_account, container, false);

		unbinder = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getPresenter().getEmailCurrentUser();

		if (emailCurrentUser != null){
			deleteEmailUser.setText(emailCurrentUser);
		}
	}

	@NonNull
	@Override
	public DeleteAccountContract.Presenter createPresenter() {
		return new DeleteAccountPresenter();
	}

	@OnClick(R.id.delete_account)
	public void onViewClicked() {
	}

	@Override
	public void setEmailCurrentUser(String emailCurrentUser) {
		this.emailCurrentUser = emailCurrentUser;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
