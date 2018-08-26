package com.papaprogramador.presidenciales.TreeMvp.Opinions;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;
import com.papaprogramador.presidenciales.Utils.StaticMethods.CheckConnection;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.BackpressureStrategy.BUFFER;
import static io.reactivex.BackpressureStrategy.LATEST;

public class OpinionsPresenter extends MvpBasePresenter<OpinionsContract.View> implements OpinionsContract.Presenter {

	private Context context;
	DatabaseReference firebaseReference;
	private List<Opinions> opinionsList;

	OpinionsPresenter(Context context) {
		this.context = context;
		this.opinionsList = new ArrayList<>();

		firebaseReference = FirebaseDatabase.getInstance()
				.getReference(FirebaseReference.NODE_OPINIONS);
	}


	@Override
	public void getOpinions(long opinionId, final boolean pullToRefresh) {

		if (!CheckConnection.checkConnection(context)) {
			ifViewAttached(new ViewAction<OpinionsContract.View>() {
				@Override
				public void run(@NonNull OpinionsContract.View view) {
					view.showError(null, pullToRefresh);
				}
			});
		} else {
			getDataOpinions(opinionId, pullToRefresh);
		}
	}

	@Override
	public void getDataOpinions(long opinionId, final boolean pullToRefresh) {

		Query query;

		if (opinionId == 0) {
			query = firebaseReference
					.orderByChild("orderBy")
					.limitToLast(Constans.OPINIONS_PER_PAGE);
		} else {
			query = firebaseReference
					.orderByChild("orderBy")
					.endAt(opinionId)
					.limitToLast(Constans.OPINIONS_PER_PAGE);
		}


		RxFirebaseDatabase.observeChildEvent(query, BUFFER)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new FlowableSubscriber<RxFirebaseChildEvent<DataSnapshot>>() {
					@Override
					public void onSubscribe(Subscription s) {
						s.request(Long.MAX_VALUE);
						ifViewAttached(new ViewAction<OpinionsContract.View>() {
							@Override
							public void run(@NonNull OpinionsContract.View view) {
								view.showContent();
								view.setRefreshing();
							}
						});
					}

					@Override
					public void onNext(RxFirebaseChildEvent<DataSnapshot> dataSnapshotRxFirebaseChildEvent) {
						switch (dataSnapshotRxFirebaseChildEvent.getEventType()) {
							case ADDED:
								getListOpinions(dataSnapshotRxFirebaseChildEvent.getValue());
								break;
							case MOVED:
								break;
							case CHANGED:
								break;
							case REMOVED:
								break;
						}
					}

					@Override
					public void onError(final Throwable t) {
						ifViewAttached(new ViewAction<OpinionsContract.View>() {
							@Override
							public void run(@NonNull OpinionsContract.View view) {
								view.showError(t, pullToRefresh);
							}
						});
					}

					@Override
					public void onComplete() {
						ifViewAttached(new ViewAction<OpinionsContract.View>() {
							@Override
							public void run(@NonNull OpinionsContract.View view) {
								view.showContent();
							}
						});
					}
				});
	}

	@Override
	public void getListOpinions(DataSnapshot dataSnapshot) {

		Opinions opinions = dataSnapshot.getValue(Opinions.class);
		Objects.requireNonNull(opinions)
				.setOpinionId(dataSnapshot.getKey());

		if (!opinionsList.contains(opinions)) {
			opinionsList.clear();
			opinionsList.add(opinions);

			ifViewAttached(new ViewAction<OpinionsContract.View>() {
				@Override
				public void run(@NonNull OpinionsContract.View view) {
					view.setData(opinionsList);
					view.showContent();
				}
			});
		} else {
			ifViewAttached(new ViewAction<OpinionsContract.View>() {
				@Override
				public void run(@NonNull OpinionsContract.View view) {
					view.showContent();
				}
			});
		}

	}
}
