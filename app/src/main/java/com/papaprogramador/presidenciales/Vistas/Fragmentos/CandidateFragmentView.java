package com.papaprogramador.presidenciales.Vistas.Fragmentos;


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
import com.papaprogramador.presidenciales.Adaptadores.CandidatoAdapter;
import com.papaprogramador.presidenciales.InterfacesMVP.CandidateFragment;
import com.papaprogramador.presidenciales.Objetos.Candidato;
import com.papaprogramador.presidenciales.Presenters.ListCandidateFragmentPresenter;
import com.papaprogramador.presidenciales.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CandidateFragmentView extends MvpLceViewStateFragment<SwipeRefreshLayout, List<Candidato>,
		CandidateFragment.View, CandidateFragment.Presenter>
		implements CandidateFragment.View, SwipeRefreshLayout.OnRefreshListener {

	CandidatoAdapter candidatoAdapter;

	@BindView(R.id.rv_ListCandidatos_CandidatosFragment)
	RecyclerView recyclerViewCandidate;

	Unbinder unbinder;

//TODO: Falta comprobación cuando no hay conexion a internet para obtener datos de firebase
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

		candidatoAdapter = new CandidatoAdapter();

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
	public LceViewState<List<Candidato>, CandidateFragment.View> createViewState() {
		return new RetainingLceViewState<>();
	}

	@Override
	public void loadData(boolean pullToRefresh) {
		errorView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);

		getPresenter().getListCandidateFromFirebase(pullToRefresh);
	}


	@Override
	public void setData(List<Candidato> data) {

		candidatoAdapter.setCandidatoList(data);

		recyclerViewCandidate.setAdapter(candidatoAdapter);
		candidatoAdapter.notifyDataSetChanged();

		contentView.setRefreshing(false);

		errorView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
	}

	@Override
	public List<Candidato> getData() {
		return candidatoAdapter == null ? null : candidatoAdapter.getCandidatoList();
	}

	@Override
	protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
		return e.toString();
	}


	@Override
	public void onRefresh() {
		loadData(true);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
