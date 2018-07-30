package com.papaprogramador.presidenciales.UseCases;

import com.papaprogramador.presidenciales.Obj.User;

public class GetUserProfile {

	interface UserProfileListener {
		void onResult(User user);
	}

	private UserProfileListener listener;
	private User user = null;

	public GetUserProfile() {
		getUserProfile();
	}

	private void getUserProfile() {

	}

}
