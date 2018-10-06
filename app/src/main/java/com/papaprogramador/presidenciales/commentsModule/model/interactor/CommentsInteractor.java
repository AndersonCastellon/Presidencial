package com.papaprogramador.presidenciales.commentsModule.model.interactor;

import com.papaprogramador.presidenciales.common.pojo.Comment;

public interface CommentsInteractor {

	void getComments(String opinionId);

	void publishComment(Comment commentPublication);

	void addCommentNotifier(String opinionId);

	void removeCommentNotifier(String opinionId);

	void onDestroy();
}
