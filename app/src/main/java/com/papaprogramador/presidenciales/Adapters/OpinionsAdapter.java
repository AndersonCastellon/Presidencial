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

	private List<Opinions> opinionsList;

	public OpinionsAdapter() {
		this.opinionsList = new ArrayList<>();
	}

	@NonNull
	@Override
	public OpinionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opinions_layout, parent, false);
		return new OpinionsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull OpinionsViewHolder holder, int position) {
		Opinions opinions = opinionsList.get(position);
		holder.setData(opinions);
	}

	public List<Opinions> getOpinionsList() {
		return opinionsList;
	}

	public void setOpinionsList(List<Opinions> newOpinionsList) {

		if (!this.opinionsList.containsAll(newOpinionsList)) {

			this.opinionsList.addAll(newOpinionsList);
			Collections.sort(opinionsList);
			notifyItemRangeInserted(0, newOpinionsList.size());
		}
	}

	public String getLastItemId() {

		String lastItem = null;
		int lastItemId = this.opinionsList.size() == 0 ? -1 : this.opinionsList.size() - 1;

		if (lastItemId >= 0) {
			lastItem = opinionsList.get(lastItemId).getOpinionId();
		}

		return lastItem;
	}

	@Override
	public int getItemCount() {
		return opinionsList == null ? 0 : opinionsList.size();
	}
}
