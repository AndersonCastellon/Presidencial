package com.papaprogramador.presidenciales.commentsModule.model.dataAccess;

import android.support.v4.util.Pair;

import com.papaprogramador.presidenciales.common.pojo.Comment;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface CommentsDataSource {
	Observable<List<Comment>> getComments(String opinionId);

	Single<Boolean> publishComment(Comment commentPublication);

	Single<Pair<Boolean, Integer>> deleteComment(Comment comment);

	Observable<Pair<Integer, Comment>> addCommentNotifier(String opinionId);

	Single<Boolean> removeCommentNotifier(String opinionId);
}
