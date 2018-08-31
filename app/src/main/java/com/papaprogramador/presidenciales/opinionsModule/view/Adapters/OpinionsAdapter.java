package com.papaprogramador.presidenciales.opinionsModule.view.Adapters;

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
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpinionsAdapter extends RecyclerView.Adapter<OpinionsAdapter.ViewHolder> {

	private List<Opinion> mainListOpinions;
	private long lastItem = 0;
	private OnItemClickListener listener;
	private Context context;

	public OpinionsAdapter(OnItemClickListener listener) {
		this.mainListOpinions = new ArrayList<>();
		this.listener = listener;

	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opinions_layout, parent, false);
		context = parent.getContext();
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Opinion opinion = mainListOpinions.get(position);

		holder.userName.setText(opinion.getUserName());
		holder.datePublication.setText(opinion.getDatePublication());
		holder.opinionText.setText(opinion.getOpinionText());

		holder.btnLikeOpinion.setText(String.valueOf(opinion.getCountLike()));
		holder.btnCommentOpinion.setText(String.valueOf(opinion.getCountComments()));
		holder.btnShareOpinion.setText(String.valueOf(opinion.getCountShare()));

		holder.urlPhotoProfile = opinion.getUrlPhotoProfile();
		holder.urlPoliticalFlag = opinion.getUrlPoliticalFlag();

		RequestOptions options = new RequestOptions()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.centerCrop();

		if (opinion.getUrlOpinionImage() != null) {
			holder.urlOpinionImage = opinion.getUrlOpinionImage();

			Glide.with(context)
					.load(holder.urlOpinionImage)
					.apply(options)
					.into(holder.imageOpinion);
		} else {
			holder.imageOpinion.setVisibility(View.GONE);
		}

		holder.opinionId = opinion.getOpinionId();
		holder.userId = opinion.getUserId();

		holder.likeClicked = opinion.isLikeClicked();


		Glide.with(context)
				.load(holder.urlPhotoProfile)
				.apply(options)
				.into(holder.userPhotoProfile);

		Glide.with(context)
				.load(holder.urlPoliticalFlag)
				.apply(options)
				.into(holder.flagPolitical);

		holder.setOnClickListener(opinion, listener);
	}

	public List<Opinion> getMainListOpinions() {
		Collections.sort(mainListOpinions);
		notifyItemRangeInserted((mainListOpinions.size() + 1), mainListOpinions.size());

		if (mainListOpinions.size() != 0) {
			lastItem = mainListOpinions.get((mainListOpinions.size() - 1)).getOrderBy();
		}

		return mainListOpinions;
	}

	public void setMainListOpinions(List<Opinion> newOpinionList) {

		if (!this.mainListOpinions.containsAll(newOpinionList)) {
			this.mainListOpinions.addAll(newOpinionList);
		}
	}

	public long getLastItemId() {
		return lastItem;
	}

	@Override
	public int getItemCount() {
		return mainListOpinions == null ? 0 : mainListOpinions.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		private String opinionId;
		private String userId;
		private String urlPhotoProfile;
		private String urlPoliticalFlag;
		private String urlOpinionImage;

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

		private boolean likeClicked;
		private View view;

		ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			view = itemView;
		}

		void setOnClickListener(final Opinion opinion, final OnItemClickListener listener) {
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
						case R.id.btn_like_opinion:
							listener.onLikeClick(opinion);
							break;
						case R.id.btn_comment_opinion:
							listener.onCommentClick(opinion);
							break;
						case R.id.btn_share_opinion:
							listener.onShareClick(opinion);
							break;
						case R.id.opinion_menu:
							listener.onMenuClick(opinion);
							break;
						case R.id.image_opinion:
							listener.onImageClick(opinion);
							break;
					}
				}
			});
		}
	}
}
