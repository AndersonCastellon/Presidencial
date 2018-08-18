package com.papaprogramador.presidenciales.TreeMvp.NewOpinion;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.View.Activity.MainView;

import java.io.File;
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
	private static final String PERMISSION_GALLERY = android.Manifest.permission.READ_EXTERNAL_STORAGE;
	private static final String PERMISSION_CAMERA = android.Manifest.permission.CAMERA;
	private static final String PACKAGE_NAME_APP = "com.papaprogramador.presidenciales";

	private String mCurrentPhotoPath;
	private Uri mPhotoSelectedUri;
	private Bitmap bitmap;

	RequestOptions options = new RequestOptions()
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.centerCrop();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_opinion_view);
		ButterKnife.bind(this);

		btnUploadOpinion.setEnabled(false);
		btnUploadOpinion.setBackgroundColor(getResources().getColor(R.color.divider));

		etOpinionText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				btnUploadOpinion.setEnabled(false);
				btnUploadOpinion.setBackgroundColor(getResources().getColor(R.color.divider));
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if (s.toString().trim().length() == 0) {
					btnUploadOpinion.setEnabled(false);
					btnUploadOpinion.setBackgroundColor(getResources().getColor(R.color.divider));
				} else {
					btnUploadOpinion.setEnabled(true);
					btnUploadOpinion.setBackgroundColor(getResources().getColor(R.color.primary_dark));
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@NonNull
	@Override
	public NewOpinionContract.Presenter createPresenter() {
		return new NewOpinionPresenter(this);
	}

	@OnClick({R.id.btn_opinion_upload_image, R.id.btn_opinion_upload_photo_camera, R.id.btn_upload_opinion,
			R.id.image_opinion_selected, R.id.btn_delete_image})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_opinion_upload_image:
				getPresenter().selectImageFromGallery(PERMISSION_GALLERY, RP_GALLERY);
				break;
			case R.id.btn_opinion_upload_photo_camera:
				getPresenter().selectImageFromCamera(PERMISSION_CAMERA, RP_STORAGE);
				break;
			case R.id.btn_upload_opinion:
				getLoadNewOpinion();
				break;
			case R.id.btn_delete_image:
				deleteSelectedImageDialog();
				break;
		}
	}

	private void getLoadNewOpinion() {
		String opinionText = etOpinionText.getText().toString();

		if (bitmap != null) {
			getPresenter().loadOpinionWithImage(bitmap, opinionText);
		} else {
			getPresenter().loadOpinionWithoutImage(opinionText);
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
			bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mPhotoSelectedUri);
			getPresenter().setImageBitmapSelectedPhoto(bitmap);
			mPhotoSelectedUri = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setImageBitmapSelectedPhoto(Bitmap bitmap) {
		Glide.with(this)
				.asBitmap()
				.load(bitmap)
				.apply(options)
				.into(imageOpinionSelected);
	}

	@Override
	public void deleteSelectedImageDialog() {

		new AlertDialog.Builder(this)
				.setTitle(getString(R.string.delete_selected_image_title))
				.setMessage(getString(R.string.delete_selected_image_message))
				.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getPresenter().deleteSelectedImage();
					}
				})
				.setNegativeButton(getString(R.string.cancel_dialog_text), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
	}

	@Override
	public void deleteSelectedImage() {
		imageOpinionSelected.setImageBitmap(null);
		bitmap = null;
	}

	@Override
	public void takePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			File photoFile;
			photoFile = getPresenter().createImageFile();

			if (photoFile != null) {
				Uri photoUri = FileProvider.getUriForFile(this,
						PACKAGE_NAME_APP, photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(takePictureIntent, RC_CAMERA);
			}
		}
	}

	@Override
	public void setCurrentPhotoPath(String photoPath) {
		this.mCurrentPhotoPath = photoPath;
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
	public void newOpinionPublished() {
		Toast.makeText(this, getResources().getString(R.string.new_opinion_publiched), Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, MainView.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void opinionPublishedProgress(final double progress) {

		final ProgressDialog horizontalProgressDialog = new ProgressDialog(this);
		horizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		horizontalProgressDialog.setMessage(getString(R.string.opinion_published_progress_text));
		horizontalProgressDialog.setCancelable(false);
		horizontalProgressDialog.setMax(100);
		horizontalProgressDialog.show();

		new Thread(new Runnable() {
			int prog = (int) progress;

			@Override
			public void run() {
				while (prog <= 100) {
					horizontalProgressDialog.setProgress(prog);
					if (prog == 100) {
						horizontalProgressDialog.dismiss();
					}
				}
			}
		}).start();
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
				case RC_CAMERA:
					mPhotoSelectedUri = addPictureFromGallery();
					getPresenter().createBitMap();
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Uri addPictureFromGallery() {

		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

		File file = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);

		mCurrentPhotoPath = null;
		return contentUri;

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			switch (requestCode) {
				case RP_STORAGE:
					getPresenter().selectImageFromCamera(PERMISSION_CAMERA, RP_STORAGE);
					break;
				case RP_GALLERY:
					getPresenter().selectImageFromGallery(PERMISSION_GALLERY, RP_GALLERY);
					break;
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public boolean onSupportNavigateUp() {

		new AlertDialog.Builder(this)
				.setTitle(getString(R.string.opinion_cancel_title))
				.setMessage(getString(R.string.opinion_cancel_message))
				.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onBackPressed();
					}
				})
				.setNegativeButton(getString(R.string.cancel_dialog_text), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
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
