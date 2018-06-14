package com.papaprogramador.presidenciales.Modelos;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.Objetos.Usuario;
import com.papaprogramador.presidenciales.Utils.ReferenciasFirebase;

public class RegistrarUsuarioRTDB {

	public interface RegistrarUsuarioFirebase {
		void registroExitoso(boolean bool);
	}

	private RegistrarUsuarioFirebase listener;
	private String uidFirebase;
	private String nombreUsuario;
	private String emailUsuario;
	private String departamento;
	private String idDispositivo;
	private String voto;

	private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


	public RegistrarUsuarioRTDB(String uidFirebase, String nombreUsuario,
	                            String emailUsuario, String departamento,
	                            String idDispositivo, String voto, RegistrarUsuarioFirebase listener) {
		this.listener = listener;
		this.uidFirebase = uidFirebase;
		this.nombreUsuario = nombreUsuario;
		this.emailUsuario = emailUsuario;
		this.departamento = departamento;
		this.idDispositivo = idDispositivo;
		this.voto = voto;

		buscarUidFirebase(uidFirebase);
	}

	private void buscarUidFirebase(String uidFirebase) {

		databaseReference.child(ReferenciasFirebase.NODO_USUARIO).child(uidFirebase)
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

		Usuario usuario = new Usuario(nombreUsuario, emailUsuario, departamento, idDispositivo, voto);

		databaseReference.child(ReferenciasFirebase.NODO_USUARIO)
				.child(uidFirebase).setValue(usuario);

		databaseReference.child(ReferenciasFirebase.NODO_ID_DISPOSITIVO)
				.child(idDispositivo).setValue(emailUsuario);

		listener.registroExitoso(true);

	}

	private void registrarSoloNombreUsuario() {
		databaseReference.child(ReferenciasFirebase.NODO_USUARIO)
				.child(uidFirebase).child(ReferenciasFirebase.NODO_NOMBRE_USUARIO)
				.setValue(nombreUsuario);

		listener.registroExitoso(true);
	}
}
