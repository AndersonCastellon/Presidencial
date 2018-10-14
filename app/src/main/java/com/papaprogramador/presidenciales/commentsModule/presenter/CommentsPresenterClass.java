package com.papaprogramador.presidenciales.commentsModule.presenter;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.commentsModule.events.CommentEvent;
import com.papaprogramador.presidenciales.commentsModule.model.interactor.CommentsInteractor;
import com.papaprogramador.presidenciales.commentsModule.model.interactor.CommentsInteractorClass;
import com.papaprogramador.presidenciales.commentsModule.view.CommentsView;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseUserAPI;
import com.papaprogramador.presidenciales.common.pojo.Comment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CommentsPresenterClass extends MvpBasePresenter<CommentsView>
		implements CommentsPresenter {

	private CommentsInteractor interactor;
	private FirebaseUserAPI userAPI;

	public CommentsPresenterClass() {
		userAPI = FirebaseUserAPI.getInstance();
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
	public void publishComment(String opinionId, String commentText) {
		interactor.publishComment(Comment.Builder()
				.opinionId(opinionId)
				.userId(userAPI.getUserId())
				.userName(userAPI.getUserName())
				.userPhotoUrl(userAPI.getPhotoProfile())
				.userPoliticlaFlagUrl(userAPI.getPoliticalFlag())
				.content(commentText)
				.build());
	}

	@Override
	public void deleteComment(Comment comment) {
		interactor.deleteComment(comment);
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
							view.showProgress();
							view.addAllComments(event.getComments());
							view.addCommentsNotifier();
							break;
						case CommentEvent.SUCCES_ADD:
							view.removeCommentsNotifier();
							view.hideProgress();
							view.addComment(event.getComment());
							view.addCommentsNotifier();
							break;
						case CommentEvent.SUCCES_REMOVED:
							view.addCommentsNotifier();
							view.hideProgress();
							view.deleteComment(event.getComment());
							break;
						case CommentEvent.NO_DATA:
							view.hideProgress();
							view.showNoData();
							break;
						case CommentEvent.ERROR:
							view.hideProgress();
							view.showError(event.getResMsg());
							break;
					}
					view.hideProgress();
				}
			});
		}
	}
}
