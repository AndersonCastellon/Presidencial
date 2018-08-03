package com.papaprogramador.presidenciales.TreeMvp.NewOpinion;

import android.graphics.Bitmap;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface NewOpinionContract {

	interface View extends MvpView {
		void setToolbar();
		void setUserProfile(String userName, String uriPhotoProfile, String urlPoliticalFlag);
		void selectImageFromGallery();
		void createBitMap();
		void setImageBitmapSelectedPhoto(Bitmap bitmap);
		void showSelectedPhotoView(boolean show);
	}

	interface Presenter extends MvpPresenter<NewOpinionContract.View> {
		void selectImageFromGallery();
		void createBitMap();
		void setImageBitmapSelectedPhoto(Bitmap bitmap);
		void setAuthListener();
		void removeAuthListener();
	}
}
