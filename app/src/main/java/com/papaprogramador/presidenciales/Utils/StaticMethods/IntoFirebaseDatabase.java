package com.papaprogramador.presidenciales.Utils.StaticMethods;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;

public class IntoFirebaseDatabase {

	private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

	public static void applyNewVoteCandidate(int currentVotes, String idCandidate) {
		int votesApply = ++currentVotes;

		databaseReference.child(FirebaseReference.NODO_CANDIDATOS).child(idCandidate)
				.child(FirebaseReference.NODO_VOTOS_CANDIDATO)
				.setValue(votesApply);
	}

	public static void deleteVoteCurrentUser(int currentVotes, String idCandidate) {
		int votesApply = --currentVotes;

		databaseReference.child(FirebaseReference.NODO_CANDIDATOS).child(idCandidate)
				.child(FirebaseReference.NODO_VOTOS_CANDIDATO)
				.setValue(votesApply);
	}

	public static void applyNewVoteUser(String idCandidate,String urlPoliticalFlag, String uidUser) {

		databaseReference.child(FirebaseReference.NODO_USUARIO).child(uidUser)
				.child(FirebaseReference.NODO_VOTOPOR).setValue(idCandidate);

		databaseReference.child(FirebaseReference.NODO_USUARIO).child(uidUser)
				.child(FirebaseReference.POLITICAL_FLAG).setValue(urlPoliticalFlag);
	}

	public static boolean setDepartmentUser(String uidUser, String department) {

		databaseReference.child(FirebaseReference.NODO_USUARIO).child(uidUser)
				.child(FirebaseReference.NODO_DEPARTAMENTO).setValue(department);

		return true;
	}

	public static void deleteUserInfo(String idUser, String idDevice){

		databaseReference.child(FirebaseReference.NODO_USUARIO).child(idUser).removeValue();
		databaseReference.child(FirebaseReference.NODO_ID_DISPOSITIVO).child(idDevice).removeValue();

	}

}
