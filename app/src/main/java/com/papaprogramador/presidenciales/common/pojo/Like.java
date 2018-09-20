package com.papaprogramador.presidenciales.common.pojo;

public class Like {

	private String opinionId;
	private String userId;

	private Like(Builder builder) {
		opinionId = builder.opinionId;
		userId = builder.userId;
	}

	public String getOpinionId() {
		return opinionId;
	}

	public String getUserId() {
		return userId;
	}

	public static Builder Builder() {
		return new Builder();
	}

	public static class Builder {
		private String opinionId;
		private String userId;

		public Builder() {
		}

		public Builder opinionId(String photoId) {
			this.opinionId = photoId;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Like build() {
			return new Like(this);
		}
	}
}

