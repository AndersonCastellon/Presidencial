package com.papaprogramador.presidenciales2019.ui.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales2019.R;
import com.papaprogramador.presidenciales2019.io.Utils.ReferenciasFirebase;
import com.papaprogramador.presidenciales2019.model.Candidato;
import com.papaprogramador.presidenciales2019.ui.adapter.CandidatoAdapter;

import java.util.ArrayList;
import java.util.List;

public class CandidatosFragment extends Fragment {

	RecyclerView recyclerView;
	List<Candidato> candidatoList; //Lista de objetos Candidato
	CandidatoAdapter candidatoAdapter; //Adaptador para pasar los datos al recyclerview


	public CandidatosFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_candidatos, container, false);

		FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//Instancia database


		//Implementación del Recyclerview
		recyclerView = view.findViewById(R.id.rv);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//Creacion del layoutmanager
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//Orientacion del layoutmanager
		recyclerView.setLayoutManager(linearLayoutManager);//asignacion de layoutmanager al recyclerview

		//Creacion de la lista de objetos
		candidatoList = new ArrayList<>();
		//Creacion del adaptador con la lista de objetos
		candidatoAdapter = new CandidatoAdapter(candidatoList);
		//Asignacion del adaptador al recyclerview
		recyclerView.setAdapter(candidatoAdapter);

		//Obtencion de los datos desde la base de datos firebase
		//Referencia al nodo de los Candidatos
		firebaseDatabase.getReference().child(ReferenciasFirebase.NODO_CANDIDATOS).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {//Método donde se reciben los datos
				candidatoList.removeAll(candidatoList);//Limpieza total de la lista
				//Foreach para recorrer la lista de datos y obtener sus valores
				for (DataSnapshot snapshot ://Variable que se utilizará
						dataSnapshot.getChildren())/*Entrar a cada nodo hijo de la snapshot*/ {
					Candidato candidato = snapshot.getValue(Candidato.class);//Asignar los valores al POJO Candidato
					candidatoList.add(candidato);//Agregar la lista de objetos a la lista
					candidatoAdapter.notifyDataSetChanged();//Notificar al adaptador de los cambios en los datos

				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {//Método en caso de que ocurra un error en la obtención de la información

			}
		});
		return view;
	}}
