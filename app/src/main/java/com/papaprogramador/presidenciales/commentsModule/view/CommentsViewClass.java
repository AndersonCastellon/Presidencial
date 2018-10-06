package com.papaprogramador.presidenciales.commentsModule.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.commentsModule.presenter.CommentsPresenter;
import com.papaprogramador.presidenciales.commentsModule.presenter.CommentsPresenterClass;
import com.papaprogramador.presidenciales.common.pojo.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsViewClass extends MvpActivity<CommentsView, CommentsPresenter>
		implements CommentsView {

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

	private String opinionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments_view);
		ButterKnife.bind(this);
		getPresenter().onCreate();
		opinionId = getIntent().getStringExtra(Constans.EXTRA_OPINION);
	}

	@OnClick(R.id.btn_send_comment)
	public void onViewClicked() {
	}

	@NonNull
	@Override
	public CommentsPresenter createPresenter() {
		return new CommentsPresenterClass();
	}

	@Override
	public void showProgress() {

	}

	@Override
	public void hideProgress() {

	}

	@Override
	public void addAllComments(List<Comment> comments) {

	}

	@Override
	public void addComment(Comment comment) {

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

	}

	@Override
	public void showError(int resMsg) {

	}

	@Override
	protected void onPause() {
		super.onPause();
		removeCommentsNotifier();
	}

	@Override
	protected void onResume() {
		super.onResume();
		addCommentsNotifier();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getPresenter().onDestroy();
	}
}
