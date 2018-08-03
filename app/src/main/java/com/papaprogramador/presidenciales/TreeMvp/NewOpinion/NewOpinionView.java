package com.papaprogramador.presidenciales.TreeMvp.NewOpinion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewOpinionView extends MvpActivity<NewOpinionContract.View, NewOpinionContract.Presenter>
		implements NewOpinionContract.View {

	@BindView(R.id.img_user_profile)
	ImageView imgUserProfile;
	@BindView(R.id.user_name)
	TextView userName;
	@BindView(R.id.flag_political)
	ImageView flagPolitical;
	@BindView(R.id.et_opinion_text)
	EditText etOpinionText;
	@BindView(R.id.btn_opinion_upload_image)
	Button btnOpinionUploadImage;
	@BindView(R.id.btn_opinion_upload_photo_camera)
	Button btnOpinionUploadPhotoCamera;
	@BindView(R.id.btn_upload_opinion)
	Button btnUploadOpinion;
	@BindView(R.id.opinion_buttons)
	LinearLayout opinionButtons;
	@BindView(R.id.image_opinion_selected)
	ImageView imageOpinionSelected;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.btn_delete_image)
	ImageButton btnDeleteImage;

	private static final int RC_GALLERY = 1;
	private static final int RC_CAMERA = 2;
	private static final int RP_GALLERY = 3;
	private static final int RP_STORAGE = 4;
	private static final String IMAGE_STORAGE_DIRECTORY = "/PresidencialesApp";
	private static final String PATH_IMAGE_OPINIONS = "OpinionsImages";

	private String mCurrentPhotoPath;
	private Uri mPhotoSelectedUri;

	RequestOptions options = new RequestOptions()
			.centerCrop()
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.centerCrop();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_opinion_view);
		ButterKnife.bind(this);
	}

	@NonNull
	@Override
	public NewOpinionContract.Presenter createPresenter() {
		return new NewOpinionPresenter();
	}

	@OnClick({R.id.btn_opinion_upload_image, R.id.btn_opinion_upload_photo_camera, R.id.btn_upload_opinion,
			R.id.image_opinion_selected, R.id.btn_delete_image})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_opinion_upload_image:
				getPresenter().selectImageFromGallery();
				break;
			case R.id.btn_opinion_upload_photo_camera:
				break;
			case R.id.btn_upload_opinion:
				break;
			case R.id.image_opinion_selected:
				break;
			case R.id.btn_delete_image:
				break;
		}
	}

	@Override
	public void setToolbar() {

		setSupportActionBar(toolbar);
		if (toolbar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			toolbar.setTitle(R.string.new_opinion);
		}
	}

	@Override
	public void setUserProfile(String userName, String uriPhotoProfile, String urlPoliticalFlag) {

		this.userName.setText(userName);

		if (uriPhotoProfile != null) {
			Glide.with(this)
					.load(uriPhotoProfile)
					.apply(options)
					.into(imgUserProfile);
		} else {

			Glide.with(this)
					.load(R.drawable.ic_person)
					.apply(options)
					.into(imgUserProfile);

			imgUserProfile.setDrawingCacheBackgroundColor(getResources().getColor(R.color.accent));
		}

		if (urlPoliticalFlag != null) {
			Glide.with(this)
					.load(urlPoliticalFlag)
					.apply(options)
					.into(flagPolitical);
		} else {
			flagPolitical.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void selectImageFromGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, RC_GALLERY);
	}

	@Override
	public void createBitMap() {
		try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mPhotoSelectedUri);
			getPresenter().setImageBitmapSelectedPhoto(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setImageBitmapSelectedPhoto(Bitmap bitmap) {
//		Glide.with(this)
//				.asBitmap()
//				.load(bitmap)
//				.apply(options)
//				.into(imageOpinionSelected);

		imageOpinionSelected.setImageBitmap(bitmap);
	}

	@Override
	public void showSelectedPhotoView(boolean show) {
		if (show) {
			opinionButtons.setVisibility(View.GONE);
			btnDeleteImage.setVisibility(View.VISIBLE);
			imageOpinionSelected.setVisibility(View.VISIBLE);
		} else {
			opinionButtons.setVisibility(View.VISIBLE);
			btnDeleteImage.setVisibility(View.GONE);
			imageOpinionSelected.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case RC_GALLERY:
					if (data != null) {
						mPhotoSelectedUri = data.getData();
						getPresenter().createBitMap();
					}
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return super.onSupportNavigateUp();
	}

	@Override
	protected void onStart() {
		super.onStart();
		getPresenter().setAuthListener();
	}

	@Override
	protected void onStop() {
		super.onStop();
		getPresenter().removeAuthListener();
	}
}
