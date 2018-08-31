package com.papaprogramador.presidenciales.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constans;
import com.papaprogramador.presidenciales.common.pojo.Candidate;
import com.papaprogramador.presidenciales.View.Activity.DetailOfCandidates;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.CandidatoViewHolder> {

	private List<Candidate> candidateList;

	public CandidateAdapter() {
	}

	public void setCandidateList(List<Candidate> candidateList) {
		this.candidateList = candidateList;
	}

	public List<Candidate> getCandidateList() {
		return candidateList;
	}

	@NonNull
	@Override
	public CandidatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_candidatos, parent, false);
		CandidatoViewHolder candidatoViewHolder = new CandidatoViewHolder(view);
		return candidatoViewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull final CandidatoViewHolder holder, int position) {
		Candidate candidate = candidateList.get(position);

		holder.textViewNombre.setText(candidate.getNombreCandidato());
		holder.textViewPartido.setText(candidate.getPartidoCandidato());
		holder.textViewVotos.setText(String.valueOf(candidate.getVotosCandidato()));
		holder.idcandidato = candidate.getId();
		holder.urlImage = candidate.getUrlImagen();
		holder.urlHtml = candidate.getUrlHtml();
		holder.urlPoliticalFlag = candidate.getPoliticalFlag();

		//TODO: Implementar placeholder para Glide

		RequestOptions options = new RequestOptions()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.centerCrop();

		Glide.with(holder.context)
				.load(holder.urlImage)
				.apply(options)
				.into(holder.imageViewCandidato);

		holder.setOnClickListener();

	}

	@Override
	public int getItemCount() {
		return candidateList.size();
	}

	public static class CandidatoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		ImageView imageViewCandidato;
		TextView textViewNombre, textViewPartido, textViewVotos;

		Context context;

		String idcandidato;
		String urlImage;
		String urlHtml;
		String urlPoliticalFlag;

		private CandidatoViewHolder(View itemView) {
			super(itemView);

			context = itemView.getContext();//Inicializacion del contexto

			imageViewCandidato = itemView.findViewById(R.id.imagenCandidato);
			textViewNombre = itemView.findViewById(R.id.nombreCandidato);
			textViewPartido = itemView.findViewById(R.id.partidoCandidato);
			textViewVotos = itemView.findViewById(R.id.votosCandidato);
		}

		void setOnClickListener() {
			imageViewCandidato.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.imagenCandidato:
					Intent detalleCandidato = new Intent(context, DetailOfCandidates.class);
					detalleCandidato.putExtra(Constans.PUT_ID_CANDIDATE, idcandidato);
					detalleCandidato.putExtra(Constans.PUT_NOMBRE_CANDIDATE, textViewNombre.getText().toString());
					detalleCandidato.putExtra(Constans.PUT_URL_IMAGEN_CANDIDATE, urlImage);
					detalleCandidato.putExtra(Constans.PUT_URL_HTML_CANDIDATE, urlHtml);
					detalleCandidato.putExtra(Constans.PUT_POLITICAL_FLAG, urlPoliticalFlag);
					context.startActivity(detalleCandidato);
					break;
			}
		}
	}
}
