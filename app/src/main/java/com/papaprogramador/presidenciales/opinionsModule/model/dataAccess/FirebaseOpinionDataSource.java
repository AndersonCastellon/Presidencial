package com.papaprogramador.presidenciales.opinionsModule.model.dataAccess;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.common.BasicErrorEventCallback;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseUserAPI;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

import static io.reactivex.BackpressureStrategy.BUFFER;

public class FirebaseOpinionDataSource {

	private static final String PATH_OPINIONS = "Opinions";

	private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
	private CompositeDisposable disposable;


	public FirebaseOpinionDataSource() {
		mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
		disposable = new CompositeDisposable();
	}

	private DatabaseReference getOpinionsReference() {
		return mDatabaseAPI.getmReference().child(PATH_OPINIONS);
	}


	public void subscribeToOpinions(long lastOpinion, final OpinionsEventListener listener) {

		final Query query;
		if (lastOpinion == 0) {
			query = getOpinionsReference()
					.orderByChild(Opinion.ORDER_BY)
					.limitToLast(Constans.OPINIONS_PER_PAGE);
		} else {
			query = getOpinionsReference()
					.orderByChild(Opinion.ORDER_BY)
					.endAt(lastOpinion)
					.limitToLast(Constans.OPINIONS_PER_PAGE);
		}

		disposable.add(RxFirebaseDatabase.observeChildEvent(query, BUFFER)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new ResourceSubscriber<RxFirebaseChildEvent<DataSnapshot>>() {
					@Override
					public void onNext(RxFirebaseChildEvent<DataSnapshot> dataSnapshotRxFirebaseChildEvent) {
						switch (dataSnapshotRxFirebaseChildEvent.getEventType()) {
							case ADDED:
								listener.onChildAdded(getOpinion(dataSnapshotRxFirebaseChildEvent.getValue()));
								break;
							case CHANGED:
								listener.onChildUpdated(getOpinion(dataSnapshotRxFirebaseChildEvent.getValue()));
								break;
							case REMOVED:
								listener.onChildRemoved(getOpinion(dataSnapshotRxFirebaseChildEvent.getValue()));
								break;
						}
					}

					@Override
					public void onError(Throwable t) {
						t.getMessage();
						t.getCause();
					}

					@Override
					public void onComplete() {
						listener.onComplete();
					}
				}));

	}

	public void removeOpinion(Opinion opinion, final BasicErrorEventCallback callback) {
		getOpinionsReference().child(opinion.getOpinionId())
				.removeValue(new DatabaseReference.CompletionListener() {
					@Override
					public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
						if (databaseError == null) {
							callback.onSuccess();
						} else {
							switch (databaseError.getCode()) {
								case DatabaseError.PERMISSION_DENIED:
									callback.onError(OpinionEvent.ERROR_TO_REMOVE, R.string.opinion_error_remove);
									break;
								default:
									callback.onError(OpinionEvent.ERROR_SERVER, R.string.error_server);
							}
						}
					}
				});
	}

	private Opinion getOpinion(DataSnapshot dataSnapshot) {
		Opinion opinion = dataSnapshot.getValue(Opinion.class);

		if (opinion != null) {
			opinion.setOpinionId(dataSnapshot.getKey());
		}
		return opinion;
	}

	public void unsubscribeToOpinions() {
		if (!disposable.isDisposed()) {
			disposable.dispose();
		}
	}
}
