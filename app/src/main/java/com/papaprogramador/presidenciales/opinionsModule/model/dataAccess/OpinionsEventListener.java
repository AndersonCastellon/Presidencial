package com.papaprogramador.presidenciales.opinionsModule.model.dataAccess;

import com.papaprogramador.presidenciales.common.pojo.Opinion;

public interface OpinionsEventListener {
	void onChildAdded(Opinion opinion);
	void onChildUpdated(Opinion opinion);
	void onChildRemoved(Opinion opinion);

	void onComplete();
	void onError(int resMsg);
}
