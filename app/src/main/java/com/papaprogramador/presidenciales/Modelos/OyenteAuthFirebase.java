package com.papaprogramador.presidenciales.Modelos;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OyenteAuthFirebase {

	public interface UsuarioActual {
		void usuario(FirebaseUser user);
	}

	private UsuarioActual listener;

	public OyenteAuthFirebase() {
		this.listener = listener;
		obtenerUsuarioFirebaseActual();
	}

	private void obtenerUsuarioFirebaseActual() {
		new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				listener.usuario(user);

			}
		};
	}
}
