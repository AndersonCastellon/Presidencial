package com.papaprogramador.presidenciales.opinionsModule.pojo;

public class Likes {

	private String userId;
	private boolean clickLike;

	public Likes() {
	}

	public Likes(String userId, boolean clickLike) {
		this.userId = userId;
		this.clickLike = clickLike;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isClickLike() {
		return clickLike;
	}

	public void setClickLike(boolean clickLike) {
		this.clickLike = clickLike;
	}
}

