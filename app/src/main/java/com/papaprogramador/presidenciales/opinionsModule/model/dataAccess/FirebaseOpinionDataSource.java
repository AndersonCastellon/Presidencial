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
import com.papaprogramador.presidenciales.common.pojo.Like;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.opinionsModule.events.OpinionEvent;

import java.util.ArrayList;
import java.util.List;

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
	private FirebaseLikesDataSource firebaseLikesDataSource;
	private CompositeDisposable disposable;


	public FirebaseOpinionDataSource() {
		firebaseLikesDataSource = new FirebaseLikesDataSource();
		mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
		disposable = new CompositeDisposable();
	}

	private DatabaseReference getOpinionsReference() {
		return mDatabaseAPI.getmReference().child(PATH_OPINIONS);
	}


	public void subscribeToOpinions(long lastOpinion, List<Opinion> opinionList, final OpinionsEventListener listener) {

		final Query query;
		if (lastOpinion == 0) {
			query = getOpinionsReference().orderByChild(Opinion.ORDER_BY).limitToLast(Constans.OPINIONS_PER_PAGE);
		} else {
			query = getOpinionsReference().orderByChild(Opinion.ORDER_BY).endAt(lastOpinion).limitToLast(Constans.OPINIONS_PER_PAGE);
		}

		final List<String> currentOpinion = new ArrayList<>();
		for (Opinion opinion : opinionList) {
			currentOpinion.add(opinion.getOpinionId());
		}

		disposable.add(RxFirebaseDatabase.observeChildEvent(query, BUFFER)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new ResourceSubscriber<RxFirebaseChildEvent<DataSnapshot>>() {
					@Override
					public void onNext(final RxFirebaseChildEvent<DataSnapshot> dataSnapshotRxFirebaseChildEvent) {

						if (!currentOpinion.contains(dataSnapshotRxFirebaseChildEvent.getValue().getKey())) {
							switch (dataSnapshotRxFirebaseChildEvent.getEventType()) {
								case ADDED:
									firebaseLikesDataSource.getLikes(dataSnapshotRxFirebaseChildEvent.getValue().getKey(),
											new LikeDataSource.ListLikesListener() {
												@Override
												public void onSuccess(List<Like> likes) {
													listener.onChildAdded(getOpinion(dataSnapshotRxFirebaseChildEvent.getValue(), likes));
												}

												@Override
												public void onError(Exception e) {

												}
											});
									break;
								case CHANGED:
									firebaseLikesDataSource.getLikes(dataSnapshotRxFirebaseChildEvent.getValue().getKey(),
											new LikeDataSource.ListLikesListener() {
												@Override
												public void onSuccess(List<Like> likes) {
													listener.onChildUpdated(getOpinion(dataSnapshotRxFirebaseChildEvent.getValue(), likes));
												}

												@Override
												public void onError(Exception e) {

												}
											});
									break;
								case REMOVED:
									listener.onChildRemoved(getOpinion(dataSnapshotRxFirebaseChildEvent.getValue()));
									break;
							}
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
		return getOpinion(dataSnapshot, null);
	}

	private Opinion getOpinion(DataSnapshot dataSnapshot, List<Like> likes) {
		Opinion opinion = dataSnapshot.getValue(Opinion.class);

		if (opinion != null) {
			opinion.setOpinionId(dataSnapshot.getKey());
			if (likes != null) {
				for (Like like : likes) {
					opinion.addUserLikeId(like.getUserId());
				}
			}
		}
		return opinion;
	}

	public void unsubscribeToOpinions() {
		if (!disposable.isDisposed()) {
			disposable.dispose();
		}
	}
}
