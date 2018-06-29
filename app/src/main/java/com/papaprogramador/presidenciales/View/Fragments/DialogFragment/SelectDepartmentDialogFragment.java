package com.papaprogramador.presidenciales.View.Fragments.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dd.processbutton.iml.ActionProcessButton;
import com.papaprogramador.presidenciales.InterfacesMVP.SelectedDepartmentDialog;
import com.papaprogramador.presidenciales.Presenters.SelectedDepartmentPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.Utils.MvpDialogFragment.MvpDialogFragment;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SelectDepartmentDialogFragment extends MvpDialogFragment<SelectedDepartmentDialog.View, SelectedDepartmentDialog.Presenter>
		implements SelectedDepartmentDialog.View {

	@BindView(R.id.mSpinnerDepartment)
	NiceSpinner mSpinnerDepartment;

	@BindView(R.id.actionButtonSetDepartment)
	ActionProcessButton actionButtonSetDepartment;

	Unbinder unbinder;

	public static SelectDepartmentDialogFragment newInstance(Bundle arguments) {

		SelectDepartmentDialogFragment selectDepartmentDialogFragment = new SelectDepartmentDialogFragment();

		if (arguments != null) {
			selectDepartmentDialogFragment.setArguments(arguments);
		}

		return selectDepartmentDialogFragment;
	}

	public SelectDepartmentDialogFragment() {
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.department_dialog_fragment, null);

		actionButtonSetDepartment.setMode(ActionProcessButton.Mode.ENDLESS);

		List<String> listDepartment = new LinkedList<>(Arrays.asList(Constans.DEPARTAMENTO));
		mSpinnerDepartment.attachDataSource(listDepartment);

		return new AlertDialog.Builder(getActivity()).setView(viewGroup).create();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = super.onCreateView(inflater, container, savedInstanceState);

		unbinder = ButterKnife.bind(this, rootView);

		return rootView;
	}

	@Override
	public SelectedDepartmentDialog.Presenter createPresenter() {
		return new SelectedDepartmentPresenter();
	}

	@OnClick(R.id.actionButtonSetDepartment)
	public void onViewClicked() {

		actionButtonSetDepartment.setProgress(1);

		String uidUser = getArguments().getString(Constans.PUT_UID_USER);
		String department = mSpinnerDepartment.getText().toString();

		getPresenter().setDepartmentUserIntoFirebase(uidUser, department);
	}

	@Override
	public void SelectDepartmentPlease() {
		mSpinnerDepartment.setError(getString(R.string.selectDepartment));
	}

	@Override
	public void TaskIsSuccesful() {
		actionButtonSetDepartment.setProgress(0);
		dismiss();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
