package com.papaprogramador.presidenciales.Adaptadores;

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
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.Utils.Constantes;
import com.papaprogramador.presidenciales.Objetos.Candidato;
import com.papaprogramador.presidenciales.Vistas.Actividades.DetailCandidatoView;

import java.util.List;

public class CandidatoAdapter extends RecyclerView.Adapter<CandidatoAdapter.CandidatoViewHolder> {

	List<Candidato> candidatoList;

	public CandidatoAdapter() {
	}

	public void setCandidatoList(List<Candidato> candidatoList) {
		this.candidatoList = candidatoList;
	}

	public List<Candidato> getCandidatoList() {
		return candidatoList;
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
		Candidato candidato = candidatoList.get(position);
		holder.textViewNombre.setText(candidato.getNombreCandidato());
		holder.textViewPartido.setText(candidato.getPartidoCandidato());
		holder.textViewVotos.setText(String.valueOf(candidato.getVotosCandidato()));
		holder.idcandidato = candidato.getId();
		holder.urlImage = candidato.getStringImagen();

		FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
		StorageReference storageReference = firebaseStorage.getReferenceFromUrl(candidato.getStringImagen());

		//Obteniendo la imagen con Glide, mucho más optimo
		//TODO: Implementar placeholder para Glide
		Glide.with(holder.imageViewCandidato.getContext())
				.using(new FirebaseImageLoader())
				.load(storageReference)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(holder.imageViewCandidato);

		holder.setOnClickListener();

	}

	@Override
	public int getItemCount() {
		return candidatoList.size();
	}

	public static class CandidatoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		ImageView imageViewCandidato;
		TextView textViewNombre, textViewPartido, textViewVotos;

		Context context;//Variable del contexto

		String idcandidato;
		String urlImage;

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
					Intent detalleCandidato = new Intent(context, DetailCandidatoView.class);
					detalleCandidato.putExtra(Constantes.PUT_ID_CANDIDATO, idcandidato);
					detalleCandidato.putExtra(Constantes.PUT_NOMBRE_CANDIDATO, textViewNombre.getText().toString());
					detalleCandidato.putExtra(Constantes.PUT_URL_IMAGEN_CANDIDATO, urlImage);
					context.startActivity(detalleCandidato);
					break;
			}
		}
	}
}
