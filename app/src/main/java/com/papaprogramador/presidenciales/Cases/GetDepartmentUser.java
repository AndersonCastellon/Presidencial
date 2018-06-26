package com.papaprogramador.presidenciales.Cases;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;

public class GetDepartmentUser {

	public interface DepartmentUserListener{
		void onResult(DataSnapshot department);
		void onError(String error);
	}

	private String uidUser;
	private DepartmentUserListener listener;

	public GetDepartmentUser(String uidUser, DepartmentUserListener listener) {
		this.uidUser = uidUser;
		this.listener = listener;

		getDepartmentUser();
	}

	private void getDepartmentUser() {

		final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

		databaseReference.child(FirebaseReference.NODO_USUARIO).child(uidUser)
				.child(FirebaseReference.NODO_DEPARTAMENTO)
				.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				listener.onResult(dataSnapshot);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				listener.onError(databaseError.getMessage());
			}
		});
	}
}
