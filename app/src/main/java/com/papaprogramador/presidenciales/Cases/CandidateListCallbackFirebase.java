package com.papaprogramador.presidenciales.Cases;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Obj.Candidate;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;

import java.util.ArrayList;
import java.util.List;

public class CandidateListCallbackFirebase {

	public interface ListCandidateListener {
		void onSuccess(List<Candidate> candidateList);

		void onError(Exception e);
	}

	private ListCandidateListener listener;
	private List<Candidate> candidateList;

	public CandidateListCallbackFirebase(ListCandidateListener listener) {
		this.listener = listener;
		candidateList = new ArrayList<>();
		getListCandidateFirebase();
	}

	private void getListCandidateFirebase() {

		FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

		firebaseDatabase.getReference().child(FirebaseReference.NODO_CANDIDATOS)
				.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						onDataSnapshotCandidateResult(dataSnapshot);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
	}

	private void onDataSnapshotCandidateResult(DataSnapshot dataSnapshot) {

		candidateList.removeAll(candidateList);

		if (dataSnapshot.getChildren() == null) {
			listener.onError(new Exception("DataSnapshotListCandidateFirebaseNullException"));
		} else {
			for (DataSnapshot listCandidate :
					dataSnapshot.getChildren()) {
				Candidate candidate = listCandidate.getValue(Candidate.class);
				candidateList.add(candidate);
			}

			listener.onSuccess(candidateList);
		}
	}

}
