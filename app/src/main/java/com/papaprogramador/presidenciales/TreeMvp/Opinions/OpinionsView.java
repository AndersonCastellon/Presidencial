package com.papaprogramador.presidenciales.TreeMvp.Opinions;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.TreeMvp.NewOpinion.NewOpinionView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpinionsView extends Fragment {


	private static final int RC_UPLOAD_OPINION = 255;
	@BindView(R.id.loadingView)
	ProgressBar loadingView;
	@BindView(R.id.errorView)
	TextView errorView;
	@BindView(R.id.rv_opinions)
	RecyclerView rvOpinions;
	@BindView(R.id.contentView)
	SwipeRefreshLayout contentView;
	Unbinder unbinder;

	public OpinionsView() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.opinions_fragment, container, false);
		unbinder = ButterKnife.bind(this, view);
		return view;
	}

	@OnClick(R.id.fab_new_opinion)
	public void onViewClicked() {
		Intent intent = new Intent(getActivity(), NewOpinionView.class);
		startActivityForResult(intent, RC_UPLOAD_OPINION);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
