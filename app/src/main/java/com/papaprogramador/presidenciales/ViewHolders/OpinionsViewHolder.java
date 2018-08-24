package com.papaprogramador.presidenciales.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpinionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

	Context context;

	String opinionId;
	String userId;
	String urlPhotoProfile;
	String urlPoliticalFlag;
	String urlOpinionImage;

	@BindView(R.id.img_user_profile)
	SimpleDraweeView userPhotoProfile;
	@BindView(R.id.user_name)
	TextView userName;
	@BindView(R.id.date_publication)
	TextView datePublication;
	@BindView(R.id.flag_political)
	SimpleDraweeView flagPolitical;
	@BindView(R.id.opinion_text)
	TextView opinionText;
	@BindView(R.id.image_opinion)
	SimpleDraweeView imageOpinion;
	@BindView(R.id.btn_like_opinion)
	Button btnLikeOpinion;
	@BindView(R.id.btn_comment_opinion)
	Button btnCommentOpinion;
	@BindView(R.id.btn_share_opinion)
	Button btnShareOpinion;
	@BindView(R.id.opinion_menu)
	ImageView opinionMenu;

	boolean likeClicked;

	RequestOptions options;

	public OpinionsViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		context = itemView.getContext();
		options = new RequestOptions()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.centerCrop();
	}

	public void setData (Opinions opinion){

		userName.setText(opinion.getUserName());
		datePublication.setText(opinion.getDatePublication());
		opinionText.setText(opinion.getOpinionText());

		btnLikeOpinion.setText(String.valueOf(opinion.getCountLike()));
		btnCommentOpinion.setText(String.valueOf(opinion.getCountComments()));
		btnShareOpinion.setText(String.valueOf(opinion.getCountShare()));

		urlPhotoProfile = opinion.getUrlPhotoProfile();
		urlPoliticalFlag = opinion.getUrlPoliticalFlag();

		if (opinion.getUrlOpinionImage() != null) {
			urlOpinionImage = opinion.getUrlOpinionImage();

			Glide.with(context)
					.load(urlOpinionImage)
					.apply(options)
					.into(imageOpinion);
		} else {
			imageOpinion.setVisibility(View.GONE);
		}

		opinionId = opinion.getOpinionId();
		userId = opinion.getUserId();

		likeClicked = opinion.isLikeClicked();

		//Foto del usuario
		Glide.with(context)
				.load(urlPhotoProfile)
				.apply(options)
				.into(userPhotoProfile);

		//Bandera politica del usuario
		Glide.with(context)
				.load(urlPoliticalFlag)
				.apply(options)
				.into(flagPolitical);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_like_opinion:
				break;
			case R.id.btn_comment_opinion:
				break;
			case R.id.btn_share_opinion:
				break;
			case R.id.opinion_menu:
				break;
		}
	}
}
