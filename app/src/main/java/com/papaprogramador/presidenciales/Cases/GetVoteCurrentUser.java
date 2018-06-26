package com.papaprogramador.presidenciales.Cases;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;

public class GetVoteCurrentUser {

	public interface VoteCurrentUserListener{
		void onResult(DataSnapshot vote);
	}

	private String uidUser;
	private VoteCurrentUserListener listener;

	public GetVoteCurrentUser(String uidUser, VoteCurrentUserListener listener) {
		this.uidUser = uidUser;
		this.listener = listener;
		getVoteCurrentUser();
	}

	private void getVoteCurrentUser() {

		DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();

		firebaseReference.child(FirebaseReference.NODO_USUARIO).child(uidUser)
				.child(FirebaseReference.NODO_VOTOPOR)
				.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						listener.onResult(dataSnapshot);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
	}
}
