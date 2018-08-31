package com.papaprogramador.presidenciales.opinionsModule.model;

import com.papaprogramador.presidenciales.common.BasicErrorEventCallback;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.opinionsModule.OpinionsContract;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.OpinionsEventListener;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.RealtimeSubscriberOpinions;

import org.greenrobot.eventbus.EventBus;

public class OpinionsInteractor implements OpinionsContract.Interactor {

	private RealtimeSubscriberOpinions mSubscriberOpinions;

	public OpinionsInteractor() {
		mSubscriberOpinions = new RealtimeSubscriberOpinions();
	}

	@Override
	public void subscribeToOpinions(long lastOpinion) {
		mSubscriberOpinions.subscribeToOpinions(lastOpinion, new OpinionsEventListener() {
			@Override
			public void onChildAdded(Opinion opinion) {
				postEvent(opinion, OpinionEvent.SUCCES_ADD);
			}

			@Override
			public void onChildUpdated(Opinion opinion) {
				postEvent(opinion, OpinionEvent.SUCCES_UPDATE);
			}

			@Override
			public void onChildRemoved(Opinion opinion) {
				postEvent(opinion, OpinionEvent.SUCCES_REMOVE);
			}

			@Override
			public void onComplete() {
				postEvent(OpinionEvent.ON_COMPLETE);
			}

			@Override
			public void onError(int resMsg) {
				postEvent(OpinionEvent.ERROR_SERVER, resMsg);
			}
		});
	}

	@Override
	public void unsubscribeToOpinions() {
		mSubscriberOpinions.unsubscribeToOpinions();
	}

	@Override
	public void removeOpinion(Opinion opinion) {
		mSubscriberOpinions.removeOpinion(opinion, new BasicErrorEventCallback() {
			@Override
			public void onSuccess() {
				postEvent(OpinionEvent.SUCCES_REMOVE);
			}

			@Override
			public void onError(int typeEvent, int resMsg) {
				postEvent(typeEvent, resMsg);
			}
		});
	}

	private void postEvent(int typeEvent){
		postEvent(null, typeEvent, 0);
	}

	private void postEvent(int typeEvent, int resMsg) {
		postEvent(null, typeEvent, resMsg);
	}

	private void postEvent(Opinion opinion, int typeEvent) {
		postEvent(opinion, typeEvent, 0);
	}

	private void postEvent(Opinion opinion, int typeEvent, int resMsg) {
		OpinionEvent opinionEvent = new OpinionEvent();
		opinionEvent.setOpinion(opinion);
		opinionEvent.setTypeEvent(typeEvent);
		opinionEvent.setResMsg(resMsg);
		EventBus.getDefault().post(opinionEvent);
	}
}
