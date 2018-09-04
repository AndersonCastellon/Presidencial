package com.papaprogramador.presidenciales.opinionsModule.view.Adapters;

import com.papaprogramador.presidenciales.common.pojo.Opinion;

public interface OnItemClickListener {
	void onImageClick(Opinion opinion);
	void onLikeClick(Opinion opinion);
	void onCommentClick(Opinion opinion);
	void onShareClick(Opinion opinion);
	void onEditOpinionClick(Opinion opinion);
	void onRemoveOpinionClick(Opinion opinion);
}
