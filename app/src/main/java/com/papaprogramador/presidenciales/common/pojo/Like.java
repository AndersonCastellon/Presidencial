package com.papaprogramador.presidenciales.common.pojo;

import com.google.firebase.database.Exclude;

public class Like {

	@Exclude
	private String opinionId;
	private boolean clickLike;

	public Like() {
	}

	public Like(String opinionId, boolean clickLike) {
		this.opinionId = opinionId;
		this.clickLike = clickLike;
	}
	@Exclude
	public String getOpinionId() {
		return opinionId;
	}
	@Exclude
	public void setOpinionId(String opinionId) {
		this.opinionId = opinionId;
	}

	public boolean isClickLike() {
		return clickLike;
	}

	public void setClickLike(boolean clickLike) {
		this.clickLike = clickLike;
	}
}

