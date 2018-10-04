package com.papaprogramador.presidenciales.commentsModule.model.dataAccess;

import com.papaprogramador.presidenciales.common.pojo.Comment;

import java.util.List;

import io.reactivex.Observable;

public interface CommentsDataSource {
	Observable<List<Comment>> getComments(String opinionId);

	Observable<Comment> publishComment(Comment commentPublication);

	Observable<Comment> addCommentNotifier(String opinionId);

	Observable<Boolean> removeCommentNotifier(String opinionId);
}
