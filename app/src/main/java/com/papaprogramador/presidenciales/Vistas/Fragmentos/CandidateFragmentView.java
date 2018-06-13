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


	public CandidateFragmentView() {
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_candidatos, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		unbinder = ButterKnife.bind(this, view);

		contentView.setOnRefreshListener(this);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerViewCandidate.setLayoutManager(linearLayoutManager);

		candidatoAdapter = new CandidatoAdapter();
		recyclerViewCandidate.setAdapter(candidatoAdapter);

		loadData(false);
	}

	@NonNull
	@Override
	public CandidateFragment.Presenter createPresenter() {
		return new ListCandidateFragmentPresenter();
	}

	@NonNull
	@Override
	public LceViewState<List<Candidato>, CandidateFragment.View> createViewState() {
		return null;
	}

	@Override
	public void setData(List<Candidato> data) {
		candidatoAdapter.setCandidatoList(data);
		candidatoAdapter.notifyDataSetChanged();
	}

	@Override
	public List<Candidato> getData() {
		return null;
	}

	@Override
	public void loadData(boolean pullToRefresh) {
		getPresenter().getDataSnapshotCandidatosFirebase();
	}

	@Override
	protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
		return null;
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
