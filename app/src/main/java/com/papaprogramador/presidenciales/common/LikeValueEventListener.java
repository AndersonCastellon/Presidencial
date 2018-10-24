package com.papaprogramador.presidenciales.common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.common.pojo.Opinion;


public abstract class LikeValueEventListener implements ValueEventListener {

	private Opinion opinion;

	protected LikeValueEventListener(DatabaseReference reference, Opinion opinion) {
		reference.addValueEventListener(this);
		this.opinion = opinion;
	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		try {
			for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
				opinion.addUserLikeId(snapshot.getKey());
			}
			onDataChange(opinion);
		} catch (DatabaseException ignored) {
		}
	}

	@Override
	public void onCancelled(DatabaseError databaseError) {

	}

	protected abstract void onDataChange(Opinion opinion);
}
