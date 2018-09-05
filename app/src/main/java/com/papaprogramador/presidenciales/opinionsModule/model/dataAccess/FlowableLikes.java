package com.papaprogramador.presidenciales.opinionsModule.model.dataAccess;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseUserAPI;
import com.papaprogramador.presidenciales.common.pojo.Like;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

import static io.reactivex.BackpressureStrategy.BUFFER;

public class FlowableLikes {
	private static final String PATH_LIKES = "Likes";

	private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
	private FirebaseUserAPI firebaseUserAPI;
	private CompositeDisposable disposable;

	public FlowableLikes() {
		mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
		firebaseUserAPI = FirebaseUserAPI.getInstance();
		disposable = new CompositeDisposable();
	}

	private DatabaseReference getLikesReference() {
		return mDatabaseAPI.getmReference().child(PATH_LIKES);
	}

	public void subscribeToLikes(final LikesEventListener listener) {

		Query query = getLikesReference().child(firebaseUserAPI.getUserId());

		disposable.add(RxFirebaseDatabase.observeChildEvent(query, BUFFER)
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new ResourceSubscriber<RxFirebaseChildEvent<DataSnapshot>>() {
					@Override
					public void onNext(RxFirebaseChildEvent<DataSnapshot> dataSnapshotRxFirebaseChildEvent) {
						switch (dataSnapshotRxFirebaseChildEvent.getEventType()) {
							case ADDED:
								listener.onChildAdded(getLike(dataSnapshotRxFirebaseChildEvent.getValue()));
								break;
							case REMOVED:
								listener.onChildRemoved(getLike(dataSnapshotRxFirebaseChildEvent.getValue()));
								break;
						}
					}

					@Override
					public void onError(Throwable t) {

					}

					@Override
					public void onComplete() {

					}
				}));

	}

	private Like getLike(DataSnapshot dataSnapshot) {
		Like like = dataSnapshot.getValue(Like.class);

		if (like != null) {
			like.setOpinionId(dataSnapshot.getKey());
		}
		return like;
	}

	public void unsubscribeToOpinions() {
		if (!disposable.isDisposed()) {
			disposable.dispose();
		}
	}
}
