package com.papaprogramador.presidenciales.TreeMvp.Opinions;


import android.content.Intent;
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
import com.papaprogramador.presidenciales.Adapters.OpinionsAdapter;
import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.TreeMvp.NewOpinion.NewOpinionView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OpinionsView extends MvpLceViewStateFragment<SwipeRefreshLayout, List<Opinions>,
		OpinionsContract.View, OpinionsContract.Presenter> implements OpinionsContract.View,
		SwipeRefreshLayout.OnRefreshListener {

	//TODO: Ordenar las opiniones, las más recientes arriba -> COMPLETADO.
	//TODO: Utilizar RxFirebase para usar hilos de fondo en la obtención de la lista de opiniones -> COMPLETADO
	//TODO: Resolver porqué la app se vuelve lenta al haber muchas opiniones -> COMPLETADO

	//TODO: Implementar paginación para no sobrecargar la memoria
	//TODO: Resolver java.lang.OutOfMemoryError: Failed to allocate a 31961100 byte allocation with 16776768 free bytes and 23MB until OOM
	//TODO: borde y separación entre opiniones
	//TODO: Botón mostrar más en textos largos
	//TODO: Juntar más el texto y los iconos de los botones inferiores
	//TODO: Botón flotante que notifique que hay nuevas opiniones
	//TODO: Listener al dar clic en la imagen cargada en la opinión

	@BindView(R.id.rv_opinions)
	RecyclerView rvOpinions;
	Unbinder unbinder;

	OpinionsAdapter opinionsAdapter;

	public OpinionsView() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		setRetainInstance(true);
		return inflater.inflate(R.layout.opinions_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		unbinder = ButterKnife.bind(this, view);
		contentView.setOnRefreshListener(this);
		opinionsAdapter = new OpinionsAdapter();

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		rvOpinions.setLayoutManager(linearLayoutManager);

		rvOpinions.setHasFixedSize(true);
		rvOpinions.setItemViewCacheSize(20);
		rvOpinions.setDrawingCacheEnabled(true);

		loadData(false);
	}

	@OnClick(R.id.fab_new_opinion)
	public void onViewClicked() {
		Intent intent = new Intent(getActivity(), NewOpinionView.class);
		startActivity(intent);
	}

	@NonNull
	@Override
	public OpinionsContract.Presenter createPresenter() {
		return new OpinionsPresenter(getActivity());
	}

	@Override
	protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
		loadData(pullToRefresh);
		return getResources().getString(R.string.null_Connection_Exception);
	}

	@Override
	public List<Opinions> getData() {
		return opinionsAdapter == null ? null : opinionsAdapter.getOpinionsList();
	}

	@NonNull
	@Override
	public LceViewState<List<Opinions>, OpinionsContract.View> createViewState() {
		return new RetainingLceViewState<>();
	}

	@Override
	public void setData(List<Opinions> data) {

		errorView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);

		opinionsAdapter.setOpinionsList(data);

		rvOpinions.setAdapter(opinionsAdapter);
		opinionsAdapter.notifyDataSetChanged();

		contentView.setRefreshing(false);

	}

	@Override
	public void loadData(boolean pullToRefresh) {
		errorView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);

		getPresenter().getOpinionsList(pullToRefresh);
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
