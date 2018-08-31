package com.papaprogramador.presidenciales.common.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.Objects;

public class Opinion implements Parcelable, Comparable<Opinion> {
	public static final String ORDER_BY = "orderBy";

	@Exclude
	private String opinionId;
	private String userId;
	private String userName;
	private String urlPhotoProfile;
	private String datePublication;
	private String urlPoliticalFlag;
	private String opinionText;
	private String urlOpinionImage;
	private int countLike;
	private int countComments;
	private int countShare;
	private boolean likeClicked;

	private long orderBy;

	public Opinion() {
	}

	private Opinion(Parcel source) {
		this.opinionId = source.readString();
		this.userId = source.readString();
		this.userName = source.readString();
		this.urlPhotoProfile = source.readString();
		this.datePublication = source.readString();
		this.urlPoliticalFlag = source.readString();
		this.opinionText = source.readString();
		this.urlOpinionImage = source.readString();
		this.countLike = source.readInt();
		this.countComments = source.readInt();
		this.countShare = source.readInt();
		this.likeClicked = source.readByte() == 1;
		this.orderBy = source.readLong();

	}

	public Opinion(String userId, String userName, String urlPhotoProfile, String datePublication,
	               String urlPoliticalFlag, String opinionText, String urlOpinionImage, int countLike,
	               int countComments, int countShare, long orderBy) {
		this.userId = userId;
		this.userName = userName;
		this.urlPhotoProfile = urlPhotoProfile;
		this.datePublication = datePublication;
		this.urlPoliticalFlag = urlPoliticalFlag;
		this.opinionText = opinionText;
		this.urlOpinionImage = urlOpinionImage;
		this.countLike = countLike;
		this.countComments = countComments;
		this.countShare = countShare;
		this.orderBy = orderBy;
	}

	public long getOrderBy() {
		return orderBy;
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

	public String getDatePublication() {
		return datePublication;
	}

	public String getUrlPoliticalFlag() {
		return urlPoliticalFlag;
	}

	public String getOpinionText() {
		return opinionText;
	}

	public String getUrlOpinionImage() {
		return urlOpinionImage;
	}

	public int getCountLike() {
		return countLike;
	}

	public int getCountComments() {
		return countComments;
	}

	public int getCountShare() {
		return countShare;
	}

	public boolean isLikeClicked() {
		return likeClicked;
	}

	public void setLikeClicked(boolean likeClicked) {
		this.likeClicked = likeClicked;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Opinion opinion = (Opinion) o;
		return countLike == opinion.countLike &&
				countComments == opinion.countComments &&
				countShare == opinion.countShare &&
				likeClicked == opinion.likeClicked &&
				orderBy == opinion.orderBy &&
				Objects.equals(datePublication, opinion.datePublication) &&
				Objects.equals(opinionId, opinion.opinionId) &&
				Objects.equals(userId, opinion.userId) &&
				Objects.equals(userName, opinion.userName) &&
				Objects.equals(urlPhotoProfile, opinion.urlPhotoProfile) &&
				Objects.equals(urlPoliticalFlag, opinion.urlPoliticalFlag) &&
				Objects.equals(opinionText, opinion.opinionText) &&
				Objects.equals(urlOpinionImage, opinion.urlOpinionImage);
	}

	@Override
	public int hashCode() {
		return Objects.hash(opinionId);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(opinionId);
		dest.writeString(userId);
		dest.writeString(userName);
		dest.writeString(urlPhotoProfile);
		dest.writeString(datePublication);
		dest.writeString(urlPoliticalFlag);
		dest.writeString(opinionText);
		dest.writeString(urlOpinionImage);
		dest.writeInt(countLike);
		dest.writeInt(countComments);
		dest.writeInt(countShare);
		dest.writeLong(orderBy);

	}

	public static final Parcelable.Creator<Opinion> CREATOR = new Parcelable.Creator<Opinion>() {

		@Override
		public Opinion createFromParcel(Parcel source) {
			return new Opinion(source);
		}

		@Override
		public Opinion[] newArray(int size) {
			return new Opinion[size];
		}
	};

	@Override
	public int compareTo(@NonNull Opinion o) {

		return Long.compare(o.orderBy,orderBy);

	}
}
