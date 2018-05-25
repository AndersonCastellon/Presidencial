package com.papaprogramador.presidenciales.Vista.Fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.papaprogramador.presidenciales.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpinionesFragment extends Fragment {


	public OpinionesFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_opiniones, container, false);

		

		return view;
	}

}
