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
import android.widget.Toast;

import com.papaprogramador.presidenciales.InterfacesMVP.SelectedDepartmentDialog;
import com.papaprogramador.presidenciales.Presenters.SelectedDepartmentPresenter;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.Utils.MvpDialogFragment.MvpDialogFragment;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SelectDepartmentDialogFragment extends MvpDialogFragment<SelectedDepartmentDialog.View, SelectedDepartmentDialog.Presenter>
implements SelectedDepartmentDialog.View{

	@BindView(R.id.mSpinnerDepartment)
	NiceSpinner mSpinnerDepartment;

	Unbinder unbinder;

	public static SelectDepartmentDialogFragment newInstance(Bundle arguments) {

		SelectDepartmentDialogFragment selectDepartmentDialogFragment = new SelectDepartmentDialogFragment();

		if (arguments != null){
			selectDepartmentDialogFragment.setArguments(arguments);
		}

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
		super.onCreateView(inflater, container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.department_dialog_fragment, container, false);

		unbinder = ButterKnife.bind(this, rootView);

		List<String> listDepartment = new LinkedList<>(Arrays.asList(Constans.DEPARTAMENTO));
		mSpinnerDepartment.attachDataSource(listDepartment);

		return rootView;
	}

	@Override
	public SelectedDepartmentDialog.Presenter createPresenter() {
		return new SelectedDepartmentPresenter();
	}

	@OnClick(R.id.actionButtonSetDepartment)
	public void onViewClicked() {

		String uidUser = getArguments().getString(Constans.PUT_UID_USER);
		String department = mSpinnerDepartment.getText().toString();

		getPresenter().setDepartmentUserIntoFirebase(uidUser, department);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@Override
	public void SelectDepartmentPlease() {
		mSpinnerDepartment.setError(getString(R.string.selectDepartment));
	}
}
