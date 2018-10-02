package com.papaprogramador.presidenciales.commentsModule.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.papaprogramador.presidenciales.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsViewClass extends AppCompatActivity {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments_view);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.btn_send_comment)
	public void onViewClicked() {
	}
}
