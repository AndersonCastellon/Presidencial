package com.papaprogramador.presidenciales.opinionsModule.model.dataAccess;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.papaprogramador.presidenciales.common.ChangeEventListener;
import com.papaprogramador.presidenciales.common.FirebaseRxDatabase;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseUserAPI;
import com.papaprogramador.presidenciales.common.pojo.Like;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

import static io.reactivex.BackpressureStrategy.BUFFER;

public class FirebaseLikesDataSource implements LikeDataSource {
	private static final String PATH_OPINIONS = "Opinions";
	private static final String PATH_LIKES = "Likes";

	private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
	private CompositeDisposable disposable;

	private final Map<String, ChangeEventListener<Long>> listenerMap = new HashMap<>();


	public FirebaseLikesDataSource() {
		mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
		disposable = new CompositeDisposable();
	}

	private DatabaseReference getReference() {
		return mDatabaseAPI.getmReference();
	}

	@Override
	public void getLikes(final String opinionId, final ListLikesListener listener) {

		final Query query = getReference().child(PATH_OPINIONS).child(opinionId).child(PATH_LIKES);

		disposable.add(RxFirebaseDatabase.observeValueEvent(query)
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new ResourceSubscriber<DataSnapshot>() {
					@Override
					public void onNext(DataSnapshot dataSnapshot) {
						List<Like> likes = new LinkedList<>();
						for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
							likes.add(
									new Like.Builder()
											.opinionId(opinionId)
											.userId(snapshot.getKey())
											.build());
						}

						listener.onSuccess(likes);
					}

					@Override
					public void onError(Throwable t) {
						listener.onError((Exception) t);
					}

					@Override
					public void onComplete() {

					}
				}));
	}

	@Override
	public void toggleLike(final Like like, final ToggleLikeListener listener) {

		final DatabaseReference likeRef = getReference().child(PATH_OPINIONS)
				.child(like.getOpinionId()).child(PATH_LIKES);

		disposable.add(FirebaseRxDatabase.observeValueEvent(likeRef, BUFFER)
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<DataSnapshot>() {
					@Override
					public void accept(DataSnapshot dataSnapshot) throws Exception {
						if (dataSnapshot.hasChild(like.getUserId())) {
							likeRef.child(like.getUserId()).removeValue()
									.addOnFailureListener(new OnFailureListener() {
										@Override
										public void onFailure(@NonNull Exception e) {
											listener.onError(e);
										}
									}).addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									listener.onSuccess(false);
								}
							});
						} else {
							likeRef.child(like.getUserId()).setValue(ServerValue.TIMESTAMP)
									.addOnFailureListener(new OnFailureListener() {
										@Override
										public void onFailure(@NonNull Exception e) {
											listener.onError(e);
										}
									}).addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									listener.onSuccess(true);
								}
							});
						}
					}
				}));
	}

	@Override
	public void addLikeNotifier(final String opinionId, final LikeNotifierListener listener) {
		final DatabaseReference likeRef = getReference().child(PATH_OPINIONS)
				.child(opinionId).child(PATH_LIKES);

		if (listenerMap.containsKey(opinionId)) {
			likeRef.removeEventListener(listenerMap.get(opinionId));
			listenerMap.remove(opinionId);
		}

		ChangeEventListener<Long> eventListener = new ChangeEventListener<Long>(likeRef, Long.class) {
			@Override
			protected void onChildAdded(String key, Long data) {
				Like like = Like.Builder()
						.opinionId(opinionId)
						.userId(key)
						.build();
				listener.onSuccess(new Pair<>(like, true));
			}

			@Override
			protected void onChildRemoved(String key, Long data) {
				Like like = Like.Builder()
						.opinionId(opinionId)
						.userId(key)
						.build();
				listener.onSuccess(new Pair<>(like, false));
			}
		};

		listenerMap.put(opinionId, eventListener);

	}

	@Override
	public void removeLikeNotifier(String opinionId) {
		if (listenerMap.containsKey(opinionId)) {
			DatabaseReference likeRef = getReference().child(PATH_OPINIONS)
					.child(opinionId).child(PATH_LIKES);
			likeRef.removeEventListener(listenerMap.get(opinionId));
			listenerMap.remove(opinionId);
		}
	}

	public void unsubscribeToLikes() {
		if (!disposable.isDisposed()) {
			disposable.dispose();
		}
	}
}
