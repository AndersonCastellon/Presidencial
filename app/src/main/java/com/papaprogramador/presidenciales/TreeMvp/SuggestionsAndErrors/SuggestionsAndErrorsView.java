package com.papaprogramador.presidenciales.TreeMvp.SuggestionsAndErrors;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SuggestionsAndErrorsView extends MvpFragment<SuggestionsAndErrorsContract.View,
		SuggestionsAndErrorsContract.Presenter> implements SuggestionsAndErrorsContract.View {


	@BindView(R.id.mSpinner)
	NiceSpinner mSpinner;
	Unbinder unbinder;

	public SuggestionsAndErrorsView() {
	}


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_suggestions_and_errors, container, false);
		unbinder = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<String> suggestions = new LinkedList<>(Arrays.asList(getResources()
				.getStringArray(R.array.suggestions_array)));
		mSpinner.attachDataSource(suggestions);
	}

	@OnClick(R.id.send_suggestions_and_errors)
	public void onViewClicked() {
		String subject = mSpinner.getText().toString();
		getPresenter().setMailSuggestions(subject);
	}

	@NonNull
	@Override
	public SuggestionsAndErrorsContract.Presenter createPresenter() {
		return new SuggestionsAndErrorsPresenter();
	}

	@Override
	public void selectSuggestionSubject() {
		Snackbar.make(mSpinner, getResources().getString(R.string.select_options_spinner), Snackbar.LENGTH_LONG)
				.show();
	}

	@Override
	public void sendSuggestions(String[] TO, String subject) {

		Intent emailIntent = new Intent(Intent.ACTION_SEND);

		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.setClassName(Constans.PACKAGE_NAME_GMAIL,
				Constans.CLASS_NAME_GMAIL_COMPOSE_MAIL);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

		String shooserTitle = getResources().getString(R.string.send_suggestions_and_errors);

		try {
			startActivity(Intent.createChooser(emailIntent, shooserTitle));
		} catch (android.content.ActivityNotFoundException e) {
			Snackbar.make(mSpinner, getResources().getString(R.string.no_email_client), Snackbar.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
