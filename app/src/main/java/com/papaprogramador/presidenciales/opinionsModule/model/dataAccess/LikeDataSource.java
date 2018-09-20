package com.papaprogramador.presidenciales.opinionsModule.model.dataAccess;

import android.support.v4.util.Pair;

import com.papaprogramador.presidenciales.common.BasicListener;
import com.papaprogramador.presidenciales.common.pojo.Like;

import java.util.List;


public interface LikeDataSource {
	void getLikes(String opinionId, ListLikesListener listener);

	void toggleLike(Like like, ToggleLikeListener listener);

	void addLikeNotifier(String opinionId, LikeNotifierListener listener);

	void removeLikeNotifier(String opinionId);

	interface ListLikesListener extends BasicListener {
		void onSuccess(List<Like> likes);
	}

	interface ToggleLikeListener extends BasicListener {
		void onSuccess(boolean result);
	}

	interface LikeNotifierListener extends BasicListener {
		void onSuccess(Pair<Like, Boolean> likePair);
	}
}
