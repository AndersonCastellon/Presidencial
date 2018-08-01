package com.papaprogramador.presidenciales.UseCases;

import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Obj.User;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;

public class RegistrarUsuarioRTDB {

	public interface RegistrarUsuarioFirebase {
		void registroExitoso(boolean bool);
	}

	private RegistrarUsuarioFirebase listener;
	private String idDispositivo;
	private User user;
	private String userId;

	private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


	public RegistrarUsuarioRTDB(String userId, String idDispositivo, User user, RegistrarUsuarioFirebase listener) {
		this.listener = listener;
		this.userId = userId;
		this.idDispositivo = idDispositivo;
		this.user = user;

		buscarUidFirebase();
	}

	private void buscarUidFirebase() {
		databaseReference.child(FirebaseReference.NODO_USUARIO).child(userId)
				.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						onDataSnapshot(dataSnapshot);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
	}

	private void onDataSnapshot(DataSnapshot dataSnapshot) {
		if (dataSnapshot.getValue() == null) {
			registrarUsuarioCompleto();
		} else {
			registrarSoloNombreUsuario();
		}
	}

	private void registrarUsuarioCompleto() {
		String emailUser = user.getEmail();

		databaseReference.child(FirebaseReference.NODO_USUARIO)
				.child(userId).setValue(user);

		databaseReference.child(FirebaseReference.NODO_ID_DISPOSITIVO)
				.child(idDispositivo).setValue(emailUser);

		listener.registroExitoso(true);

	}

	private void registrarSoloNombreUsuario() {
		String emailUser = user.getEmail();

		databaseReference.child(FirebaseReference.NODO_USUARIO)
				.child(userId).child(FirebaseReference.NODO_NOMBRE_USUARIO)
				.setValue(emailUser);

		listener.registroExitoso(true);
	}
}
