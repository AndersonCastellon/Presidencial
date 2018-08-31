package com.papaprogramador.presidenciales.UseCases;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.common.pojo.User;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;
import com.papaprogramador.presidenciales.Utils.StaticMethods.GetFirebaseUser;

public class GetUserProfile {

	public interface UserProfileListener {
		void onResult(User user);
	}

	private UserProfileListener listener;
	private User user;
	private String userId;

	public GetUserProfile(UserProfileListener listener) {
		this.listener = listener;
		FirebaseUser firebaseUser = GetFirebaseUser.getFirebaseUser();
		this.userId = firebaseUser.getUid();
		getUserProfile();
	}

	private void getUserProfile() {

		 DatabaseReference databaseReference = FirebaseDatabase.getInstance()
				 .getReference(FirebaseReference.NODO_USUARIO);

		 databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
			 @Override
			 public void onDataChange(DataSnapshot dataSnapshot) {
				 user = dataSnapshot.getValue(User.class);
				 user.setUserId(dataSnapshot.getKey());
				 listener.onResult(user);
			 }

			 @Override
			 public void onCancelled(DatabaseError databaseError) {

			 }
		 });
	}
}
