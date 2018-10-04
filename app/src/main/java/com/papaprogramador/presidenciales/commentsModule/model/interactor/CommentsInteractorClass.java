package com.papaprogramador.presidenciales.commentsModule.model.interactor;

import com.papaprogramador.presidenciales.common.pojo.Comment;

import java.util.List;

import io.reactivex.Observable;

public class CommentsInteractorClass implements CommentsInteractor {
	@Override
	public Observable<List<Comment>> getComments(String opinionId) {
		return null;
	}

	@Override
	public Observable<Comment> publishComment(Comment commentPublication) {
		return null;
	}

	@Override
	public Observable<Comment> addCommentNotifier(String opinionId) {
		return null;
	}

	@Override
	public Observable<Boolean> removeCommentNotifier(String opinionId) {
		return null;
	}
}
