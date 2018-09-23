package com.papaprogramador.presidenciales.common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class FirebaseRxDatabase {

	public static Observable<DataSnapshot> observeValueEvent(final DatabaseReference query, BackpressureStrategy strategy) {
		return Observable.create(new ObservableOnSubscribe<DataSnapshot>() {
			@Override
			public void subscribe(final ObservableEmitter<DataSnapshot> emitter) throws Exception {
				query.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						query.removeEventListener(this);
						emitter.onNext(dataSnapshot);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
						query.removeEventListener(this);
						emitter.onError(databaseError.toException());
					}
				});
			}
		});
	}
}
