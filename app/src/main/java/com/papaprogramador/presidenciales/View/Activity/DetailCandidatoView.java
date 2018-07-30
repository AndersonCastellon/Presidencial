package com.papaprogramador.presidenciales.View.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.InterfacesMVP.DetailCandidateContract;
import com.papaprogramador.presidenciales.Presenters.DetailCandidatePresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.View.Fragments.DialogFragment.SelectDepartmentDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetailCandidatoView extends MvpActivity<DetailCandidateContract.View, DetailCandidateContract.Presenter>
		implements DetailCandidateContract.View, SelectDepartmentDialogFragment.DialogFragmentSelectedDepartmentListener {

	@BindView(R.id.CandidateImg)
	ImageView imgCandidate;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.webViewDetailCandidate)
	WebView webViewDetailCandidate;
	@BindView(R.id.fab_vote)
	FloatingActionButton fabVote;

	Unbinder unbinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_candidato);

		unbinder = ButterKnife.bind(this);
	}

	@OnClick({R.id.fab_vote, R.id.fab_share})
	public void onFabButtonsClicked(View view) {
		switch (view.getId()) {
			case R.id.fab_vote:
				getPresenter().goCurrentVote();
				break;
			case R.id.fab_share:
				getPresenter().fabShare(this);
				break;
		}
	}

	@NonNull
	@Override
	public DetailCandidateContract.Presenter createPresenter() {

		Bundle bundle = getIntent().getExtras();

		assert bundle != null;

		String idCandidate = bundle.getString(Constans.PUT_ID_CANDIDATE);
		String nameCandidate = bundle.getString(Constans.PUT_NOMBRE_CANDIDATE);
		String urlImgCandidate = bundle.getString(Constans.PUT_URL_IMAGEN_CANDIDATE);
		String urlHtmlCandidate = bundle.getString(Constans.PUT_URL_HTML_CANDIDATE);
		String urlPoliticalFlag = bundle.getString(Constans.PUT_POLITICAL_FLAG);

		return new DetailCandidatePresenter(idCandidate, nameCandidate, urlImgCandidate,
				urlHtmlCandidate, urlPoliticalFlag);
	}

	@Override
	public void onWebViewSettings(String urlHtmlCandidate) {

		webViewDetailCandidate.getSettings().setDomStorageEnabled(true);
		webViewDetailCandidate.getSettings().setLoadWithOverviewMode(true);
		webViewDetailCandidate.loadUrl(urlHtmlCandidate);
	}

	@Override
	public void getImgCandidate(String urlImgCandidate) {

		//TODO: Implementar placeholder para Glide
		Glide.with(this)
				.load(urlImgCandidate)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.centerCrop()
				.crossFade()
				.into(imgCandidate);
	}

	@Override
	public void getToolbar(String nameCandidate) {

		setSupportActionBar(toolbar);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(nameCandidate);
	}

	@Override
	public void shareCandidate(String shareCandidateString) {

		Intent shareCandidateIntent = new Intent();
		shareCandidateIntent.setAction(Intent.ACTION_SEND);

		shareCandidateIntent.putExtra(Intent.EXTRA_TEXT, shareCandidateString);
		shareCandidateIntent.setType("text/plain");

		String shooserTitle = getResources().getString(R.string.share);
		Intent shooser = Intent.createChooser(shareCandidateIntent, shooserTitle);


		if (shareCandidateIntent.resolveActivity(getPackageManager()) != null) {
			startActivity(shooser);
		}
	}

	@Override
	public void existingVote() {
		Snackbar.make(fabVote, getResources().getString(R.string.existingVote),
				Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void onResult(boolean result) {
		if (result) {
			getPresenter().goCurrentVote();
		}
	}

	@Override
	public void goSelectDepartmentDialogFragment(String uidUser) {

		Bundle bundle = new Bundle();
		bundle.putString(Constans.PUT_UID_USER, uidUser);

		SelectDepartmentDialogFragment dialogFragment = SelectDepartmentDialogFragment.newInstance(bundle);
		dialogFragment.show(getSupportFragmentManager(), "SelectedDepartmentDialog");
	}

	@Override
	public void applyNewVoteIsSuccesful() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(getResources().getString(R.string.applyNewVoteIsSuccesful))
				.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	@Override
	public void showProgressFab(boolean show) {
		if (show) {
			fabVote.setShowProgressBackground(true);
			fabVote.setIndeterminate(true);
		} else {
			fabVote.hideProgress();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		getPresenter().setAuthListener();
	}

	@Override
	protected void onStop() {
		super.onStop();
		getPresenter().removeAuthListener();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}
}
