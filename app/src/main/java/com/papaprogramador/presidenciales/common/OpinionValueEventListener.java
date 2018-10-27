package com.papaprogramador.presidenciales.common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.common.pojo.Opinion;

import java.util.ArrayList;
import java.util.List;

public class OpinionValueEventListener {

	private static final String PATH_LIKES = "Likes";

	private DatabaseReference query;
	private DataSnapshot opinionSnapshot;

	public OpinionValueEventListener(final Query reference) {
		this.query = (DatabaseReference) reference;

		reference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				opinionSnapshot = dataSnapshot;
				reference.removeEventListener(this);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	public List<Opinion> getOpinions() {

		final List<Opinion> opinions = new ArrayList<>();

		for (final DataSnapshot dataOpinion : opinionSnapshot.getChildren()) {

			final DatabaseReference likeRef = query.child(dataOpinion.getKey())
					.child(PATH_LIKES);

			likeRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot likeSnapshot) {
					likeRef.removeEventListener(this);
					List<String> userLikes = new ArrayList<>();
					for (DataSnapshot dataLike : likeSnapshot.getChildren()) {
						userLikes.add(dataLike.getKey());
					}

					Opinion opinion = dataOpinion.getValue(Opinion.class);
					if (opinion != null) {
						opinion.setOpinionId(dataOpinion.getKey());
						opinion.setUserLikes(userLikes);
					}

					opinions.add(opinion);
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});
		}

		return opinions;
	}
}
