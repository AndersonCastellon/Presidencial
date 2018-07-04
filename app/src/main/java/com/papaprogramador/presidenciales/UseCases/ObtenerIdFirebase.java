package com.papaprogramador.presidenciales.UseCases;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;

public class ObtenerIdFirebase {

	public interface IdObtenido {
		void idObtenido(final boolean bool, String idFirebase);
	}

	private IdObtenido listener;
	private String idDispositivo;
	private String emailUsuario;

	public ObtenerIdFirebase(String idDispositivo, IdObtenido listener) {
		this.listener = listener;
		this.idDispositivo = idDispositivo;
		obtenerIdFirebase();
	}

	public ObtenerIdFirebase(String idDispositivo, String emailUsuario, IdObtenido listener) {
		this.listener = listener;
		this.idDispositivo = idDispositivo;
		this.emailUsuario = emailUsuario;
		obtenerIdFirebase();
	}

	private void obtenerIdFirebase() {

		final DatabaseReference referenceIDdispositivo = FirebaseDatabase.getInstance().getReference();

		referenceIDdispositivo.child(FirebaseReference.NODO_ID_DISPOSITIVO)
				.child(idDispositivo).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				validarIDs(dataSnapshot, idDispositivo, emailUsuario);
				referenceIDdispositivo.removeEventListener(this);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}

	private void validarIDs(DataSnapshot dataSnapshot, String idDispositivo, String emailUsuario) {

		final DataSnapshot idFirebase = dataSnapshot;
		final String id = idDispositivo;
		final String email = emailUsuario;

		if (email == null) {
			validarSinEmail(idFirebase, id);
		}  else {
			validarConEmail(email, id, idFirebase);
		}

	}

	private void validarConEmail(String email, String id, DataSnapshot idFirebase) {

		if (idFirebase.getValue() == null) {
			listener.idObtenido(true, "");
		} else {
			listener.idObtenido(idFirebase.getValue().toString().equals(email), id);
		}
	}

	private void validarSinEmail(DataSnapshot idFirebase, String id) {

		if (idFirebase.getValue() == null) {
			listener.idObtenido(false, id);
		} else {
			listener.idObtenido(idFirebase.getKey().equals(id), id);
		}
	}
}
