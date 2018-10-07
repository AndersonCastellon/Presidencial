package com.papaprogramador.presidenciales.commentsModule.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.common.dataAccess.FirebaseUserAPI;
import com.papaprogramador.presidenciales.common.pojo.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

	private List<Comment> comments;
	private FirebaseUserAPI userAPI;
	private Context context;
	private OnMenuCommentItemClickListener itemClickListener;

	public CommentsAdapter(OnMenuCommentItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
		userAPI = FirebaseUserAPI.getInstance();
		comments = new ArrayList<>();
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		context = parent.getContext();
		View view = LayoutInflater.from(context)
				.inflate(R.layout.comments_items_layout, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		setupClickableViews(viewHolder);
		return viewHolder;
	}

	private void setupClickableViews(final ViewHolder viewHolder) {
		viewHolder.imageMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupMenu popupMenu = new PopupMenu(context, viewHolder.imageMenu);
				popupMenu.inflate(R.menu.comment_menu);
				popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
							case R.id.remove_comment:
								itemClickListener.onDeleteClick(comments.get(viewHolder.getAdapterPosition()));
								break;
						}
						return false;
					}
				});
				popupMenu.show();
			}
		});
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Comment comment = comments.get(position);
		holder.bindView(comment);
	}

	@Override
	public int getItemCount() {
		return comments.size();
	}

	public void addAllComments(List<Comment> comments) {
		this.comments = comments;
		notifyItemRangeInserted(0, comments.size());
	}

	public void addComment(Comment comment) {
		if (getItemPosition(comment) == -1) {
			comments.add(comment);
			notifyItemInserted(comments.size());
		}
	}

	private int getItemPosition(Comment comment) {
		int currentPosition = -1;
		for (int i = 0; i < comments.size() && currentPosition == -1; i++) {
			Comment c = comments.get(i);
			if (c.getCommentId().equals(comment.getCommentId())) {
				currentPosition = i;
			}
		}
		return currentPosition;
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.flag_political)
		ImageView flagPolitical;
		@BindView(R.id.photo_user)
		CircleImageView photoUser;
		@BindView(R.id.user_name)
		TextView userName;
		@BindView(R.id.image_menu)
		ImageButton imageMenu;
		@BindView(R.id.comment)
		TextView comment;
		Context context;

		ViewHolder(View itemView) {
			super(itemView);
			context = itemView.getContext();
			ButterKnife.bind(this, itemView);
		}

		void bindView(Comment comment) {
			RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop();

			if (comment.getUserId().equals(userAPI.getUserId())) {
				imageMenu.setVisibility(View.VISIBLE);
			} else {
				imageMenu.setVisibility(View.GONE);
			}

			Glide.with(context).load(comment.getUserPhotoUrl()).apply(options).into(photoUser);
			if (!comment.getUserPoloticalFlagUrl().isEmpty()) {
				Glide.with(context).load(comment.getUserPoloticalFlagUrl()).apply(options).into(flagPolitical);
			} else {
				flagPolitical.setVisibility(View.GONE);
			}

			userName.setText(comment.getUserName());
			this.comment.setText(comment.getContent());
		}
	}
}
