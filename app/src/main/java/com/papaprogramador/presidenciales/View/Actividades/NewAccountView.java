package com.papaprogramador.presidenciales.View.Actividades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.NewAccount;
import com.papaprogramador.presidenciales.Presenters.NewAccountPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NewAccountView extends MvpActivity<NewAccount.View,
		NewAccount.Presenter> implements NewAccount.View {

	@BindView(R.id.nameUser)
	TextInputEditText nameUser;
	@BindView(R.id.emailUser)
	TextInputEditText emailUser;
	@BindView(R.id.emailUser2)
	TextInputEditText emailUser2;
	@BindView(R.id.passUser)
	TextInputEditText passUser;
	@BindView(R.id.passUser2)
	TextInputEditText passUser2;
	@BindView(R.id.mSpinnerDepartament)
	MaterialBetterSpinner mSpinnerDepartment;
	@BindView(R.id.layoutContentNewAccount)
	LinearLayout layoutContentNewAccount;
	@BindView(R.id.mProgressBarNewAccount)
	ProgressBar mProgressBarNewAccount;

	Unbinder unbinder;

	private String mIdDevice;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);

		unbinder = ButterKnife.bind(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mIdDevice = bundle.getString(Constans.PUT_ID_DISPOSITIVO);
		}

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_dropdown_item_1line, Constans.departamento);

		mSpinnerDepartment.setAdapter(arrayAdapter);

	}

	@OnClick(R.id.mBtnCreateNewAccount)
	public void onBtnCreateNewAccountClicked() {

		String nUsuario = nameUser.getText().toString();
		String eUsuario = emailUser.getText().toString();
		String eUsuario2 = emailUser2.getText().toString();
		String password = passUser.getText().toString();
		String password2 = passUser2.getText().toString();
		String departamento = mSpinnerDepartment.getText().toString();
		Context context = NewAccountView.this;

		getPresenter().validarCampos(context, mIdDevice, nUsuario, eUsuario, eUsuario2,
				password, password2, departamento);
	}

	@NonNull
	@Override
	public NewAccount.Presenter createPresenter() {
		return new NewAccountPresenter();
	}

	@Override
	public void accountAlreadyExists() {

		Snackbar.make(mSpinnerDepartment, getResources().getString(R.string.emailConCuentaAsociada),
				Snackbar.LENGTH_LONG)
				.setActionTextColor(getResources().getColor(R.color.accent))
				.setAction(getResources().getText(R.string.RESET_PASS), new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NewAccountView.this,
								ResetPasswordView.class);
						startActivity(intent);
						finish();
					}
				})
				.show();

	}

	@Override
	public void showProgressBar(Boolean show) {

		if (show) {
			layoutContentNewAccount.setVisibility(View.GONE);
			mProgressBarNewAccount.setVisibility(View.VISIBLE);
		} else {
			layoutContentNewAccount.setVisibility(View.VISIBLE);
			mProgressBarNewAccount.setVisibility(View.GONE);
		}
	}

	@Override
	public void nameUserIsEmpty() {
		nameUser.setError(getResources().getString(R.string.nombreUsuarioVacio));
	}

	@Override
	public void emailUserIsEmpty() {
		emailUser.setError(getResources().getString(R.string.emailUsuarioVacio));
	}

	@Override
	public void emailUser2IsEmpty() {
		emailUser2.setError(getResources().getString(R.string.confirmaEmailUsuario));
	}

	@Override
	public void passUserIsEmpty() {
		passUser.setError(getResources().getString(R.string.passVacio));
	}

	@Override
	public void passUser2IsEmpty() {
		passUser2.setError(getResources().getString(R.string.pass2Vacio));
	}

	@Override
	public void departmentIsEmpty() {
		mSpinnerDepartment.setError(getResources().getString(R.string.eligeTuDepartamento));
	}

	@Override
	public void emailUserIsDifferent() {
		emailUser.setError(getResources().getString(R.string.emailNoCoincide));
		emailUser2.setError(getResources().getString(R.string.emailNoCoincide));
	}

	@Override
	public void emailUserIsInvalid() {
		emailUser.setError(getResources().getString(R.string.emailInvalido));
		emailUser2.setError(getResources().getString(R.string.emailInvalido));
	}

	@Override
	public void passUserIsInvalid() {
		passUser.setError(getResources().getString(R.string.passInvalido));
		passUser2.setError(getResources().getString(R.string.passInvalido));

		passUser.setText("");
		passUser2.setText("");
	}

	@Override
	public void passUserIsDifferent() {
		passUser.setError(getResources().getString(R.string.passNoCoincide));
		passUser2.setError(getResources().getString(R.string.passNoCoincide));

		passUser.setText("");
		passUser2.setText("");
	}

	@Override
	public void goEmailVerifyView(String emailUser, String passUser) {

		Intent intent = new Intent(NewAccountView.this, EmailVerifyView.class);
		intent.putExtra(Constans.PUT_EMAIL_USUARIO, emailUser);
		intent.putExtra(Constans.PUT_PASSWORD, passUser);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}
}
