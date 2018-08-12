package com.papaprogramador.presidenciales.UseCases;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class GetOpinionsList {

	public interface OpinionListListener{
		void onResult(List<Opinions> opinionsList);
		void onError(DatabaseError error);
	}

	private OpinionListListener listListener;
	private List<Opinions> opinionsList;

	public GetOpinionsList(OpinionListListener listListener) {
		this.listListener = listListener;
		this.opinionsList = new ArrayList<>();
	}

	public void getOpinionsList(){

		DatabaseReference firebaseReference = FirebaseDatabase.getInstance()
				.getReference(FirebaseReference.NODE_OPINIONS);

		firebaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				createOpinionsList(dataSnapshot);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				listListener.onError(databaseError);
			}
		});
	}

	private void createOpinionsList(DataSnapshot dataSnapshot) {

//		opinionsList.removeAll(opinionsList);

		for (DataSnapshot data :
				dataSnapshot.getChildren()) {
			Opinions opinions = data.getValue(Opinions.class);
			Objects.requireNonNull(opinions).setOpinionId(data.getKey());

			if (!opinionsList.contains(opinions)) {
				opinionsList.add(opinions);
			}
		}

		Collections.sort(opinionsList);
		listListener.onResult(opinionsList);
	}
}
