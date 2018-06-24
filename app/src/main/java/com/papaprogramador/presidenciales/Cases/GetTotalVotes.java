package com.papaprogramador.presidenciales.Cases;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;

public class GetTotalVotes {

	public interface VotesListener{
		void onResult(int votes);
	}

	private String idCandidate;
	private VotesListener listener;

	public GetTotalVotes(String idCandidate, VotesListener listener) {
		this.idCandidate = idCandidate;
		this.listener = listener;
		getTotalVotes();
	}

	private void getTotalVotes() {

		final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

		databaseReference.child(idCandidate).child(FirebaseReference.NODO_VOTOS)
				.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						int vote = Integer.parseInt(dataSnapshot.getValue().toString());
						listener.onResult(vote);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
	}
}
