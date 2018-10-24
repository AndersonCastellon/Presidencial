package com.papaprogramador.presidenciales.opinionsModule.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.Views.NewOpinion.NewOpinionView;
import com.papaprogramador.presidenciales.commentsModule.view.CommentsViewClass;
import com.papaprogramador.presidenciales.common.pojo.Opinion;
import com.papaprogramador.presidenciales.opinionsModule.OpinionsContract;
import com.papaprogramador.presidenciales.opinionsModule.presenter.OpinionsPresenter;
import com.papaprogramador.presidenciales.opinionsModule.view.Adapters.OnItemClickListener;
import com.papaprogramador.presidenciales.opinionsModule.view.Adapters.OpinionsAdapter;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class OpinionsView extends MvpFragment<OpinionsContract.View, OpinionsContract.Presenter> implements OpinionsContract.View,
		SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

	//TODO: Ordenar las opiniones, las más recientes arriba -> COMPLETADO.
	//TODO: Utilizar RxFirebase para usar hilos de fondo en la obtención de la lista de opiniones -> COMPLETADO
	//TODO: Resolver porqué la app se vuelve lenta al haber muchas opiniones -> COMPLETADO
	//TODO: Retornar a el tab de las opiniones al publicar o cancelar una opinión MainView -> COMPLETADO
	//TODO: Resolver java.lang.OutOfMemoryError: Failed to allocate a 31961100 byte allocation with 16776768 free bytes and 23MB until OOM -> SOLUCIONADO
	//TODO: Implementar paginación para no sobrecargar la memoria -> EN PROCESO
	//TODO: Listener al dar clic en la imagen cargada en la opinión -> COMPLETADO
	//TODO: Botón flotante debe ocultarse al hacer scroll hacia abajo -> EN PROCESO


	//TODO: Al eliminar una opinión, algunas del listado permiten ser eliminadas también aunque no sean del usuario actual
	//TODO: Botón flotante que notifique que hay nuevas opiniones
	//TODO: Botón mostrar más en textos largos
	//TODO: borde y separación entre opiniones


	@BindView(R.id.rv_opinions)
	RecyclerView rvOpinions;
	@BindView(R.id.contentView)
	SwipeRefreshLayout contentView;
	@BindView(R.id.loadingView)
	ProgressBar loadingView;

	Unbinder unbinder;
	OpinionsAdapter opinionsAdapter;

	private LinearLayoutManager layoutManager;
	private int totalItemCount;
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
		getPresenter().onCreate();
		setRetainInstance(true);
		unbinder = ButterKnife.bind(this, view);
		setContentView();
		setRecyclerView();
		getPresenter().getData(opinionsAdapter.getLastItemId());

		rvOpinions.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				totalItemCount = layoutManager.getItemCount();
				lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

				if (!mIsLoading && totalItemCount <= (lastVisibleItemPosition + 1)) {
					requestRemoveLikeNotifiers();
					getPresenter().getData(opinionsAdapter.getLastItemId());
					requestAddLikeNotifiers();
					mIsLoading = true;
				}
			}
		});

	}

	private void setRecyclerView() {
		rvOpinions.setItemAnimator(new SlideInLeftAnimator());
		opinionsAdapter = new OpinionsAdapter(getActivity(), this);
		rvOpinions.setAdapter(opinionsAdapter);

		layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		rvOpinions.setLayoutManager(layoutManager);
	}

	private void setContentView() {
		contentView.setOnRefreshListener(this);
		contentView.setColorSchemeResources(R.color.google_blue, R.color.google_green,
				R.color.google_red, R.color.google_yellow);
	}

	@OnClick(R.id.fab_new_opinion)
	public void onNewOpinionClicked() {
		Intent intent = new Intent(getActivity(), NewOpinionView.class);
		startActivity(intent);
	}

	@NonNull
	@Override
	public OpinionsContract.Presenter createPresenter() {
		return new OpinionsPresenter();
	}

	@Override
	public void onRefresh() {
		requestRemoveLikeNotifiers();
		getPresenter().getData(opinionsAdapter.getLastItemId());
		requestAddLikeNotifiers();
		onComplete();
	}

	@Override
	public void showProgress(boolean show) {
		if (show) {
			contentView.setVisibility(View.GONE);
			loadingView.setVisibility(View.VISIBLE);
		} else {
			contentView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
		}
	}

	@Override
	public void addAll(List<Opinion> opinions) {
		opinionsAdapter.addAll(opinions);
	}

	@Override
	public void add(Opinion opinion) {
		opinionsAdapter.add(opinion);
	}

	@Override
	public void update(Opinion opinion) {
		opinionsAdapter.update(opinion);
	}

	@Override
	public void remove(Opinion opinion) {
		opinionsAdapter.remove(opinion);
	}

	@Override
	public void updateOpinionLikeCounter(String opinionId, String userId, boolean result) {
		opinionsAdapter.updateOpinionLikeCounter(opinionId, userId, result);
	}

	@Override
	public void onComplete() {
		contentView.setRefreshing(false);
		mIsLoading = false;
	}

	@Override
	public void requestAddOpinionNotifiers() {
		getPresenter().addOpinionNotifiers();
	}

	@Override
	public void requestRemoveOpinionNotifiers() {
		getPresenter().removeOpinionNotifiers();
	}

	@Override
	public void requestAddLikeNotifiers() {
		List<Opinion> opinions = opinionsAdapter.getItems();
		if (opinions != null) {
			for (Opinion opinion : opinions) {
				getPresenter().addLikeNotifiers(opinion.getOpinionId());
			}
		}
	}

	@Override
	public void requestRemoveLikeNotifiers() {
		List<Opinion> opinions = opinionsAdapter.getItems();
		if (opinions != null) {
			for (Opinion opinion : opinions) {
				getPresenter().removeLikeNotifiers(opinion.getOpinionId());
			}
		}
	}

	@Override
	public void removeFail() {
		Snackbar.make(contentView, R.string.opinion_error_remove, Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void onShowError(int resMsg) {
		Snackbar.make(contentView, resMsg, Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void onImageClick(Opinion opinion) {
	}

	@Override
	public void onLikeClick(Opinion opinion) {
		getPresenter().onLikeClick(opinion);
	}

	@Override
	public void onCommentClick(Opinion opinion) {
		Intent intent = new Intent(getActivity(), CommentsViewClass.class);
		intent.putExtra(Constans.EXTRA_OPINION, opinion.getOpinionId());
		startActivity(intent);
	}

	@Override
	public void onShareClick(Opinion opinion) {
	}

	@Override
	public void onEditOpinionClick(Opinion opinion) {

	}

	@Override
	public void onRemoveOpinionClick(final Opinion opinion) {
		new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
				.setTitle(R.string.opinion_dialog_remove_title)
				.setMessage(R.string.opinion_dialog_remove_message)
				.setPositiveButton(R.string.opinion_dialog_remove_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						getPresenter().deleteOpinion(opinion);
					}
				})
				.setNegativeButton(R.string.common_dialog_cancel, null)
				.show();
	}

	@Override
	public void onResume() {
		super.onResume();
		getPresenter().getData(opinionsAdapter.getLastItemId());
		requestAddOpinionNotifiers();
		requestAddLikeNotifiers();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
		requestRemoveLikeNotifiers();
		requestRemoveOpinionNotifiers();
		getPresenter().onDestroy();
	}
}
