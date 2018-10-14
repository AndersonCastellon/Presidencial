package com.papaprogramador.presidenciales.commentsModule.model.dataAccess;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.commentsModule.events.CommentEvent;
import com.papaprogramador.presidenciales.common.ChangeEventListener;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.papaprogramador.presidenciales.common.pojo.Comment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class FirebaseCommentsDataSource implements CommentsDataSource {

	private static final String PATH_OPINION = "Opinions";
	private static final String PATH_COMMENTS = "Comments";

	private FirebaseRealtimeDatabaseAPI databaseAPI;
	private Map<String, ChangeEventListener<Comment>> listenerMap = new HashMap<>();

	public FirebaseCommentsDataSource() {
		databaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
	}

	@Override
	public Observable<List<Comment>> getComments(final String opinionId) {
		return Observable.create(new ObservableOnSubscribe<List<Comment>>() {
			@Override
			public void subscribe(final ObservableEmitter<List<Comment>> emitter) {
				final DatabaseReference commentRef = databaseAPI.getReference().child(PATH_OPINION)
						.child(opinionId)
						.child(PATH_COMMENTS);

				commentRef.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						emitter.onNext(createListComments(dataSnapshot));
						commentRef.removeEventListener(this);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
						emitter.onError(databaseError.toException());
						commentRef.removeEventListener(this);
					}
				});
			}
		});
	}

	@Override
	public Single<Boolean> publishComment(final Comment commentPublication) {
		return Single.create(new SingleOnSubscribe<Boolean>() {
			@Override
			public void subscribe(final SingleEmitter<Boolean> emitter) {
				DatabaseReference commentsRef = databaseAPI.getReference().child(PATH_OPINION)
						.child(commentPublication.getOpinionId())
						.child(PATH_COMMENTS);
				DatabaseReference commentRef = commentsRef.push();

				commentRef.setValue(commentPublication).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						if (!emitter.isDisposed()) {
							emitter.onError(e);
						}
					}
				}).addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()) {
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
	public Single<Pair<Boolean, Integer>> deleteComment(final Comment comment) {
		return Single.create(new SingleOnSubscribe<Pair<Boolean, Integer>>() {
			@Override
			public void subscribe(final SingleEmitter<Pair<Boolean, Integer>> emitter) {
				DatabaseReference commentRef = databaseAPI.getReference()
						.child(PATH_OPINION)
						.child(comment.getOpinionId())
						.child(PATH_COMMENTS)
						.child(comment.getCommentId());

				commentRef.removeValue(new DatabaseReference.CompletionListener() {
					@Override
					public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
						if (databaseError == null) {
							emitter.onSuccess(new Pair<>(true, R.string.success_delete_comment));
						} else {
							switch (databaseError.getCode()) {
								case DatabaseError.PERMISSION_DENIED:
									emitter.onSuccess(new Pair<>(false, R.string.error_while_delete_comment));
									break;
								case DatabaseError.NETWORK_ERROR:
									emitter.onSuccess(new Pair<>(false, R.string.error_red));
									break;
								default:
									emitter.onSuccess(new Pair<>(false, R.string.error_server));
							}
						}
					}
				});
			}
		});
	}

	@Override
	public Observable<Pair<Integer, Comment>> addCommentNotifier(final String opinionId) {
		return Observable.create(new ObservableOnSubscribe<Pair<Integer, Comment>>() {
			@Override
			public void subscribe(final ObservableEmitter<Pair<Integer, Comment>> emitter) {
				DatabaseReference commentRef = databaseAPI.getReference()
						.child(PATH_OPINION)
						.child(opinionId).child(PATH_COMMENTS);

				if (listenerMap.containsKey(opinionId)) {
					commentRef.removeEventListener(listenerMap.get(opinionId));
					listenerMap.remove(opinionId);
				}

				ChangeEventListener<Comment> listener = new ChangeEventListener<Comment>(commentRef,
						Comment.class) {
					@Override
					protected void onChildAdded(String commentId, Comment commentData) {
						emitter.onNext(new Pair<>(CommentEvent.SUCCES_ADD, createComment(commentId, commentData)));
					}

					@Override
					protected void onChildRemoved(String key, Comment data) {
						emitter.onNext(new Pair<>(CommentEvent.SUCCES_REMOVED, createComment(key, data)));
					}
				};
				listenerMap.put(opinionId, listener);
			}
		});
	}

	@Override
	public Single<Boolean> removeCommentNotifier(final String opinionId) {
		return Single.create(new SingleOnSubscribe<Boolean>() {
			@Override
			public void subscribe(SingleEmitter<Boolean> emitter) {
				if (listenerMap.containsKey(opinionId)) {
					DatabaseReference commentRef = databaseAPI.getReference()
							.child(PATH_OPINION)
							.child(opinionId)
							.child(PATH_COMMENTS);

					commentRef.removeEventListener(listenerMap.get(opinionId));
					listenerMap.remove(opinionId);
				}
			}
		});
	}

	private List<Comment> createListComments(DataSnapshot dataSnapshot) {
		List<Comment> comments = new LinkedList<>();
		for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
			Comment comment = snapshot.getValue(Comment.class);
			if (comment != null) {
				comment.setCommentId(snapshot.getKey());
				comments.add(comment);
			}
		}
		return comments;
	}

	private Comment createComment(String commentId, Comment commentData) {
		return Comment.Builder()
				.commentId(commentId)
				.opinionId(commentData.getOpinionId())
				.userId(commentData.getUserId())
				.userName(commentData.getUserName())
				.content(commentData.getContent())
				.userPhotoUrl(commentData.getUserPhotoUrl())
				.userPoliticlaFlagUrl(commentData.getUserPoloticalFlagUrl())
				.build();
	}
}
