package com.papaprogramador.presidenciales2019.io.Utils;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales2019.R;
import com.papaprogramador.presidenciales2019.model.Usuario;
import com.papaprogramador.presidenciales2019.ui.NewAccountActivity;

public class Metodos {

	public Context context;

	public void RegistrarUsuario(final String firebaseUID, String Username, String Useremail, String Departamento, final String IDdispositivo) {

		final Usuario usuario = new Usuario(Username, Useremail, Departamento, IDdispositivo, Constantes.VOTO_POR);

		final DatabaseReference databaseReference;
		databaseReference = FirebaseDatabase.getInstance().getReference();


		databaseReference.child(ReferenciasFirebase.NODO_ID_DISPOSITIVO).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot snapshot :dataSnapshot.getChildren()
						) {
					if (IDdispositivo == snapshot.getValue()){

						Toast.makeText(context, R.string.DispositivoYaUtilizado,
								Toast.LENGTH_LONG).show();
						break;
					}else {
						databaseReference.child(ReferenciasFirebase.NODO_ID_DISPOSITIVO).setValue(IDdispositivo);
					}
					
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});

		databaseReference.child(ReferenciasFirebase.NODO_UID).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot snapshot :
						dataSnapshot.getChildren()) {
					if (firebaseUID == snapshot.getValue()){
						break;
					}else {
						databaseReference.child(ReferenciasFirebase.NODO_USUARIO).child(firebaseUID).setValue(usuario);
						databaseReference.child(ReferenciasFirebase.NODO_UID).setValue(firebaseUID);
					}
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});


	}
}
