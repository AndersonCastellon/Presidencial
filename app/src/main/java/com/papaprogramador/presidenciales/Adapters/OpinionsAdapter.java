package com.papaprogramador.presidenciales.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.papaprogramador.presidenciales.Obj.Opinions;
import com.papaprogramador.presidenciales.R;
import com.papaprogramador.presidenciales.ViewHolders.OpinionsViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpinionsAdapter extends RecyclerView.Adapter<OpinionsViewHolder> {

	private List<Opinions> mainListOpinions;
	private long lastItem = 0;

	public OpinionsAdapter() {
		this.mainListOpinions = new ArrayList<>();
	}

	@NonNull
	@Override
	public OpinionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opinions_layout, parent, false);
		return new OpinionsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull OpinionsViewHolder holder, int position) {
		Opinions opinions = mainListOpinions.get(position);
		holder.setData(opinions);
	}

	public List<Opinions> getMainListOpinions() {
		Collections.sort(mainListOpinions);
		notifyItemRangeInserted((mainListOpinions.size()+1), 20);

		if (mainListOpinions.size() != 0){
			lastItem = mainListOpinions.get((mainListOpinions.size() - 1)).getOrderBy();
		}

		return mainListOpinions;
	}

	public void setMainListOpinions(List<Opinions> newOpinionsList) {

		if (!this.mainListOpinions.containsAll(newOpinionsList)) {
			this.mainListOpinions.addAll(newOpinionsList);
		}
	}

	public long getLastItemId() {
		return lastItem;
	}

	@Override
	public int getItemCount() {
		return mainListOpinions == null ? 0 : mainListOpinions.size();
	}
}
