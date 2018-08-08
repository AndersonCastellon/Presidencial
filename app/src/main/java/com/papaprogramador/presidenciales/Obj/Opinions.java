package com.papaprogramador.presidenciales.Obj;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Opinions implements Parcelable {

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

	public Opinions() {
	}

	private Opinions(Parcel source) {
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
	}

	public Opinions(String userId, String userName, String urlPhotoProfile, String datePublication,
	                String urlPoliticalFlag, String opinionText, String urlOpinionImage, int countLike,
	                int countComments, int countShare) {
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
	}

	public String getOpinionId() {
		return opinionId;
	}

	public void setOpinionId(String opinionId) {
		this.opinionId = opinionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUrlPhotoProfile() {
		return urlPhotoProfile;
	}

	public void setUrlPhotoProfile(String urlPhotoProfile) {
		this.urlPhotoProfile = urlPhotoProfile;
	}

	public String getDatePublication() {
		return datePublication;
	}

	public void setDatePublication(String datePublication) {
		this.datePublication = datePublication;
	}

	public String getUrlPoliticalFlag() {
		return urlPoliticalFlag;
	}

	public void setUrlPoliticalFlag(String urlPoliticalFlag) {
		this.urlPoliticalFlag = urlPoliticalFlag;
	}

	public String getOpinionText() {
		return opinionText;
	}

	public void setOpinionText(String opinionText) {
		this.opinionText = opinionText;
	}

	public String getUrlOpinionImage() {
		return urlOpinionImage;
	}

	public void setUrlOpinionImage(String urlOpinionImage) {
		this.urlOpinionImage = urlOpinionImage;
	}

	public int getCountLike() {
		return countLike;
	}

	public void setCountLike(int countLike) {
		this.countLike = countLike;
	}

	public int getCountComments() {
		return countComments;
	}

	public void setCountComments(int countComments) {
		this.countComments = countComments;
	}

	public int getCountShare() {
		return countShare;
	}

	public void setCountShare(int countShare) {
		this.countShare = countShare;
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
		Opinions opinions = (Opinions) o;
		return countLike == opinions.countLike &&
				countComments == opinions.countComments &&
				countShare == opinions.countShare &&
				likeClicked == opinions.likeClicked &&
				Objects.equals(datePublication, opinions.datePublication) &&
				Objects.equals(opinionId, opinions.opinionId) &&
				Objects.equals(userId, opinions.userId) &&
				Objects.equals(userName, opinions.userName) &&
				Objects.equals(urlPhotoProfile, opinions.urlPhotoProfile) &&
				Objects.equals(urlPoliticalFlag, opinions.urlPoliticalFlag) &&
				Objects.equals(opinionText, opinions.opinionText) &&
				Objects.equals(urlOpinionImage, opinions.urlOpinionImage);
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
	}

	public static final Parcelable.Creator<Opinions> CREATOR = new Parcelable.Creator<Opinions>() {

		@Override
		public Opinions createFromParcel(Parcel source) {
			return new Opinions(source);
		}

		@Override
		public Opinions[] newArray(int size) {
			return new Opinions[size];
		}
	};
}
