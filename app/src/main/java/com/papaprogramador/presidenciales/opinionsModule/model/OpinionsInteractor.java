package com.papaprogramador.presidenciales.opinionsModule.model;

import android.support.v4.util.Pair;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.opinionsModule.OpinionsContract;
import com.papaprogramador.presidenciales.opinionsModule.events.LikeEvent;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;
import com.papaprogramador.presidenciales.opinionsModule.model.dataAccess.FirebaseOpinionDataSource;
import com.papaprogramador.presidenciales.common.pojo.Like;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OpinionsInteractor implements OpinionsContract.Interactor {

	private FirebaseOpinionDataSource opinionDataSource;

	public OpinionsInteractor() {
		opinionDataSource = new FirebaseOpinionDataSource();
	}

	@Override
	public void getOpinions(long timeStamp) {
		opinionDataSource.getOpinions(timeStamp)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SingleObserver<List<Opinion>>() {
					@Override
					public void onSubscribe(Disposable d) {
					}

					@Override
					public void onSuccess(List<Opinion> opinionList) {
						postEventOpinion(opinionList, OpinionEvent.INITIAL_DATA);
					}

					@Override
					public void onError(Throwable e) {
					}
				});
	}

	@Override
	public void addOpinionNotifier() {
		opinionDataSource.addOpinionNotifier()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<Pair<Boolean, Opinion>>() {
					@Override
					public void onSubscribe(Disposable d) {

					}

					@Override
					public void onNext(Pair<Boolean, Opinion> pair) {
						if (pair.first){
							postEventOpinion(pair.second, OpinionEvent.SUCCES_ADD);
						} else {
							postEventOpinion(pair.second, OpinionEvent.SUCCES_REMOVE);
						}
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onComplete() {

					}
				});
	}

	@Override
	public void removeOpinionNotifier() {
		opinionDataSource.removeOpinionNotifier()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SingleObserver<Boolean>() {
					@Override
					public void onSubscribe(Disposable d) {

					}
					@Override
					public void onSuccess(Boolean aBoolean) {

					}

					@Override
					public void onError(Throwable e) {

					}
				});
	}

	@Override
	public void toggleLike(Like like) {
		opinionDataSource.toggleLike(like)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SingleObserver<Boolean>() {
					@Override
					public void onSubscribe(Disposable d) {

					}

					@Override
					public void onSuccess(Boolean aBoolean) {
						if (!aBoolean){
							postEventLike(R.string.error_while_like, LikeEvent.ERROR);
						}
					}

					@Override
					public void onError(Throwable e) {

					}
				});
	}

	@Override
	public void addLikeNotifier(String opinionId) {
		opinionDataSource.addLikeNotifier(opinionId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<Pair<Like, Boolean>>() {
					@Override
					public void onSubscribe(Disposable d) {

					}

					@Override
					public void onNext(Pair<Like, Boolean> pair) {
						if (pair.second){
							postEventLike(pair.first, LikeEvent.LIKE);
						} else {
							postEventLike(pair.first, LikeEvent.DISLIKE);
						}
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onComplete() {

					}
				});
	}

	@Override
	public void removeLikeNotifier(String opinionId) {
		opinionDataSource.removeLikeNotifier(opinionId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SingleObserver<Boolean>() {
					@Override
					public void onSubscribe(Disposable d) {

					}

					@Override
					public void onSuccess(Boolean aBoolean) {

					}

					@Override
					public void onError(Throwable e) {

					}
				});
	}

	@Override
	public void deleteOpinion(Opinion opinion) {
		opinionDataSource.deleteOpinion(opinion)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SingleObserver<Boolean>() {
					@Override
					public void onSubscribe(Disposable d) {

					}

					@Override
					public void onSuccess(Boolean aBoolean) {
						if (aBoolean){
							postEventOpinion(OpinionEvent.SUCCES_REMOVE, R.string.delete_opinion);
						} else {
							postEventOpinion(OpinionEvent.ERROR_TO_REMOVE, R.string.error_while_delete_opinion);
						}
					}

					@Override
					public void onError(Throwable e) {

					}
				});
	}

	private void postEventLike(int resMsg, int typeEvent) {
		postEventLike(null, typeEvent, resMsg);
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

	private void postEventOpinion(List<Opinion> opinions, int typeEvent) {
		postEventOpinion(opinions, null, typeEvent, 0);
	}

	private void postEventOpinion(int typeEvent) {
		postEventOpinion(null, null, typeEvent, 0);
	}

	private void postEventOpinion(int typeEvent, int resMsg) {
		postEventOpinion(null, null, typeEvent, resMsg);
	}

	private void postEventOpinion(Opinion opinion, int typeEvent) {
		postEventOpinion(null, opinion, typeEvent, 0);
	}

	private void postEventOpinion(List<Opinion> opinions, Opinion opinion, int typeEvent, int resMsg) {
		OpinionEvent opinionEvent = new OpinionEvent();
		opinionEvent.setOpinions(opinions);
		opinionEvent.setOpinion(opinion);
		opinionEvent.setTypeEvent(typeEvent);
		opinionEvent.setResMsg(resMsg);
		EventBus.getDefault().post(opinionEvent);
	}
}
