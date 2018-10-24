package com.papaprogramador.presidenciales.opinionsModule.events;

import com.papaprogramador.presidenciales.common.pojo.Opinion;

import java.util.List;

public class OpinionEvent {

	public static final int INITIAL_DATA = 100;
	public static final int SUCCES_ADD = 1;
	public static final int SUCCES_UPDATE = 2;
	public static final int SUCCES_REMOVE = 3;
	public static final int ERROR_SERVER = 4;
	public static final int ERROR_TO_REMOVE = 5;
	public static final int ON_COMPLETE = 6;

	private List<Opinion> opinions;
	private Opinion opinion;
	private int typeEvent;
	private int resMsg;

	public OpinionEvent() {
	}

	public Opinion getOpinion() {
		return opinion;
	}

	public void setOpinion(Opinion opinion) {
		this.opinion = opinion;
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

	public List<Opinion> getOpinions() {
		return opinions;
	}

	public void setOpinions(List<Opinion> opinions) {
		this.opinions = opinions;
	}
}
