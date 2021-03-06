package com.papaprogramador.presidenciales.commentsModule.model.interactor;

import android.support.v4.util.Pair;

import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.commentsModule.events.CommentEvent;
import com.papaprogramador.presidenciales.commentsModule.model.dataAccess.FirebaseCommentsDataSource;
import com.papaprogramador.presidenciales.common.pojo.Comment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentsInteractorClass implements CommentsInteractor {

	private FirebaseCommentsDataSource commentsDataSource;
	private Observable<List<Comment>> getCommentsObservable;
	private Single<Pair<Boolean, Integer>> deleteComment;
	private Observable<Pair<Integer, Comment>> addCommentsNotifier;
	private Single<Boolean> publishComment;
	private Single<Boolean> removeCommentsNotifier;

	public CommentsInteractorClass() {
		commentsDataSource = new FirebaseCommentsDataSource();
	}


	@Override
	public void getComments(String opinionId) {
		getCommentsObservable = commentsDataSource.getComments(opinionId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		getCommentsObservable.subscribe(new Observer<List<Comment>>() {
			@Override
			public void onSubscribe(Disposable d) {
			}

			@Override
			public void onNext(List<Comment> comments) {
				postCommentEvent(CommentEvent.INITIAL_DATA, comments);
			}

			@Override
			public void onError(Throwable e) {
				postCommentEvent(CommentEvent.ERROR, R.string.error_server);
			}

			@Override
			public void onComplete() {
				postCommentEvent(CommentEvent.NO_DATA);
			}
		});
	}

	@Override
	public void publishComment(Comment commentPublication) {
		publishComment = commentsDataSource.publishComment(commentPublication)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		publishComment.subscribe(new SingleObserver<Boolean>() {
			@Override
			public void onSubscribe(Disposable d) {
			}

			@Override
			public void onSuccess(Boolean aBoolean) {
				if (!aBoolean) {
					postCommentEvent(CommentEvent.ERROR, R.string.error_while_publish_comment);
				}
			}

			@Override
			public void onError(Throwable e) {
				postCommentEvent(CommentEvent.ERROR, R.string.error_server);
			}
		});
	}

	@Override
	public void deleteComment(Comment comment) {
		deleteComment = commentsDataSource.deleteComment(comment)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		deleteComment.subscribe(new SingleObserver<Pair<Boolean, Integer>>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onSuccess(Pair<Boolean, Integer> pair) {
				if (!pair.first) {
					postCommentEvent(CommentEvent.ERROR, pair.second);
				}
			}

			@Override
			public void onError(Throwable e) {

			}
		});
	}

	@Override
	public void addCommentNotifier(String opinionId) {
		addCommentsNotifier = commentsDataSource.addCommentNotifier(opinionId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		addCommentsNotifier.subscribe(new Observer<Pair<Integer, Comment>>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(Pair<Integer, Comment> pair) {
				if (pair.first != null) {
					switch (pair.first) {
						case CommentEvent.SUCCES_ADD:
							postCommentEvent(CommentEvent.SUCCES_ADD, pair.second);
							break;
						case CommentEvent.SUCCES_REMOVED:
							postCommentEvent(CommentEvent.SUCCES_REMOVED, pair.second);
							break;
					}
				}

			}

			@Override
			public void onError(Throwable e) {
				postCommentEvent(CommentEvent.ERROR, R.string.error_while_get_new_comments);
			}

			@Override
			public void onComplete() {

			}
		});
	}

	@Override
	public void removeCommentNotifier(String opinionId) {
		removeCommentsNotifier = commentsDataSource.removeCommentNotifier(opinionId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		removeCommentsNotifier.subscribe(new SingleObserver<Boolean>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onSuccess(Boolean aBoolean) {
				if (!aBoolean) {
					postCommentEvent(CommentEvent.ERROR, R.string.error_while_remove_listener_comments);
				}
			}

			@Override
			public void onError(Throwable e) {
				postCommentEvent(CommentEvent.ERROR, R.string.error_while_remove_listener_comments);
			}
		});
	}

	@Override
	public void onDestroy() {
		//descartar todas las subscripciones con rxjava
	}

	private void postCommentEvent(int eventType) {
		postCommentEvent(eventType, null, null, 0);
	}

	private void postCommentEvent(int eventType, Comment comment) {
		postCommentEvent(eventType, null, comment, 0);
	}

	private void postCommentEvent(int eventType, List<Comment> comments) {
		postCommentEvent(eventType, comments, null, 0);
	}

	private void postCommentEvent(int eventType, int resMsg) {
		postCommentEvent(eventType, null, null, resMsg);
	}

	private void postCommentEvent(int eventType, List<Comment> comments, Comment comment, int resMsg) {
		CommentEvent event = CommentEvent.Builder()
				.eventType(eventType)
				.comments(comments)
				.comment(comment)
				.resMsg(resMsg)
				.build();
		EventBus.getDefault().post(event);
	}
}
