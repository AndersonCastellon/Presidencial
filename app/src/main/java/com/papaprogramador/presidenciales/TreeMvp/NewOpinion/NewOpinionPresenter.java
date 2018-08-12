package com.papaprogramador.presidenciales.TreeMvp.NewOpinion;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.Obj.User;
import com.papaprogramador.presidenciales.UseCases.GetUserProfile;
import com.papaprogramador.presidenciales.UseCases.LoadImageOpinion;
import com.papaprogramador.presidenciales.UseCases.PublicNewOpinion;
import com.papaprogramador.presidenciales.Utils.StaticMethods.CreatePhotoFile;
import com.papaprogramador.presidenciales.Utils.StaticMethods.GetPermissions;
import com.papaprogramador.presidenciales.Utils.StaticMethods.TimeStamp;

import java.io.File;

public class NewOpinionPresenter extends MvpBasePresenter<NewOpinionContract.View>
		implements NewOpinionContract.Presenter {

	private static final String PATTERN = "dd/MM/yy hh:mm:ss";
	private FirebaseAuth firebaseAuth;
	private Context context;
	private String userId;
	private String userName;
	private String urlPhotoProfile;
	private String urlPoliticalFlag;

	private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
		@Override
		public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
			FirebaseUser user = firebaseAuth.getCurrentUser();
			if (user != null) {
				new GetUserProfile(new GetUserProfile.UserProfileListener() {
					@Override
					public void onResult(User user) {

						userId = user.getUserId();

						userName = user.getUsername();
						urlPhotoProfile = user.getUriPhotoProfile();
						urlPoliticalFlag = user.getPoliticalFlag();

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

		String timeStamp = TimeStamp.timeStamp("ddmmyyyy_HHmmss");
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
	public void loadOpinionWithImage(Uri photoSelectedUri, final String opinionText) {

		LoadImageOpinion loadImageOpinion = new LoadImageOpinion(photoSelectedUri, new LoadImageOpinion.UploadImageResult() {
			@Override
			public void onResult(String downloadUri) {

				String datePublication = TimeStamp.timeStamp(PATTERN);
				long orderBy = Long.valueOf(TimeStamp.timeStamp("DMyyhhmmss"));

				Opinions opinion = new Opinions(userId, userName, urlPhotoProfile, datePublication, urlPoliticalFlag,
						opinionText, downloadUri, 0,0,0, orderBy);

				loadOpinion(opinion);
			}

			@Override
			public void onProgress(final double progress) {
				ifViewAttached(new ViewAction<NewOpinionContract.View>() {
					@Override
					public void run(@NonNull NewOpinionContract.View view) {
						view.opinionPublishedProgress(progress);
					}
				});
			}

			@Override
			public void onFailure(Exception e) {

			}
		});

		loadImageOpinion.loadImageOpinion();
	}

	@Override
	public void loadOpinionWithoutImage(String opinionText) {
		String datePublication = TimeStamp.timeStamp(PATTERN);
		long orderBy = Long.valueOf(TimeStamp.timeStamp("DMyyhhmmss"));


		Opinions opinion = new Opinions(userId, userName, urlPhotoProfile, datePublication, urlPoliticalFlag,
				opinionText, null, 0,0,0, orderBy);

		loadOpinion(opinion);
	}

	private void loadOpinion(Opinions opinion) {

		PublicNewOpinion publicNewOpinion = new PublicNewOpinion(opinion, new PublicNewOpinion.PublicOpinionListener() {
			@Override
			public void onResult(boolean result) {
				if (result){
					ifViewAttached(new ViewAction<NewOpinionContract.View>() {
						@Override
						public void run(@NonNull NewOpinionContract.View view) {
							view.newOpinionPublished();
						}
					});
				}
			}

			@Override
			public void onFailure(Exception e) {

			}
		});

		publicNewOpinion.publicNewOpinion();
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
