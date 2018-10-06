package com.papaprogramador.presidenciales.commentsModule.view;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.papaprogramador.presidenciales.common.pojo.Comment;

import java.util.List;

public interface CommentsView extends MvpView {
	void showProgress();
	void hideProgress();

	void addAllComments(List<Comment> comments);
	void addComment(Comment comment);
	void addCommentsNotifier();
	void removeCommentsNotifier();
	void showNoData();
	void showError(int resMsg);
}
