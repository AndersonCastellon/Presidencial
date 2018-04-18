package com.papaprogramador.presidenciales2019.ui.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.papaprogramador.presidenciales2019.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CandidatosFragment extends Fragment {


	public CandidatosFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_candidatos, container, false);


		
		return view;
	}

}
