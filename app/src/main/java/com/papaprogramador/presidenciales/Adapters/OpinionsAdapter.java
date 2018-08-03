package com.papaprogramador.presidenciales.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpinionsAdapter extends RecyclerView.Adapter<OpinionsAdapter.OpinionsViewHolder> {

	private List<Opinions> opinionsList;

	public OpinionsAdapter() {
	}

	public List<Opinions> getOpinionsList() {
		return opinionsList;
	}

	public void setOpinionsList(List<Opinions> opinionsList) {
		this.opinionsList = opinionsList;
	}

	@NonNull
	@Override
	public OpinionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opinions_layout, parent, false);
		return new OpinionsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull OpinionsViewHolder holder, int position) {

		Opinions opinions = opinionsList.get(position);

		holder.userName.setText(opinions.getUserName());
		holder.datePublication.setText((int) opinions.getDatePublication());
		holder.opinionText.setText(opinions.getOpinionText());
		holder.btnLikeOpinion.setText(opinions.getCountLike());
		holder.btnCommentOpinion.setText(opinions.getCountComments());
		holder.btnShareOpinion.setText(opinions.getCountShare());

		holder.urlPhotoProfile = opinions.getUrlPhotoProfile();
		holder.urlPoliticalFlag = opinions.getUrlPoliticalFlag();
		holder.urlOpinionImage = opinions.getUrlOpinionImage();

		holder.opinionId = opinions.getOpinionId();
		holder.userId = opinions.getUserId();

		holder.likeClicked = opinions.isLikeClicked();

		RequestOptions options = new RequestOptions()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.centerCrop();

		//Foto del usuario
		Glide.with(holder.context)
				.load(holder.urlPhotoProfile)
				.apply(options)
				.into(holder.userPhotoProfile);

		//Bandera politica del usuario
		Glide.with(holder.context)
				.load(holder.urlPoliticalFlag)
				.apply(options)
				.into(holder.flagPolitical);

		//Imagen de la opinion cargada por el usuario
		Glide.with(holder.context)
				.load(holder.urlOpinionImage)
				.apply(options)
				.into(holder.imageOpinion);

		holder.setOnClickListener();
	}

	@Override
	public int getItemCount() {
		return opinionsList.size();
	}

	public static class OpinionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		Context context;

		String opinionId;
		String userId;
		String urlPhotoProfile;
		String urlPoliticalFlag;
		String urlOpinionImage;

		@BindView(R.id.img_user_profile)
		ImageView userPhotoProfile;
		@BindView(R.id.user_name)
		TextView userName;
		@BindView(R.id.date_publication)
		TextView datePublication;
		@BindView(R.id.flag_political)
		ImageView flagPolitical;
		@BindView(R.id.opinion_text)
		TextView opinionText;
		@BindView(R.id.image_opinion)
		ImageView imageOpinion;
		@BindView(R.id.btn_like_opinion)
		Button btnLikeOpinion;
		@BindView(R.id.btn_comment_opinion)
		Button btnCommentOpinion;
		@BindView(R.id.btn_share_opinion)
		Button btnShareOpinion;
		@BindView(R.id.opinion_menu)
		ImageView opinionMenu;

		boolean likeClicked;

		OpinionsViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			context = itemView.getContext();
		}

		void setOnClickListener() {
			btnLikeOpinion.setOnClickListener(this);
			btnCommentOpinion.setOnClickListener(this);
			btnShareOpinion.setOnClickListener(this);
			opinionMenu.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()){
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
}
