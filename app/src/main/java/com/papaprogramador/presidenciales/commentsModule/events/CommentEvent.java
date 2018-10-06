package com.papaprogramador.presidenciales.commentsModule.events;

import com.papaprogramador.presidenciales.common.pojo.Comment;

import java.util.List;

public class CommentEvent {
	public static final int INITIAL_DATA = 0;
	public static final int SUCCES_ADD = 1;
	public static final int NO_DATA = 2;
	public static final int ERROR = 3;

	private List<Comment> comments;
	private Comment comment;
	private int eventType;
	private int resMsg;

	private CommentEvent(Builder builder) {
		comments = builder.comments;
		comment = builder.comment;
		eventType = builder.eventType;
		resMsg = builder.resMsg;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public Comment getComment() {
		return comment;
	}

	public int getEventType() {
		return eventType;
	}

	public int getResMsg() {
		return resMsg;
	}

	public static Builder Builder() {
		return new Builder();
	}

	public static class Builder {
		private List<Comment> comments;
		private Comment comment;
		private int eventType;
		private int resMsg;

		private Builder() {
		}

		public Builder comments(List<Comment> comments) {
			this.comments = comments;
			return this;
		}

		public Builder comment(Comment comment) {
			this.comment = comment;
			return this;
		}

		public Builder eventType(int eventType) {
			this.eventType = eventType;
			return this;
		}

		public Builder resMsg(int resMsg) {
			this.resMsg = resMsg;
			return this;
		}

		public CommentEvent build() {
			return new CommentEvent(this);
		}

	}
}
