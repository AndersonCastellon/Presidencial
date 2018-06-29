package com.papaprogramador.presidenciales.InterfacesMVP;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface SelectedDepartmentDialog {

	interface View extends MvpView {
		void SelectDepartmentPlease();
		void TaskIsSuccesful();
	}

	interface Presenter extends MvpPresenter<SelectedDepartmentDialog.View> {
		void setDepartmentUserIntoFirebase(String uidUser, String department);
	}
}
