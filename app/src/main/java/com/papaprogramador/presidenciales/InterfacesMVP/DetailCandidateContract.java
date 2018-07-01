package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface DetailCandidateContract {
	interface View extends MvpView {
		void onWebViewSettings(String urlHtmlCandidate);
		void getImgCandidate(String urlImgCandidate);
		void getToolbar(String nameCandidate);
		void existingVote();
		void goSelectDepartmentDialogFragment(String uidUser);
		void applyNewVoteIsSuccesful();
		void showProgressFab(boolean show);


	}

	interface Presenter extends MvpPresenter<DetailCandidateContract.View> {
		void fabShare();
		void goCurrentVote();
		void getDepartment();
		void applyVotes();
		void setAuthListener();
		void removeAuthListener();
	}
}
