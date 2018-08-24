package com.papaprogramador.presidenciales.TreeMvp.Opinions;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.papaprogramador.presidenciales.Adapters.OpinionsAdapter;
import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.TreeMvp.NewOpinion.NewOpinionView;
import com.papaprogramador.presidenciales.Utils.Constans;

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
	//TODO: Retornar a el tab de las opiniones al publicar o cancelar una opinión MainView -> COMPLETADO
	//TODO: Resolver java.lang.OutOfMemoryError: Failed to allocate a 31961100 byte allocation with 16776768 free bytes and 23MB until OOM
	//TODO: Implementar paginación para no sobrecargar la memoria -> EN PROCESO


	//TODO: Listener al dar clic en la imagen cargada en la opinión
	//TODO: Botón flotante que notifique que hay nuevas opiniones
	//TODO: Juntar más el texto y los iconos de los botones inferiores
	//TODO: Botón flotante debe ocultarse al hacer scroll hacia abajo
	//TODO: Botón mostrar más en textos largos
	//TODO: borde y separación entre opiniones
	//TODO: Corregir error que poner el correo como nombre de usuario


	@BindView(R.id.rv_opinions)
	RecyclerView rvOpinions;

	@BindView(R.id.contentView)
	SwipeRefreshLayout contentView;

	Unbinder unbinder;
	OpinionsAdapter opinionsAdapter;

	private LinearLayoutManager layoutManager;
	private int totalItemCount = 0;
	private int lastVisibleItemPosition;
	private boolean mIsLoading;

	public OpinionsView() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		setRetainInstance(true);
		View view = inflater.inflate(R.layout.opinions_fragment, container, false);
		unbinder = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		unbinder = ButterKnife.bind(this, view);
		contentView.setOnRefreshListener(this);

		opinionsAdapter = new OpinionsAdapter();
		rvOpinions.setAdapter(opinionsAdapter);

		layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		rvOpinions.setLayoutManager(layoutManager);

		contentView.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvOpinions.getContext(),
				layoutManager.getOrientation());
		rvOpinions.addItemDecoration(dividerItemDecoration);

		rvOpinions.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				totalItemCount = layoutManager.getItemCount();
				lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

				if (!mIsLoading && totalItemCount <= (lastVisibleItemPosition + Constans.OPINIONS_PER_PAGE)) {
					getPresenter().getOpinions(opinionsAdapter.getLastItemId(), false);
					mIsLoading = true;
				}
			}
		});

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
		contentView.setRefreshing(false);
		mIsLoading = false;
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

		mIsLoading = false;
		contentView.setRefreshing(false);

	}

	@Override
	public void loadData(boolean pullToRefresh) {
		errorView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);

		getPresenter().getOpinions(opinionsAdapter.getLastItemId(), pullToRefresh);
	}

	@Override
	public void onRefresh() {
		errorView.setVisibility(View.GONE);
		showLoading(true);
		loadData(true);
	}

	@Override
	public void setRefreshing() {
		contentView.setRefreshing(false);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
