package com.papaprogramador.presidenciales.Presenters;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.papaprogramador.presidenciales.InterfacesMVP.SelectedDepartmentDialog;
import com.papaprogramador.presidenciales.Utils.StaticMethods.SetIntoFirebaseDatabase;

public class SelectedDepartmentPresenter extends MvpBasePresenter<SelectedDepartmentDialog.View>
implements SelectedDepartmentDialog.Presenter{

	public SelectedDepartmentPresenter() {
	}


	@Override
	public void setDepartmentUserIntoFirebase(String uidUser, String department) {
		if (department.isEmpty()){
			ifViewAttached(new ViewAction<SelectedDepartmentDialog.View>() {
				@Override
				public void run(@NonNull SelectedDepartmentDialog.View view) {
					view.SelectDepartmentPlease();
				}
			});
		} else {
			SetIntoFirebaseDatabase.setDepartmentUser(uidUser, department);
		}
	}
}
