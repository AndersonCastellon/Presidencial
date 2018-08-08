package com.papaprogramador.presidenciales.UseCases;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;

public class PublicNewOpinion {

	public interface PublicOpinionListener {
		void onResult(boolean result);

		void onFailure(Exception e);
	}

	private Opinions opinion;
	private PublicOpinionListener listener;
	private DatabaseReference databaseReference;

	public PublicNewOpinion(Opinions opinion, PublicOpinionListener listener) {
		this.opinion = opinion;
		this.listener = listener;
		this.databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseReference.NODE_OPINIONS);
	}

	public void publicNewOpinion() {

		databaseReference.push().setValue(opinion).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				listener.onResult(true);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				listener.onFailure(e);
			}
		});
	}
}
