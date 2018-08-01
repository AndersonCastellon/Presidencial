package com.papaprogramador.presidenciales.TreeMvp.NewOpinion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.papaprogramador.presidenciales.R;

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
	Button imageOpinionSelected;
	@BindView(R.id.toolbar)
	Toolbar toolbar;

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

	@OnClick({R.id.btn_opinion_upload_image, R.id.btn_opinion_upload_photo_camera, R.id.btn_upload_opinion})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_opinion_upload_image:
				break;
			case R.id.btn_opinion_upload_photo_camera:
				break;
			case R.id.btn_upload_opinion:
				break;
		}
	}

	@OnClick(R.id.image_opinion_selected)
	public void onViewClicked() {
	}

	@Override
	public void setToolbar() {

		setSupportActionBar(toolbar);
		toolbar.setTitle(R.string.new_opinion);
	}

	@Override
	public void setUserProfile(String userName, String uriPhotoProfile, String urlPoliticalFlag) {

		this.userName.setText(userName);

		if (uriPhotoProfile != null) {
			Glide.with(this)
					.load(uriPhotoProfile)
					.centerCrop()
					.into(imgUserProfile);
		} else {

			Glide.with(this)
					.load(R.drawable.ic_person)
					.centerCrop()
					.into(imgUserProfile);

			imgUserProfile.setDrawingCacheBackgroundColor(getResources().getColor(R.color.accent));
		}

		if (urlPoliticalFlag != null) {
			Glide.with(this)
					.load(urlPoliticalFlag)
					.centerCrop()
					.into(flagPolitical);
		} else {
			flagPolitical.setVisibility(View.INVISIBLE);
		}

		imageOpinionSelected.setVisibility(View.GONE);
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
