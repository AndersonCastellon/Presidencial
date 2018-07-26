package com.papaprogramador.presidenciales.TreeMvp.Opinions;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.papaprogramador.presidenciales.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpinionsView extends Fragment {


	public OpinionsView() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.opinions_fragment, container, false);

		

		return view;
	}

}
