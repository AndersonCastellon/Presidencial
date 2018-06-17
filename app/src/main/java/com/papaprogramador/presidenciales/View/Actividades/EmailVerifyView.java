package com.papaprogramador.presidenciales.View.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.EmailVerify;
import com.papaprogramador.presidenciales.Presenters.EmailVerifyPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EmailVerifyView extends MvpActivity<EmailVerify.View, EmailVerify.Presenter>
		implements EmailVerify.View {

	@BindView(R.id.layoutContentNewAccount)
	ConstraintLayout contentEmailVerifyView;
	@BindView(R.id.mProgressBarEmailVerifyView)
	ProgressBar mProgressBarEmailVerifyView;

	private String emailUser;
	private String passUser;

	Unbinder unbinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_verification);

		unbinder = ButterKnife.bind(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			emailUser = bundle.getString(Constans.PUT_EMAIL_USUARIO);
			passUser = bundle.getString(Constans.PUT_PASSWORD);
		}
	}

	@OnClick(R.id.mBtnButtonVerify)
	public void onmBtnButtonVerifyClicked() {
		getPresenter().startIsEmailIsVerify(emailUser, passUser);
	}

	@NonNull
	@Override
	public EmailVerify.Presenter createPresenter() {
		return new EmailVerifyPresenter(this);
	}

	@Override
	public void emailNoVerify() {
		Snackbar.make(contentEmailVerifyView, getResources().getString(R.string.emailNoVerificado),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void errorSession() {
		Snackbar.make(contentEmailVerifyView, getResources().getString(R.string.errorThis),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void goMainActivity() {
		Intent intent = new Intent(EmailVerifyView.this, MainView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void showProgressBar(boolean show) {
		if (show) {
			contentEmailVerifyView.setVisibility(View.GONE);
			mProgressBarEmailVerifyView.setVisibility(View.VISIBLE);
		} else {
			contentEmailVerifyView.setVisibility(View.VISIBLE);
			mProgressBarEmailVerifyView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}

}
