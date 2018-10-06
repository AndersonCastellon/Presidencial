package com.papaprogramador.presidenciales.commentsModule.presenter;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.papaprogramador.presidenciales.commentsModule.events.CommentEvent;
import com.papaprogramador.presidenciales.commentsModule.view.CommentsView;
import com.papaprogramador.presidenciales.common.pojo.Comment;

public interface CommentsPresenter extends MvpPresenter<CommentsView> {
	void onCreate();
	void onDestroy();

	void getComments(String opinionId);

	void publishComment(Comment commentPublication);

	void addCommentNotifier(String opinionId);

	void removeCommentNotifier(String opinionId);

	void commentsEventsListener(CommentEvent event);
}
