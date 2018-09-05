package com.papaprogramador.presidenciales.opinionsModule.model.dataAccess;

import com.papaprogramador.presidenciales.common.pojo.Like;

public interface LikesEventListener {
	void onChildAdded(Like like);
	void onChildRemoved(Like like);

	void onError(int resMsg);
}
