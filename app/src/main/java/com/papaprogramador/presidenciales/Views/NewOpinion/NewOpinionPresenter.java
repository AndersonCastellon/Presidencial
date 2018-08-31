package com.papaprogramador.presidenciales.Views.NewOpinion;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.common.pojo.User;
import com.papaprogramador.presidenciales.UseCases.GetUserProfile;
import com.papaprogramador.presidenciales.Utils.FirebaseReference;
import com.papaprogramador.presidenciales.Utils.StaticMethods.CreatePhotoFile;
import com.papaprogramador.presidenciales.Utils.StaticMethods.GetByteImage;
import com.papaprogramador.presidenciales.Utils.StaticMethods.GetPermissions;
import com.papaprogramador.presidenciales.Utils.StaticMethods.TimeStamp;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NewOpinionPresenter extends MvpBasePresenter<NewOpinionContract.View>
		implements NewOpinionContract.Presenter {

	private static final String PATTERN = "dd/MM/yy hh:mm:ss";
	private FirebaseAuth firebaseAuth;
	private Context context;
	private String userId;
	private String userName;
	private String urlPhotoProfile;
	private String urlPoliticalFlag;
	private StorageReference referenceStorageOpinionsImages;
	private DatabaseReference referenceDatabaseOpinions;

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

		referenceStorageOpinionsImages = FirebaseStorage.getInstance().getReference()
				.child(FirebaseReference.OPINIONS_IMAGES);

		referenceDatabaseOpinions = FirebaseDatabase.getInstance()
				.getReference(FirebaseReference.NODE_OPINIONS);
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
	public void loadOpinionWithImage(Bitmap bitmap, final String opinionText) {

		ifViewAttached(new ViewAction<NewOpinionContract.View>() {
			@Override
			public void run(@NonNull NewOpinionContract.View view) {
				view.showProgress(true);
			}
		});

		String fileName = TimeStamp.timeStamp("dd-MM-yyyy_HHmmss");

		StorageReference imageFolder = referenceStorageOpinionsImages.child(userId);
		StorageReference imageName = imageFolder.child(fileName);

		byte[] bytes = new byte[0];

		try {
			bytes = GetByteImage.getImageBytes(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}

		RxFirebaseStorage.putBytes(imageName, bytes)
				.subscribe(new SingleObserver<UploadTask.TaskSnapshot>() {
					@Override
					public void onSubscribe(Disposable d) {

					}

					@Override
					public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

						String downloadUri = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();

						String datePublication = TimeStamp.timeStamp(PATTERN);
						long orderBy = Long.valueOf(TimeStamp.timeStamp("DMyyhhmmss"));

						Opinion opinion = new Opinion(userId, userName, urlPhotoProfile, datePublication, urlPoliticalFlag,
								opinionText, downloadUri, 0, 0, 0, orderBy);

						loadOpinion(opinion);
					}

					@Override
					public void onError(Throwable e) {

						ifViewAttached(new ViewAction<NewOpinionContract.View>() {
							@Override
							public void run(@NonNull NewOpinionContract.View view) {
								view.showProgress(true);
							}
						});

						e.printStackTrace();
					}
				});
	}

	@Override
	public void loadOpinionWithoutImage(String opinionText) {

		ifViewAttached(new ViewAction<NewOpinionContract.View>() {
			@Override
			public void run(@NonNull NewOpinionContract.View view) {
				view.showProgress(true);
			}
		});

		String datePublication = TimeStamp.timeStamp(PATTERN);
		long orderBy = Long.valueOf(TimeStamp.timeStamp("DMyyhhmmss"));


		Opinion opinion = new Opinion(userId, userName, urlPhotoProfile, datePublication, urlPoliticalFlag,
				opinionText, null, 0, 0, 0, orderBy);

		loadOpinion(opinion);
	}

	private void loadOpinion(Opinion opinion) {

		String key = String.valueOf(TimeStamp.timeStamp("DMyyhhmmss"));

		RxFirebaseDatabase.setValue(referenceDatabaseOpinions.child(key), opinion)
				.subscribe(new CompletableObserver() {
					@Override
					public void onSubscribe(Disposable d) {

					}

					@Override
					public void onComplete() {
						ifViewAttached(new ViewAction<NewOpinionContract.View>() {
							@Override
							public void run(@NonNull NewOpinionContract.View view) {
								view.showProgress(false);
								view.newOpinionPublished();
								view.setResultIntent();
							}
						});
					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}
				});
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
