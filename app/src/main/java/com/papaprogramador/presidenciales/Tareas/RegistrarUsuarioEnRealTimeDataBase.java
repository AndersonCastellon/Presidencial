package com.papaprogramador.presidenciales.Tareas;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.papaprogramador.presidenciales.Objetos.Usuario;
import com.papaprogramador.presidenciales.Utilidades.ReferenciasFirebase;

public class RegistrarUsuarioEnRealTimeDataBase {

	public interface RegistrarUsuarioFirebase{
		void registroExitoso(boolean bool);
	}

	private RegistrarUsuarioFirebase listener;

	public RegistrarUsuarioEnRealTimeDataBase(String uidFirebase, String nombreUsuario,
	                                          String emailUsuario, String departamento,
	                                          String idDispositivo, String voto, RegistrarUsuarioFirebase listener) {
		this.listener = listener;
		registrarUsuarioEnFirebaseRealTimeDataBase(nombreUsuario, emailUsuario, departamento,
				voto, uidFirebase, idDispositivo);
	}

	private void registrarUsuarioEnFirebaseRealTimeDataBase(String nombreUsuario, String emailUsuario,
	                                                        String departamento, String voto, String uidFirebase,
	                                                        String idDispositivo) {

		Usuario usuario = new Usuario(nombreUsuario, emailUsuario, departamento, idDispositivo, voto);

		DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

		databaseReference.child(ReferenciasFirebase.NODO_USUARIO)
				.child(uidFirebase).setValue(usuario);

		databaseReference.child(ReferenciasFirebase.NODO_ID_DISPOSITIVO)
				.child(idDispositivo).setValue(emailUsuario);
	}
}
