package com.papaprogramador.presidenciales.Tareas;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Utilidades.ReferenciasFirebase;

public class ObtenerIdFirebase {

	public interface IdObtenido {
		void idObtenido(boolean bool, String idFirebase);
	}

	private IdObtenido listener;

	public ObtenerIdFirebase(String idDispositivo, IdObtenido listener) {
		this.listener = listener;
		obtenerIdFirebase(idDispositivo);
	}

	private void obtenerIdFirebase(final String idDispositivo) {

		final DatabaseReference referenceIDdispositivo = FirebaseDatabase.getInstance().getReference();

		referenceIDdispositivo.child(ReferenciasFirebase.NODO_ID_DISPOSITIVO)
				.child(idDispositivo).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				validarIDs(dataSnapshot, idDispositivo);
				referenceIDdispositivo.removeEventListener(this);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}

	private void validarIDs(DataSnapshot dataSnapshot, String idDispositivo) {
		final DataSnapshot idFirebase = dataSnapshot;
		final String id = idDispositivo;

		if (idFirebase.getValue() == null){
			listener.idObtenido(false, id);
		}else {
			listener.idObtenido(idFirebase.getKey().equals(id), id);
		}
	}
}
