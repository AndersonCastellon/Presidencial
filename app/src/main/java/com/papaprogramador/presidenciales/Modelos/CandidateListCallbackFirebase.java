package com.papaprogramador.presidenciales.Modelos;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Objetos.Candidato;
import com.papaprogramador.presidenciales.Utilidades.ReferenciasFirebase;

import java.util.ArrayList;
import java.util.List;

public class CandidateListCallbackFirebase {

	public interface ListCandidateListener {
		void onSuccess(List<Candidato> candidatoList);

		void onError(Exception e);
	}

	private ListCandidateListener listener;
	private List<Candidato> candidatoList;

	public CandidateListCallbackFirebase(ListCandidateListener listener) {
		this.listener = listener;
		candidatoList = new ArrayList<>();
		getListCandidateFirebase();
	}

	private void getListCandidateFirebase() {

		FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

		firebaseDatabase.getReference().child(ReferenciasFirebase.NODO_CANDIDATOS)
				.addValueEventListener(new ValueEventListener() {
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

		candidatoList.removeAll(candidatoList);

		if (dataSnapshot.getChildren() == null) {
			listener.onError(new Exception("DataSnapshotListCandidateFirebaseNullException"));
		} else {
			for (DataSnapshot listCandidate :
					dataSnapshot.getChildren()) {
				Candidato candidato = listCandidate.getValue(Candidato.class);
				candidatoList.add(candidato);
			}

			listener.onSuccess(candidatoList);
		}
	}

}
