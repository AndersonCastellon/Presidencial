package com.papaprogramador.presidenciales.View.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.papaprogramador.presidenciales.R;

public class SuggestionsAndErrorsFragment extends Fragment {


	public SuggestionsAndErrorsFragment() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_suggestions_and_errors, container, false);

		return view;
	}

}
