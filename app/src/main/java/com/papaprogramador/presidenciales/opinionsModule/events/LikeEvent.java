package com.papaprogramador.presidenciales.opinionsModule.events;

import com.papaprogramador.presidenciales.common.pojo.Like;

public class LikeEvent {

	public static final int SUCCES_ADD = 1;
	public static final int SUCCES_REMOVE = 2;
	public static final int ERROR = 3;

	private Like like;
	private int typeEvent;
	private int resMsg;

	public LikeEvent() {
	}

	public Like getLike() {
		return like;
	}

	public void setLike(Like like) {
		this.like = like;
	}

	public int getTypeEvent() {
		return typeEvent;
	}

	public void setTypeEvent(int typeEvent) {
		this.typeEvent = typeEvent;
	}

	public int getResMsg() {
		return resMsg;
	}

	public void setResMsg(int resMsg) {
		this.resMsg = resMsg;
	}
}
