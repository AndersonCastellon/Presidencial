package com.papaprogramador.presidenciales.common;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public abstract class AdditionEventListener<T> implements ChildEventListener {
	private Class<T> dataClass;
	private boolean initialDataLoaded = false;

	public AdditionEventListener(final DatabaseReference dataRef, Class<T> dataClass) {
		this.dataClass = dataClass;
		dataRef.addChildEventListener(this);
		dataRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				dataRef.removeEventListener(this);
				initialDataLoaded = true;
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	@Override
	public void onChildAdded(DataSnapshot dataSnapshot, String s) {
		if (initialDataLoaded) {
			try {
				T data = dataSnapshot.getValue(dataClass);
				onChildAdded(dataSnapshot.getKey(), data);
			} catch (DatabaseException ignored) {
			}
		}

	}

	@Override
	public void onChildChanged(DataSnapshot dataSnapshot, String s) {

	}

	@Override
	public void onChildRemoved(DataSnapshot dataSnapshot) {

	}

	@Override
	public void onChildMoved(DataSnapshot dataSnapshot, String s) {

	}

	@Override
	public void onCancelled(DatabaseError databaseError) {

	}

	protected abstract void onChildAdded(String commentId, T commentData);
}
