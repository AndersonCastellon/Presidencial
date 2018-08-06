package com.papaprogramador.presidenciales.TreeMvp.NewOpinion;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Obj.User;
import com.papaprogramador.presidenciales.UseCases.GetUserProfile;
import com.papaprogramador.presidenciales.Utils.StaticMethods.CreatePhotoFile;
import com.papaprogramador.presidenciales.Utils.StaticMethods.GetPermissions;
import com.papaprogramador.presidenciales.Utils.StaticMethods.TimeStamp;

import java.io.File;

public class NewOpinionPresenter extends MvpBasePresenter<NewOpinionContract.View>
		implements NewOpinionContract.Presenter {

	private FirebaseAuth firebaseAuth;
	private Context context;
	private String userId;

	private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
		@Override
		public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
			FirebaseUser user = firebaseAuth.getCurrentUser();
			if (user != null) {
				new GetUserProfile(new GetUserProfile.UserProfileListener() {
					@Override
					public void onResult(User user) {

						userId = user.getUserId();

						final String userName = user.getUsername();
						final String urlPhotoProfile = user.getUriPhotoProfile();
						final String urlPoliticalFlag = user.getPoliticalFlag();

						ifViewAttached(new ViewAction<NewOpinionContract.View>() {
							@Override
							public void run(@NonNull NewOpinionContract.View view) {
								view.setToolbar();
								view.setUserProfile(userName, urlPhotoProfile, urlPoliticalFlag);
							}
						});
					}
				});
			}
		}
	};

	NewOpinionPresenter(Context context) {
		this.context = context;
		firebaseAuth = FirebaseAuth.getInstance();
	}

	@Override
	public void selectImageFromGallery(String permission, int requestPermission) {
		if (GetPermissions.checkPermissionToApp(context, permission, requestPermission)) {
			ifViewAttached(new ViewAction<NewOpinionContract.View>() {
				@Override
				public void run(@NonNull NewOpinionContract.View view) {
					view.selectImageFromGallery();
				}
			});
		}
	}

	@Override
	public void selectImageFromCamera(String permission, int requestPermission) {
		if (GetPermissions.checkPermissionToApp(context, permission, requestPermission)) {
			ifViewAttached(new ViewAction<NewOpinionContract.View>() {
				@Override
				public void run(@NonNull NewOpinionContract.View view) {
					view.takePictureIntent();
				}
			});
		}
	}

	@Override
	public void createBitMap() {
		ifViewAttached(new ViewAction<NewOpinionContract.View>() {
			@Override
			public void run(@NonNull NewOpinionContract.View view) {
				view.createBitMap();
			}
		});
	}

	@Override
	public void setImageBitmapSelectedPhoto(final Bitmap bitmap) {
		ifViewAttached(new ViewAction<NewOpinionContract.View>() {
			@Override
			public void run(@NonNull NewOpinionContract.View view) {
				view.showSelectedPhotoView(true);
				view.setImageBitmapSelectedPhoto(bitmap);
			}
		});
	}

	@Override
	public void deleteSelectedImage() {
		ifViewAttached(new ViewAction<NewOpinionContract.View>() {
			@Override
			public void run(@NonNull NewOpinionContract.View view) {
				view.deleteSelectedImage();
				view.showSelectedPhotoView(false);
			}
		});
	}

	@Override
	public File createImageFile() {

		String timeStamp = TimeStamp.timeStamp();
		final File file = CreatePhotoFile.getPhotoFile(context, timeStamp);

		ifViewAttached(new ViewAction<NewOpinionContract.View>() {
			@Override
			public void run(@NonNull NewOpinionContract.View view) {
				view.setCurrentPhotoPath(file.getAbsolutePath());
			}
		});

		return file;
	}

	@Override
	public void setAuthListener() {
		firebaseAuth.addAuthStateListener(authStateListener);
	}

	@Override
	public void removeAuthListener() {
		firebaseAuth.removeAuthStateListener(authStateListener);
	}
}
