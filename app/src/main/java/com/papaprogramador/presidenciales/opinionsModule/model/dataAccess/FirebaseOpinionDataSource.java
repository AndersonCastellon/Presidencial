package com.papaprogramador.presidenciales.opinionsModule.model.dataAccess;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.common.ChangeEventListener;
import com.papaprogramador.presidenciales.common.LikeValueEventListener;
import com.papaprogramador.presidenciales.common.OpinionValueEventListener;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.papaprogramador.presidenciales.common.pojo.Like;
import com.papaprogramador.presidenciales.common.pojo.Opinion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class FirebaseOpinionDataSource implements OpinionDataSource {

	private static final String PATH_OPINIONS = "Opinions";
	private static final String PATH_LIKES = "Likes";

	private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

	private final Map<String, ChangeEventListener<Opinion>> listenerOpinionMap = new HashMap<>();
	private final Map<String, ChangeEventListener<Long>> listenerLikeMap = new HashMap<>();

	public FirebaseOpinionDataSource() {
		mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
	}

	private DatabaseReference getOpinionsReference() {
		return mDatabaseAPI.getReference().child(PATH_OPINIONS);
	}

	@Override
	public Single<List<Opinion>> getOpinions(final long timeStamp) {
		return Single.create(new SingleOnSubscribe<List<Opinion>>() {
			@Override
			public void subscribe(final SingleEmitter<List<Opinion>> emitter) {

				final Query query;
				if (timeStamp == 0) {
					query = getOpinionsReference().orderByChild(Opinion.ORDER_BY).limitToLast(Constans.OPINIONS_PER_PAGE);
				} else {
					query = getOpinionsReference().orderByChild(Opinion.ORDER_BY).endAt(timeStamp).limitToLast(Constans.OPINIONS_PER_PAGE);
				}

				query.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(final DataSnapshot opinionSnapshot) {
						query.removeEventListener(this);

						for (final DataSnapshot dataOpinion : opinionSnapshot.getChildren()) {
							final DatabaseReference likeRef = getOpinionsReference().child(dataOpinion.getKey())
									.child(PATH_LIKES);
							likeRef.addValueEventListener(new ValueEventListener() {
								@Override
								public void onDataChange(DataSnapshot likeSnapshot) {
									likeRef.removeEventListener(this);
									List<String> userLikes = new ArrayList<>();
									for (DataSnapshot dataLike : likeSnapshot.getChildren()) {
										userLikes.add(dataLike.getKey());
									}
									List<Opinion> opinions = new ArrayList<>();

									Opinion opinion = dataOpinion.getValue(Opinion.class);
									if (opinion != null) {
										opinion.setOpinionId(dataOpinion.getKey());
										opinion.setUserLikes(userLikes);
									}
									emitter.onSuccess(opinions);
								}

								@Override
								public void onCancelled(DatabaseError databaseError) {

								}
							});
						}

					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
			}
		});
	}

	@Override
	public Observable<Pair<Boolean, Opinion>> addOpinionNotifier() {
		return Observable.create(new ObservableOnSubscribe<Pair<Boolean, Opinion>>() {
			@Override
			public void subscribe(final ObservableEmitter<Pair<Boolean, Opinion>> emitter) {

				if (listenerOpinionMap.containsKey(PATH_OPINIONS)) {
					getOpinionsReference().removeEventListener(listenerOpinionMap.get(PATH_OPINIONS));
					listenerOpinionMap.remove(PATH_OPINIONS);
				}

				ChangeEventListener<Opinion> listener = new ChangeEventListener<Opinion>(getOpinionsReference(),
						Opinion.class) {
					@Override
					protected void onChildAdded(String key, Opinion data) {
						emitter.onNext(new Pair<>(true, createOpinion(key, data)));
					}

					@Override
					protected void onChildRemoved(String key, Opinion data) {
						emitter.onNext(new Pair<>(false, createOpinion(key, data)));
					}
				};

				listenerOpinionMap.put(PATH_OPINIONS, listener);
			}
		});
	}

	@Override
	public Single<Boolean> removeOpinionNotifier() {
		return Single.create(new SingleOnSubscribe<Boolean>() {
			@Override
			public void subscribe(SingleEmitter<Boolean> emitter) {
				if (listenerOpinionMap.containsKey(PATH_OPINIONS)) {
					getOpinionsReference().removeEventListener(listenerOpinionMap.get(PATH_OPINIONS));
					listenerOpinionMap.remove(PATH_OPINIONS);
					emitter.onSuccess(true);
				} else {
					emitter.onSuccess(false);
				}

			}
		});
	}

	@Override
	public Single<Boolean> deleteOpinion(final Opinion opinion) {
		return Single.create(new SingleOnSubscribe<Boolean>() {
			@Override
			public void subscribe(final SingleEmitter<Boolean> emitter) {
				getOpinionsReference().child(opinion.getOpinionId())
						.removeValue(new DatabaseReference.CompletionListener() {
							@Override
							public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
								if (databaseError == null) {
									emitter.onSuccess(true);
								} else {
									emitter.onSuccess(false);
								}
							}
						});
			}
		});
	}

	@Override
	public Single<Boolean> toggleLike(final Like like) {
		return Single.create(new SingleOnSubscribe<Boolean>() {
			@Override
			public void subscribe(final SingleEmitter<Boolean> emitter) {

				final DatabaseReference likeRef = getOpinionsReference()
						.child(like.getOpinionId()).child(PATH_LIKES);

				likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if (dataSnapshot.hasChild(like.getUserId())) {
							likeRef.child(like.getUserId()).removeValue()
									.addOnFailureListener(new OnFailureListener() {
										@Override
										public void onFailure(@NonNull Exception e) {
											emitter.onError(e);
										}
									}).addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									emitter.onSuccess(false);
								}
							});
						} else {
							likeRef.child(like.getUserId()).setValue(ServerValue.TIMESTAMP)
									.addOnFailureListener(new OnFailureListener() {
										@Override
										public void onFailure(@NonNull Exception e) {
											emitter.onError(e);
										}
									}).addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									emitter.onSuccess(true);
								}
							});
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
						emitter.onError(databaseError.toException());
					}
				});
			}
		});
	}

	@Override
	public Observable<Pair<Like, Boolean>> addLikeNotifier(final String opinionId) {
		return Observable.create(new ObservableOnSubscribe<Pair<Like, Boolean>>() {
			@Override
			public void subscribe(final ObservableEmitter<Pair<Like, Boolean>> emitter) {

				final DatabaseReference likeRef = getOpinionsReference()
						.child(opinionId).child(PATH_LIKES);

				if (listenerLikeMap.containsKey(opinionId)) {
					likeRef.removeEventListener(listenerLikeMap.get(opinionId));
					listenerLikeMap.remove(opinionId);
				}

				ChangeEventListener<Long> eventListener = new ChangeEventListener<Long>(likeRef, Long.class) {
					@Override
					protected void onChildAdded(String key, Long data) {
						Like like = Like.Builder()
								.opinionId(opinionId)
								.userId(key)
								.build();
						emitter.onNext(new Pair<>(like, true));
					}

					@Override
					protected void onChildRemoved(String key, Long data) {
						Like like = Like.Builder()
								.opinionId(opinionId)
								.userId(key)
								.build();
						emitter.onNext(new Pair<>(like, false));
					}
				};

				listenerLikeMap.put(opinionId, eventListener);
			}
		});
	}

	@Override
	public Single<Boolean> removeLikeNotifier(final String opinionId) {
		return Single.create(new SingleOnSubscribe<Boolean>() {
			@Override
			public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
				final DatabaseReference likeRef = getOpinionsReference()
						.child(opinionId).child(PATH_LIKES);

				if (listenerLikeMap.containsKey(opinionId)) {
					likeRef.removeEventListener(listenerLikeMap.get(opinionId));
					listenerLikeMap.remove(opinionId);
					emitter.onSuccess(true);
				}
			}
		});
	}

	private Opinion createOpinion(String opinionId, Opinion data) {
		return Opinion.Builder()
				.opinionId(opinionId)
				.userId(data.getUserId())
				.userName(data.getUserName())
				.urlPhotoProfile(data.getUrlPhotoProfile())
				.urlPoliticalFlag(data.getUrlPoliticalFlag())
				.content(data.getContent())
				.urlOpinionImage(data.getUrlOpinionImage())
				.dataTime(data.getDataTime())
				.build();
	}
}
