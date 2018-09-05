package com.papaprogramador.presidenciales.opinionsModule.model;

import com.papaprogramador.presidenciales.common.BasicErrorEventCallback;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.opinionsModule.OpinionsContract;
import com.papaprogramador.presidenciales.opinionsModule.events.LikeEvent;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.FlowableLikes;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.LikesEventListener;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.OpinionsEventListener;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.FlowableOpinion;
import com.papaprogramador.presidenciales.common.pojo.Like;

import org.greenrobot.eventbus.EventBus;

public class OpinionsInteractor implements OpinionsContract.Interactor {

	private FlowableOpinion flowableOpinion;
	private FlowableLikes flowableLikes;

	public OpinionsInteractor() {
		flowableOpinion = new FlowableOpinion();
		flowableLikes = new FlowableLikes();
	}

	@Override
	public void subscribeToOpinions(long lastOpinion) {
		flowableOpinion.subscribeToOpinions(lastOpinion, new OpinionsEventListener() {
			@Override
			public void onChildAdded(Opinion opinion) {
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
		flowableOpinion.unsubscribeToOpinions();
	}

	@Override
	public void subscribeToLikes() {
		flowableLikes.subscribeToLikes(new LikesEventListener() {
			@Override
			public void onChildAdded(Like like) {
				postEventLike(like, LikeEvent.SUCCES_ADD);
			}

			@Override
			public void onChildRemoved(Like like) {
				postEventLike(like, LikeEvent.SUCCES_REMOVE);
			}

			@Override
			public void onError(int resMsg) {
				postEventLike(LikeEvent.ERROR, resMsg);
			}
		});
	}

	@Override
	public void unsubscribeToLikes() {
		flowableLikes.unsubscribeToOpinions();
	}

	@Override
	public void removeOpinion(Opinion opinion) {
		flowableOpinion.removeOpinion(opinion, new BasicErrorEventCallback() {
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
	public void likeAdd(Opinion opinion) {

	}

	@Override
	public void likeRemove(Opinion opinion) {

	}

	private void postEventOpinion(int typeEvent){
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

	private void postEventLike(int typeEvent, int resMsg){
		postEventLike(null, typeEvent, resMsg);
	}

	private void postEventLike(Like like, int typeEvent){
		postEventLike(like, typeEvent, 0);
	}

	private void postEventLike(Like like, int typeEvent, int resMsg) {
		LikeEvent likeEvent = new LikeEvent();
		likeEvent.setLike(like);
		likeEvent.setTypeEvent(typeEvent);
		likeEvent.setResMsg(resMsg);
		EventBus.getDefault().post(likeEvent);
	}
}
