package com.papaprogramador.presidenciales.commentsModule.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.View.Activity.MainView;
import com.papaprogramador.presidenciales.commentsModule.presenter.CommentsPresenter;
import com.papaprogramador.presidenciales.commentsModule.presenter.CommentsPresenterClass;
import com.papaprogramador.presidenciales.commentsModule.view.adapter.CommentsAdapter;
import com.papaprogramador.presidenciales.commentsModule.view.adapter.OnMenuCommentItemClickListener;
import com.papaprogramador.presidenciales.common.pojo.Comment;
import com.papaprogramador.presidenciales.opinionsModule.view.OpinionsView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsViewClass extends MvpActivity<CommentsView, CommentsPresenter>
		implements CommentsView, OnMenuCommentItemClickListener {

	@BindView(R.id.rv_comments)
	RecyclerView rvComments;
	@BindView(R.id.no_comments)
	LinearLayout noComments;
	@BindView(R.id.et_comment)
	EditText etComment;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.loadingView)
	ProgressBar loadingView;
	@BindView(R.id.container_loading_view)
	FrameLayout containerLoadingView;
	@BindView(R.id.root)
	CoordinatorLayout root;

	private LinearLayoutManager linearLayoutManager;
	private CommentsAdapter commentsAdapter;
	private String opinionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments_view);
		ButterKnife.bind(this);
		setupToolbar();
		getPresenter().onCreate();
		opinionId = getIntent().getStringExtra(Constans.EXTRA_OPINION);
		setupComments();
		getPresenter().getComments(opinionId);
	}

	private void setupToolbar() {
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.comments_title);
		}
	}

	@OnClick(R.id.btn_send_comment)
	public void onViewClicked() {
		if (!etComment.getText().toString().isEmpty()) {
			showProgress();
			getPresenter().publishComment(opinionId, etComment.getText().toString());
		}

		etComment.setText("");
	}

	@Override
	public void onDeleteClick(Comment comment) {
		getPresenter().deleteComment(comment);
	}

	@NonNull
	@Override
	public CommentsPresenter createPresenter() {
		return new CommentsPresenterClass();
	}

	@Override
	public void showProgress() {
		noComments.setVisibility(View.GONE);
		containerLoadingView.setVisibility(View.VISIBLE);
		loadingView.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		noComments.setVisibility(View.GONE);
		containerLoadingView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
	}

	@Override
	public void setupComments() {
		linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setStackFromEnd(true);
		rvComments.setLayoutManager(linearLayoutManager);

		commentsAdapter = new CommentsAdapter(this);
		rvComments.setAdapter(commentsAdapter);
		rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
	}

	@Override
	public void addAllComments(List<Comment> comments) {
		rvComments.setVisibility(View.VISIBLE);
		commentsAdapter.addAllComments(comments);
	}

	@Override
	public void addComment(Comment comment) {
		rvComments.setVisibility(View.VISIBLE);
		if (commentsAdapter.addComment(comment)) {
			linearLayoutManager.smoothScrollToPosition(rvComments,
					null, commentsAdapter.getItemCount());
		}
	}

	@Override
	public void deleteComment(Comment comment) {
		commentsAdapter.deleteComment(comment);
	}

	@Override
	public void addCommentsNotifier() {
		getPresenter().addCommentNotifier(opinionId);
	}

	@Override
	public void removeCommentsNotifier() {
		getPresenter().removeCommentNotifier(opinionId);
	}

	@Override
	public void showNoData() {
		hideProgress();
		rvComments.setVisibility(View.GONE);
		noComments.setVisibility(View.VISIBLE);
	}

	@Override
	public void showError(int resMsg) {
		Snackbar.make(root, resMsg, Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressedIntent() {
		Intent intent = new Intent(CommentsViewClass.this, MainView.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressedIntent();
		return super.onSupportNavigateUp();
	}

	@Override
	protected void onPause() {
		removeCommentsNotifier();
		super.onPause();
	}

	@Override
	protected void onResume() {
		addCommentsNotifier();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		removeCommentsNotifier();
		getPresenter().onDestroy();
		super.onDestroy();
	}
}
