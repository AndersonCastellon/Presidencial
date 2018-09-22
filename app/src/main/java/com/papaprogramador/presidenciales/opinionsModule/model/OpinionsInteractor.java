package com.papaprogramador.presidenciales.opinionsModule.model;

import android.support.v4.util.Pair;

import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.common.BasicErrorEventCallback;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseUserAPI;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.opinionsModule.OpinionsContract;
import com.papaprogramador.presidenciales.opinionsModule.events.LikeEvent;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.FirebaseLikesDataSource;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.LikeDataSource;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.OpinionsEventListener;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.FirebaseOpinionDataSource;
import com.papaprogramador.presidenciales.common.pojo.Like;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OpinionsInteractor implements OpinionsContract.Interactor {

	private FirebaseOpinionDataSource firebaseOpinionDataSource;
	private FirebaseLikesDataSource firebaseLikesDataSource;
	private FirebaseUserAPI userAPI;

	public OpinionsInteractor() {
		userAPI = FirebaseUserAPI.getInstance();
		firebaseOpinionDataSource = new FirebaseOpinionDataSource();
		firebaseLikesDataSource = new FirebaseLikesDataSource();
	}

	@Override
	public void subscribeToOpinions(long lastOpinion) {
		firebaseOpinionDataSource.subscribeToOpinions(lastOpinion, new OpinionsEventListener() {
			@Override
			public void onChildAdded(final Opinion opinion) {
				postEventOpinion(opinion, OpinionEvent.SUCCES_ADD);
			}

			@Override
			public void onChildUpdated(Opinion opinion) {
				postEventOpinion(opinion, OpinionEvent.SUCCES_UPDATE);
			}

			@Override
			public void onChildRemoved(Opinion opinion) {
				postEventOpinion(opinion, OpinionEvent.SUCCES_REMOVE);
			}

			@Override
			public void onComplete() {
				postEventOpinion(OpinionEvent.ON_COMPLETE);
			}

			@Override
			public void onError(int resMsg) {
				postEventOpinion(OpinionEvent.ERROR_SERVER, resMsg);
			}
		});
	}

	@Override
	public void unsubscribeToOpinions() {
		firebaseOpinionDataSource.unsubscribeToOpinions();
	}

	@Override
	public void requestAddLikeNotifiers(String opinionId) {
		firebaseLikesDataSource.addLikeNotifier(opinionId, new LikeDataSource.LikeNotifierListener() {
			@Override
			public void onSuccess(Pair<Like, Boolean> likePair) {
				Like like = likePair.first;
				if (!Objects.requireNonNull(like).getUserId().equals(userAPI.getUserId())) {
					postEventLike(like, likePair.second ? LikeEvent.LIKE : LikeEvent.DISLIKE);
				}
			}

			@Override
			public void onError(Exception e) {
				postEventLike(R.string.error_server);
			}
		});
	}

	@Override
	public void requestRemoveLikeNotifiers(String opinionId) {
		firebaseLikesDataSource.removeLikeNotifier(opinionId);
	}

	@Override
	public void unsubscribeToLikes() {
		firebaseLikesDataSource.unsubscribeToLikes();
	}

	@Override
	public void removeOpinion(Opinion opinion) {
		firebaseOpinionDataSource.removeOpinion(opinion, new BasicErrorEventCallback() {
			@Override
			public void onSuccess() {
				postEventOpinion(OpinionEvent.SUCCES_REMOVE);
			}

			@Override
			public void onError(int typeEvent, int resMsg) {
				postEventOpinion(typeEvent, resMsg);
			}
		});
	}

	@Override
	public void onClickLike(final Like like) {
		firebaseLikesDataSource.toggleLike(like, new LikeDataSource.ToggleLikeListener() {
			@Override
			public void onSuccess(boolean result) {
				if (result) {
					postEventLike(like, LikeEvent.SUCCES_ADD);
				} else {
					postEventLike(like, LikeEvent.SUCCES_REMOVE);
				}
			}

			@Override
			public void onError(Exception e) {
				postEventLike(R.string.error_server);
			}
		});
	}

	private void postEventLike(int resMsg) {
		postEventLike(null, 0, resMsg);
	}

	private void postEventLike(Like like, int typeEvent) {
		postEventLike(like, typeEvent, 0);
	}

	private void postEventLike(Like like, int typeEvent, int resMsg) {
		LikeEvent likeEvent = new LikeEvent();
		likeEvent.setLike(like);
		likeEvent.setTypeEvent(typeEvent);
		likeEvent.setResMsg(resMsg);
		EventBus.getDefault().post(likeEvent);
	}

	private void postEventOpinion(int typeEvent) {
		postEventOpinion(null, typeEvent, 0);
	}

	private void postEventOpinion(int typeEvent, int resMsg) {
		postEventOpinion(null, typeEvent, resMsg);
	}

	private void postEventOpinion(Opinion opinion, int typeEvent) {
		postEventOpinion(opinion, typeEvent, 0);
	}

	private void postEventOpinion(Opinion opinion, int typeEvent, int resMsg) {
		OpinionEvent opinionEvent = new OpinionEvent();
		opinionEvent.setOpinion(opinion);
		opinionEvent.setTypeEvent(typeEvent);
		opinionEvent.setResMsg(resMsg);
		EventBus.getDefault().post(opinionEvent);
	}
}
