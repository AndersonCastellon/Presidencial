package com.papaprogramador.presidenciales.commentsModule.presenter;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.papaprogramador.presidenciales.commentsModule.events.CommentEvent;
import com.papaprogramador.presidenciales.commentsModule.view.CommentsView;

public interface CommentsPresenter extends MvpPresenter<CommentsView> {
	void onCreate();
	void onDestroy();

	void getComments(String opinionId);

	void publishComment(String opinionId, String commentText);

	void addCommentNotifier(String opinionId);

	void removeCommentNotifier(String opinionId);

	void commentsEventsListener(CommentEvent event);
}
