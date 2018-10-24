package com.papaprogramador.presidenciales.common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.common.pojo.Opinion;

import java.util.ArrayList;
import java.util.List;

public abstract class OpinionValueEventListener implements ValueEventListener {

	private List<Opinion> opinions;

	protected OpinionValueEventListener(Query reference) {
		opinions = new ArrayList<>();
		reference.addValueEventListener(this);
	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {

		try {
			for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
				Opinion data = snapshot.getValue(Opinion.class);
				if (data != null){
					data.setOpinionId(snapshot.getKey());
				}
				opinions.add(data);
			}
			onDataChange(opinions);
		} catch (DatabaseException ignored) {
		}
	}

	@Override
	public void onCancelled(DatabaseError databaseError) {

	}

	protected abstract void onDataChange(List<Opinion> opinions);
}
