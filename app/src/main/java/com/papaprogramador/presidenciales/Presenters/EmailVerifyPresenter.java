package com.papaprogramador.presidenciales.Presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.EmailVerify;
import com.papaprogramador.presidenciales.Cases.SignInWithCredentials;
import com.papaprogramador.presidenciales.Utils.Constans;

public class EmailVerifyPresenter extends MvpBasePresenter<EmailVerify.View>
		implements EmailVerify.Presenter {

	private Context context;

	public EmailVerifyPresenter(Context context) {
		this.context = context;
	}

	@Override
	public void startIsEmailIsVerify(final String emailUser, final String pass) {
		ifViewAttached(new ViewAction<EmailVerify.View>() {
			@Override
			public void run(@NonNull final EmailVerify.View view) {

				view.showProgressBar(true);

				new SignInWithCredentials(context, emailUser, pass,
						new SignInWithCredentials.SignIn() {
							@Override
							public void onResult(final String resultado, FirebaseUser user) {
								switch (resultado) {
									case Constans.RESULT_NO_SUCCESSFUL:
										view.showProgressBar(false);
										view.errorSession();
										break;
									case Constans.RESULT_EMAIL_NO_VERIFY:
										view.showProgressBar(false);
										view.emailNoVerify();
										break;
									case Constans.RESULT_IS_SUCCESSFUL:
										view.showProgressBar(false);
										view.goMainActivity();
										break;
								}
							}
						});
			}
		});
	}
}
