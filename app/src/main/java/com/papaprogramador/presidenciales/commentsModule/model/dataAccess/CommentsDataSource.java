package com.papaprogramador.presidenciales.commentsModule.model.dataAccess;

import com.papaprogramador.presidenciales.common.pojo.Comment;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface CommentsDataSource {
	Observable<List<Comment>> getComments(String opinionId);

	Single<Boolean> publishComment(Comment commentPublication);

	Observable<Comment> addCommentNotifier(String opinionId);

	Single<Boolean> removeCommentNotifier(String opinionId);
}
