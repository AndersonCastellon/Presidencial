package com.papaprogramador.presidenciales.TreeMvp.Opinions;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.UseCases.GetOpinionsList;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;
import com.papaprogramador.presidenciales.Utils.StaticMethods.CheckConnection;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.BackpressureStrategy;
import io.reactivex.FlowableSubscriber;

import static io.reactivex.BackpressureStrategy.BUFFER;
import static io.reactivex.BackpressureStrategy.DROP;

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
	public void getOpinionsList(final boolean pullToRefresh) {

		if (!CheckConnection.checkConnection(context)) {
			ifViewAttached(new ViewAction<OpinionsContract.View>() {
				@Override
				public void run(@NonNull OpinionsContract.View view) {
					view.showError(null, pullToRefresh);
				}
			});
		} else {

			Query query = firebaseReference;

			RxFirebaseDatabase.observeChildEvent(query, DROP)
					.subscribe(new FlowableSubscriber<RxFirebaseChildEvent<DataSnapshot>>() {
						@Override
						public void onSubscribe(Subscription s) {

						}

						@Override
						public void onNext(RxFirebaseChildEvent<DataSnapshot> dataSnapshotRxFirebaseChildEvent) {
							switch (dataSnapshotRxFirebaseChildEvent.getEventType()) {
								case ADDED:
									getListItem(dataSnapshotRxFirebaseChildEvent.getValue());
									break;
								case MOVED:
									break;
								case CHANGED:
									break;
								case REMOVED:
									break;
								default:
									getListItem(dataSnapshotRxFirebaseChildEvent.getValue());
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
	}

	private void getListItem(DataSnapshot value) {

		Opinions opinions = value.getValue(Opinions.class);
		Objects.requireNonNull(opinions)
				.setOpinionId(value.getKey());

		if (!opinionsList.contains(opinions)) {
			opinionsList.add(opinions);
		}

		Collections.sort(opinionsList);

		ifViewAttached(new ViewAction<OpinionsContract.View>() {
			@Override
			public void run(@NonNull OpinionsContract.View view) {
				view.setData(opinionsList);
				view.showContent();
			}
		});
	}
}
