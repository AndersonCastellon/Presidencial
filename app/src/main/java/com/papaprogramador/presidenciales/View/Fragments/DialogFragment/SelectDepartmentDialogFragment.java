package com.papaprogramador.presidenciales.View.Fragments.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.Utils.StaticMethods.SetIntoFirebaseDatabase;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SelectDepartmentDialogFragment extends android.support.v4.app.DialogFragment {

	private NiceSpinner mSpinnerDepartment;
	private DialogFragmentSelectedDepartmentListener listener = null;

	public interface DialogFragmentSelectedDepartmentListener {
		void onResult(boolean result);
	}

	public SelectDepartmentDialogFragment() {
	}

	public static SelectDepartmentDialogFragment newInstance(Bundle arguments) {

		SelectDepartmentDialogFragment selectDepartmentDialogFragment = new SelectDepartmentDialogFragment();

		if (arguments != null) {
			selectDepartmentDialogFragment.setArguments(arguments);
		}

		return selectDepartmentDialogFragment;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.department_dialog_fragment, null);

		mSpinnerDepartment = viewGroup.findViewById(R.id.mSpinner);
		Button actionButtonSetDepartment = viewGroup.findViewById(R.id.btnSetDepartment);

		List<String> listDepartment = new LinkedList<>(Arrays.asList(Constans.DEPARTAMENTO));
		mSpinnerDepartment.attachDataSource(listDepartment);

		actionButtonSetDepartment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String uidUser = getArguments().getString(Constans.PUT_UID_USER);
				String department = mSpinnerDepartment.getText().toString();

				setDepartmentUserIntoFirebase(uidUser, department);
			}
		});

		return new AlertDialog.Builder(getActivity()).setView(viewGroup).create();
	}

	public void setDepartmentUserIntoFirebase(String uidUser, String department) {

		if (department.isEmpty()) {
			SelectDepartmentPlease();
		} else {
			if (SetIntoFirebaseDatabase.setDepartmentUser(uidUser, department)) {
				listener.onResult(true);
				TaskIsSuccesful();
			}
		}
	}

	public void SelectDepartmentPlease() {
		mSpinnerDepartment.setError(getString(R.string.selectDepartment));
	}

	public void TaskIsSuccesful() {
		dismiss();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		try {

			listener = (DialogFragmentSelectedDepartmentListener) context;

		} catch (ClassCastException e) {

			throw new ClassCastException(
					context.toString() +
							getString(R.string.interface_no_implement));

		}
	}
}
