package com.papaprogramador.presidenciales.commentsModule.presenter;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.commentsModule.events.CommentEvent;
import com.papaprogramador.presidenciales.commentsModule.model.interactor.CommentsInteractor;
import com.papaprogramador.presidenciales.commentsModule.model.interactor.CommentsInteractorClass;
import com.papaprogramador.presidenciales.commentsModule.view.CommentsView;
import com.papaprogramador.presidenciales.common.pojo.Comment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CommentsPresenterClass extends MvpBasePresenter<CommentsView>
		implements CommentsPresenter {

	private CommentsInteractor interactor;

	public CommentsPresenterClass() {
		interactor = new CommentsInteractorClass();
	}

	@Override
	public void onCreate() {
		EventBus.getDefault().register(this);
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void getComments(String opinionId) {
		interactor.getComments(opinionId);
	}

	@Override
	public void publishComment(Comment commentPublication) {
		interactor.publishComment(commentPublication);
	}

	@Override
	public void addCommentNotifier(String opinionId) {
		interactor.addCommentNotifier(opinionId);
	}

	@Override
	public void removeCommentNotifier(String opinionId) {
		interactor.removeCommentNotifier(opinionId);
	}

	@Subscribe
	@Override
	public void commentsEventsListener(final CommentEvent event) {
		if (event != null) {
			ifViewAttached(new ViewAction<CommentsView>() {
				@Override
				public void run(@NonNull CommentsView view) {
					switch (event.getEventType()) {
						case CommentEvent.INITIAL_DATA:
							view.addAllComments(event.getComments());
							break;
						case CommentEvent.SUCCES_ADD:
							view.addComment(event.getComment());
							break;
						case CommentEvent.NO_DATA:
							view.showNoData();
							break;
						case CommentEvent.ERROR:
							view.showError(event.getResMsg());
							break;
					}
				}
			});
		}
	}
}
