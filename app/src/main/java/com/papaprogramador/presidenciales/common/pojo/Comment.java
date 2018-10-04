package com.papaprogramador.presidenciales.common.pojo;

public class Comment {
	private String commentId;
	private String opinionId;
	private String userId;
	private String userName;
	private String userPhotoUrl;
	private String userPoloticalFlagUrl;
	private String content;

	public Comment() {
	}

	Comment(Builder builder) {
		commentId = builder.commentId;
		opinionId = builder.opinionId;
		userId = builder.userId;
		userName = builder.userName;
		userPhotoUrl = builder.userPhotoUrl;
		userPoloticalFlagUrl = builder.userPoloticalFlagUrl;
		content = builder.content;
	}

	public String getCommentId() {
		return commentId;
	}

	public String getOpinionId() {
		return opinionId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPhotoUrl() {
		return userPhotoUrl;
	}

	public String getUserPoloticalFlagUrl() {
		return userPoloticalFlagUrl;
	}

	public String getContent() {
		return content;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String commentId;
		private String opinionId;
		private String userId;
		private String userName;
		private String userPhotoUrl;
		private String userPoloticalFlagUrl;
		private String content;

		public Builder() {
		}

		public Builder commentId(String commentId) {
			this.commentId = commentId;
			return this;
		}

		public Builder userName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder userPhotoUrl(String userPhotoUrl) {
			this.userPhotoUrl = userPhotoUrl;
			return this;
		}

		public Builder userPoliticlaFlagUrl(String userPoloticalFlagUrl){
			this.userPoloticalFlagUrl = userPoloticalFlagUrl;
			return this;
		}

		public Builder opinionId(String opinionId) {
			this.opinionId = opinionId;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder content(String content) {
			this.content = content;
			return this;
		}

		public Comment build() {
			return new Comment(this);
		}
	}
}
