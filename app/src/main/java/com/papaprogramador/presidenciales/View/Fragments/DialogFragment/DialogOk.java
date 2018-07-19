package com.papaprogramador.presidenciales.View.Fragments.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;

import java.util.Objects;

public class DialogOk extends DialogFragment {

	private DialogOkListener listener;
	private String message = null;
	private int requestCode;


	public DialogOk() {
	}

	public interface DialogOkListener {
		void onResultDialogOk(int requestCode);
	}

	public static DialogOk newInstance(Bundle arguments) {

		DialogOk dialogOk = new DialogOk();

		if (arguments != null) {
			dialogOk.setArguments(arguments);
		}
		return dialogOk;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return updatePasswordIsSuccessful();
	}

	public AlertDialog updatePasswordIsSuccessful() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		View v = inflater.inflate(R.layout.dialog_ok, null);

		builder.setView(v);

		if (getArguments() != null) {
			message = getArguments().getString(Constans.PUT_DIALOG_OK_MESSAGE);
			requestCode = getArguments().getInt(Constans.PUT_DIALOG_OK_REQUEST_CODE);
		}

		TextView textViewMessage = v.findViewById(R.id.tv_dialog_ok);
		textViewMessage.setText(message);

		Button btnOk = v.findViewById(R.id.btn_dialog_ok);
		Button btnCancel = v.findViewById(R.id.btn_dialog_cancel);

		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onResultDialogOk(requestCode);
				dismiss();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		return builder.create();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		try {
			listener = (DialogOkListener) context;
		} catch (ClassCastException e) {

			throw new ClassCastException(
					context.toString() +
							getString(R.string.interface_no_implement));
		}
	}
}
