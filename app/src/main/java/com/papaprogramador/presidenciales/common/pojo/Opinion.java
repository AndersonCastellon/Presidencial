package com.papaprogramador.presidenciales.common.pojo;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Opinion implements Comparable<Opinion> {

	public static final String ORDER_BY = "dataTime";

	@Exclude
	private String opinionId;
	private String urlOpinionImage;
	private String content;
	private long dataTime;
	private String userId;
	private String userName;
	private String urlPhotoProfile;
	private String urlPoliticalFlag;
	private List<String> userLikes;
	private List<String> userComments;
	private List<String> userShares;


	public Opinion() {
		userLikes = new ArrayList<>();
	}

	public Opinion(Builder builder) {
		this.opinionId = builder.opinionId;
		this.urlOpinionImage = builder.urlOpinionImage;
		this.content = builder.content;
		this.dataTime = builder.dataTime;
		this.userId = builder.userId;
		this.userName = builder.userName;
		this.urlPhotoProfile = builder.urlPhotoProfile;
		this.urlPoliticalFlag = builder.urlPoliticalFlag;
		this.userLikes = builder.userLikes;
		this.userComments = builder.userComments;
		this.userShares = builder.userShares;
	}

	public long getDataTime() {
		return dataTime;
	}

	@Exclude
	public String getOpinionId() {
		return opinionId;
	}

	@Exclude
	public void setOpinionId(String opinionId) {
		this.opinionId = opinionId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUrlPhotoProfile() {
		return urlPhotoProfile;
	}

	public String getUrlPoliticalFlag() {
		return urlPoliticalFlag;
	}

	public String getContent() {
		return content;
	}

	public String getUrlOpinionImage() {
		return urlOpinionImage;
	}

	public List<String> getUserLikes() {
		return userLikes;
	}

	public List<String> getUserComments() {
		return userComments;
	}

	public List<String> getUserShares() {
		return userShares;
	}

	public void addUserLikeId(String userLikeId) {
		this.userLikes.add(userLikeId);
	}

	public void removeUserLikeId(String userLikeId) {
		this.userLikes.remove(userLikeId);
	}

	public static Builder Builder() {
		return new Builder();
	}

	@Override
	public int hashCode() {
		return Objects.hash(opinionId);
	}

	@Override
	public int compareTo(@NonNull Opinion o) {

		return Long.compare(o.dataTime, dataTime);

	}

	public static class Builder {
		private String opinionId;
		private String urlOpinionImage;
		private String content;
		private long dataTime;
		private String userId;
		private String userName;
		private String urlPhotoProfile;
		private String urlPoliticalFlag;
		private List<String> userLikes;
		private List<String> userComments;
		private List<String> userShares;

		private Builder() {
		}

		public Builder opinionId(String opinionId) {
			this.opinionId = opinionId;
			return this;
		}

		public Builder urlOpinionImage(String urlOpinionImage) {
			this.urlOpinionImage = urlOpinionImage;
			return this;
		}

		public Builder content(String content) {
			this.content = content;
			return this;
		}

		public Builder dataTime(long dataTime) {
			this.dataTime = dataTime;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder userName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder urlPhotoProfile(String urlPhotoProfile) {
			this.urlPhotoProfile = urlPhotoProfile;
			return this;
		}

		public Builder urlPoliticalFlag(String urlPoliticalFlag) {
			this.urlPoliticalFlag = urlPoliticalFlag;
			return this;
		}

		public Builder userLikes(List<String> userLikes) {
			this.userLikes = userLikes;
			return this;
		}

		public Builder userComments(List<String> userComments) {
			this.userComments = userComments;
			return this;
		}

		public Builder userShares(List<String> userShares) {
			this.userShares = userShares;
			return this;
		}

		public Opinion build() {
			return new Opinion(this);
		}
	}
}
