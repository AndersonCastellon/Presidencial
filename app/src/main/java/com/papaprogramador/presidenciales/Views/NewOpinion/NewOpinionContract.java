package com.papaprogramador.presidenciales.Views.NewOpinion;

import android.graphics.Bitmap;
import android.net.Uri;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.io.File;

public interface NewOpinionContract {

	interface View extends MvpView {
		void setToolbar();
		void setUserProfile(String userName, String uriPhotoProfile, String urlPoliticalFlag);
		void selectImageFromGallery();
		void createBitMap();
		void setImageBitmapSelectedPhoto(Bitmap bitmap);
		void deleteSelectedImageDialog();
		void deleteSelectedImage();
		void takePictureIntent();
		void setCurrentPhotoPath(String photoPath);
		void showSelectedPhotoView(boolean show);
		void newOpinionPublished();
		void setResultIntent();
		void showProgress(boolean show);
	}

	interface Presenter extends MvpPresenter<NewOpinionContract.View> {
		void selectImageFromGallery(String permission, int requestPermission);
		void selectImageFromCamera(String permission, int requestPermission);
		void createBitMap();
		void setImageBitmapSelectedPhoto(Bitmap bitmap);
		void deleteSelectedImage();
		File createImageFile();
		void loadOpinionWithImage(Bitmap bitmap, String opinionText);
		void loadOpinionWithoutImage(String opinionText);
		void setAuthListener();
		void removeAuthListener();
	}
}
