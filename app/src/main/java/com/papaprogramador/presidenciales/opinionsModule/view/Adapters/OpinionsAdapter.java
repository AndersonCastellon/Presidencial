package com.papaprogramador.presidenciales.opinionsModule.view.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseUserAPI;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.opinionsModule.pojo.Like;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpinionsAdapter extends RecyclerView.Adapter<OpinionsAdapter.ViewHolder> {

	private List<Opinion> opinionList;
	private long lastItem = 0;
	private OnItemClickListener listener;
	private FirebaseUserAPI mUserAPI;
	private Context context;
	private boolean order = true;

	public OpinionsAdapter(Context context, OnItemClickListener listener) {
		this.context = context;
		this.opinionList = new ArrayList<>();
		this.listener = listener;
		this.mUserAPI = FirebaseUserAPI.getInstance();

	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opinions_layout, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

		Opinion opinion = opinionList.get(position);

		setViewHolder(opinion, holder);
		setClickLike();
		holder.setOnClickListener(opinion, context, listener);
	}

	private void setClickLike() {

	}

	private void setViewHolder(Opinion opinion, ViewHolder holder) {
		if (opinion.getUserId().equals(mUserAPI.getUserId())) {
			holder.opinionMenu.setVisibility(View.VISIBLE);
		}

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

		Glide.with(context)
				.load(holder.urlPhotoProfile)
				.apply(options)
				.into(holder.userPhotoProfile);

		Glide.with(context)
				.load(holder.urlPoliticalFlag)
				.apply(options)
				.into(holder.flagPolitical);
	}

	public long getLastItemId() {
		if (order) {
			Collections.sort(opinionList);
		}
		if (opinionList.size() != 0) {
			lastItem = opinionList.get((opinionList.size() - 1)).getOrderBy();
		}
		return lastItem;
	}

	@Override
	public int getItemCount() {
		return opinionList == null ? 0 : opinionList.size();
	}

	public void add(Opinion opinion) {
		if (!opinionList.contains(opinion)) {
			opinionList.add(opinion);
			if (order) {
				Collections.sort(opinionList);
			}
			notifyDataSetChanged();
		} else {
			order = false;
		}
	}

	public void update(Opinion opinion) {
		if (opinionList.contains(opinion)) {
			final int index = opinionList.indexOf(opinion);
			opinionList.set(index, opinion);
			notifyItemChanged(index);
		}
	}

	public void remove(Opinion opinion) {
		if (opinionList.contains(opinion)) {
			final int index = opinionList.indexOf(opinion);
			opinionList.remove(index);
			notifyItemRemoved(index);
		}
	}

	class ViewHolder extends RecyclerView.ViewHolder {

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

		void setOnClickListener(final Opinion opinion, final Context context, final OnItemClickListener listener) {

			view.findViewById(R.id.btn_like_opinion).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onLikeClick(opinion);
				}
			});

			view.findViewById(R.id.btn_comment_opinion).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onCommentClick(opinion);
				}
			});

			view.findViewById(R.id.btn_share_opinion).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onShareClick(opinion);
				}
			});

			view.findViewById(R.id.opinion_menu).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PopupMenu popupMenu = new PopupMenu(context, opinionMenu);
					popupMenu.inflate(R.menu.opinion_menu);
					popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {

							switch (item.getItemId()) {
								case R.id.edit_opinion:
									listener.onEditOpinionClick(opinion);
									break;
								case R.id.remove_opinion:
									listener.onRemoveOpinionClick(opinion);
									break;
							}
							return false;
						}
					});
				}
			});

			view.findViewById(R.id.image_opinion).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onImageClick(opinion);
				}
			});
		}
	}
}
