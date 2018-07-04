package com.papaprogramador.presidenciales.InterfacesMVP;

import android.content.Context;
import android.service.voice.VoiceInteractionService;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface DetailCandidateContract {
	interface View extends MvpView {
		void onWebViewSettings(String urlHtmlCandidate);
		void getImgCandidate(String urlImgCandidate);
		void getToolbar(String nameCandidate);
		void shareCandidate(String shareCandidateString, String urlImgCandidate);
		void existingVote();
		void goSelectDepartmentDialogFragment(String uidUser);
		void applyNewVoteIsSuccesful();
		void showProgressFab(boolean show);


	}

	interface Presenter extends MvpPresenter<DetailCandidateContract.View> {
		void fabShare(Context context);
		void goCurrentVote();
		void getDepartment();
		void applyVotes();
		void setAuthListener();
		void removeAuthListener();
	}
}
