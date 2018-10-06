package com.papaprogramador.presidenciales.common.dataAccess;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.papaprogramador.presidenciales.common.pojo.User;

import java.util.Objects;

public class FirebaseUserAPI {

	private FirebaseUser firebaseUser;
	private DatabaseReference reference;
	private User user;
	private static FirebaseUserAPI INSTANCE = null;
	private static final String PATH_USERS = "Usuarios";

	private FirebaseUserAPI() {
		reference = FirebaseDatabase.getInstance().getReference(PATH_USERS);
		firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

		reference.child(Objects.requireNonNull(firebaseUser).getUid())
				.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						user = dataSnapshot.getValue(User.class);
						if (user != null) {
							user.setUserId(dataSnapshot.getKey());
						}
						reference.removeEventListener(this);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
						reference.removeEventListener(this);
					}
				});
	}

	public static FirebaseUserAPI getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FirebaseUserAPI();
		}
		return INSTANCE;
	}

	public String getUserId() {
		return user.getUserId();
	}

	public String getUserName() {
		return user.getUsername();
	}

	public String getEmail() {
		return user.getEmail();
	}

	public String getDepartment() {
		return user.getDepartamento();
	}

	public String getVote() {
		return user.getVotopor();
	}

	public String getPhotoProfile() {
		return user.getUriPhotoProfile();
	}

	public String getPoliticalFlag() {
		return user.getPoliticalFlag();
	}
}
