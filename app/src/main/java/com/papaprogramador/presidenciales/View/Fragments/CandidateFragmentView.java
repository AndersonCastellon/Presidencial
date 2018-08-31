package com.papaprogramador.presidenciales.View.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.papaprogramador.presidenciales.Adapters.CandidateAdapter;
import com.papaprogramador.presidenciales.InterfacesMVP.CandidateFragment;
import com.papaprogramador.presidenciales.common.pojo.Candidate;
import com.papaprogramador.presidenciales.Presenters.ListCandidateFragmentPresenter;
import com.papaprogramador.presidenciales.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CandidateFragmentView extends MvpLceViewStateFragment<SwipeRefreshLayout, List<Candidate>,
		CandidateFragment.View, CandidateFragment.Presenter>
		implements CandidateFragment.View, SwipeRefreshLayout.OnRefreshListener {

	CandidateAdapter candidateAdapter;

	@BindView(R.id.rv_ListCandidatos_CandidatosFragment)
	RecyclerView recyclerViewCandidate;

	Unbinder unbinder;

	public CandidateFragmentView() {
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		setRetainInstance(true);
		return inflater.inflate(R.layout.fragment_candidatos, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		unbinder = ButterKnife.bind(this, view);

		contentView.setOnRefreshListener(this);

		candidateAdapter = new CandidateAdapter();

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerViewCandidate.setLayoutManager(linearLayoutManager);

		loadData(false);
	}

	@NonNull
	@Override
	public CandidateFragment.Presenter createPresenter() {
		return new ListCandidateFragmentPresenter(getActivity());
	}

	@NonNull
	@Override
	public LceViewState<List<Candidate>, CandidateFragment.View> createViewState() {
		return new RetainingLceViewState<>();
	}

	@Override
	public void loadData(boolean pullToRefresh) {
		errorView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);

		getPresenter().getListCandidateFromFirebase(pullToRefresh);
	}


	@Override
	public void setData(List<Candidate> data) {

		errorView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);

		candidateAdapter.setCandidateList(data);

		recyclerViewCandidate.setAdapter(candidateAdapter);
		candidateAdapter.notifyDataSetChanged();

		contentView.setRefreshing(false);

	}

	@Override
	public List<Candidate> getData() {
		return candidateAdapter == null ? null : candidateAdapter.getCandidateList();
	}

	@Override
	protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
		return getResources().getString(R.string.null_Connection_Exception);
	}


	@Override
	public void onRefresh() {
		errorView.setVisibility(View.GONE);
		loadData(true);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
