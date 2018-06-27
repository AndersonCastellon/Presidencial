package com.papaprogramador.presidenciales.View.Fragments.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
implements SelectedDepartmentDialog.View{

	@BindView(R.id.mSpinnerDepartment)
	NiceSpinner mSpinnerDepartment;

	Unbinder unbinder;

	public static SelectDepartmentDialogFragment newInstance(String uidUser) {

		SelectDepartmentDialogFragment selectDepartmentDialogFragment = new SelectDepartmentDialogFragment();

		Bundle bundle = new Bundle();
		bundle.putString(Constans.PUT_UID_USER, uidUser);

		selectDepartmentDialogFragment.setArguments(bundle);

		return selectDepartmentDialogFragment;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.department_dialog_fragment, null);

		return new AlertDialog.Builder(getActivity()).setView(viewGroup).create();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = super.onCreateView(inflater, container, savedInstanceState);

		unbinder = ButterKnife.bind(this, rootView);

		List<String> listDepartment = new LinkedList<>(Arrays.asList(Constans.DEPARTAMENTO));
		mSpinnerDepartment.attachDataSource(listDepartment);

		return rootView;
	}

	@Override
	public SelectedDepartmentDialog.Presenter createPresenter() {
		return new SelectedDepartmentPresenter();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@OnClick(R.id.actionButtonSetDepartment)
	public void onViewClicked() {
	}
}
