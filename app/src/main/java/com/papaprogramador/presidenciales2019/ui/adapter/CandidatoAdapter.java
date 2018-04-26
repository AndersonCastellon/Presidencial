package com.papaprogramador.presidenciales2019.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.papaprogramador.presidenciales2019.R;
import com.papaprogramador.presidenciales2019.model.Candidato;
import com.papaprogramador.presidenciales2019.ui.Fragments.CandidatosFragment;

import java.util.List;

public class CandidatoAdapter extends RecyclerView.Adapter<CandidatoAdapter.CandidatoViewHolder> {

	List<Candidato> candidatoList;
	CandidatosFragment candidatosFragment;

	public CandidatoAdapter(List<Candidato> candidatoList) {
		this.candidatoList = candidatoList;
	}

	@Override
	public CandidatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_candidatos, parent, false);
		CandidatoViewHolder candidatoViewHolder = new CandidatoViewHolder(view);
		return candidatoViewHolder;
	}

	@Override
	public void onBindViewHolder(final CandidatoViewHolder holder, int position) {
		Candidato candidato = candidatoList.get(position);
		holder.textViewNombre.setText(candidato.getNombreCandidato());
		holder.textViewPartido.setText(candidato.getPartidoCandidato());
		holder.textViewVotos.setText(String.valueOf(candidato.getVotosCandidato()));

		FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
		StorageReference storageReference = firebaseStorage.getReferenceFromUrl(candidato.getStringImagen());

		//Obteniendo la imagen con Glide, mucho más optimo
		//TODO: Implementar placeholder para Glide
		Glide.with(holder.imageViewCandidato.getContext())
				.using(new FirebaseImageLoader())
				.load(storageReference)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(holder.imageViewCandidato);

	}

	@Override
	public int getItemCount() {
		return candidatoList.size();
	}

	public static class CandidatoViewHolder extends RecyclerView.ViewHolder {

		ImageView imageViewCandidato;
		TextView textViewNombre, textViewPartido, textViewVotos;

		public CandidatoViewHolder(View itemView) {
			super(itemView);

			imageViewCandidato = itemView.findViewById(R.id.imagenCandidato);
			textViewNombre = itemView.findViewById(R.id.nombreCandidato);
			textViewPartido = itemView.findViewById(R.id.partidoCandidato);
			textViewVotos = itemView.findViewById(R.id.votosCandidato);
		}
	}
}
