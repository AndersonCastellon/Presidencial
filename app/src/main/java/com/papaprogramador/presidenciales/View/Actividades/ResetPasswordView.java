package com.papaprogramador.presidenciales.View.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.ResetPassword;
import com.papaprogramador.presidenciales.Presenters.ResetPasswordPresenter;
import com.papaprogramador.presidenciales.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ResetPasswordView extends MvpActivity<ResetPassword.View, ResetPassword.Presenter>
		implements ResetPassword.View {


	@BindView(R.id.resetEmailUser)
	TextInputEditText resetEmailUser;

	Unbinder unbinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

		unbinder = ButterKnife.bind(this);

	}

	@OnClick(R.id.mBtnResetPassUser)
	public void onBtnResetPassUserClicked() {
		String emailUsuario = resetEmailUser.getText().toString();
		getPresenter().emailUserProcess(emailUsuario);
	}

	@NonNull
	@Override
	public ResetPassword.Presenter createPresenter() {
		return new ResetPasswordPresenter();
	}

	@Override
	public void resetIsSuccesful() {
		Toast.makeText(ResetPasswordView.this,
				R.string.EmailResetSend,
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void goLoginView() {
		Intent login = new Intent(ResetPasswordView.this, LoginView.class);
		startActivity(login);
	}

	@Override
	public void emailNoExist() {
		resetEmailUser.setError(getString(R.string.EmailSinCuenta));
	}

	@Override
	public void emailIsEmpty() {
		resetEmailUser.setError(getString(R.string.emailUsuarioVacio));
	}

	@Override
	public void emailIsInvalid() {
		resetEmailUser.setError(getString(R.string.emailInvalido));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}
}
