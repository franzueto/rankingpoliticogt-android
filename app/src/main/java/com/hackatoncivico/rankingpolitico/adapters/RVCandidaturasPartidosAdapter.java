package com.hackatoncivico.rankingpolitico.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackatoncivico.rankingpolitico.ProfileActivity;
import com.hackatoncivico.rankingpolitico.R;
import com.hackatoncivico.rankingpolitico.models.Candidato;
import com.hackatoncivico.rankingpolitico.utils.ApiAccess;
import com.hackatoncivico.rankingpolitico.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by franz on 7/11/2015.
 */
public class RVCandidaturasPartidosAdapter extends RecyclerView.Adapter<RVCandidaturasPartidosAdapter.CandidatoViewHolder>{

    private List<Candidato> candidatos;
    private Context context;

    public RVCandidaturasPartidosAdapter(Context context, List<Candidato> candidatos){
        this.context = context;
        this.candidatos = candidatos;
    }

    @Override
    public CandidatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        CandidatoViewHolder pvh = new CandidatoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CandidatoViewHolder holder, int position) {
        holder.personName.setText(candidatos.get(position).nombres + " " +  candidatos.get(position).apellidos);
        holder.personPuesto.setText(candidatos.get(position).puesto.titulo);

        Picasso.with(context)
                .load(ApiAccess.DOMINIO_URL + candidatos.get(position).foto)
                .placeholder(R.drawable.avatar)
                .into(holder.personPhoto);
        //holder.personPhoto.setImageResource(candidatos.get(position).getPhotoId());
    }

    @Override
    public int getItemCount() {
        return candidatos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class CandidatoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView personName;
        TextView personPuesto;
        ImageView personPhoto;

        CandidatoViewHolder(View itemView) {
            super(itemView);
            personName = (TextView)itemView.findViewById(R.id.ranking_name);
            personPuesto = (TextView)itemView.findViewById(R.id.ranking_puesto);
            personPhoto = (ImageView)itemView.findViewById(R.id.ranking_photo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            Utils.savePreference(v.getContext(), Utils.SELECTED_CANDIDATE, String.valueOf(candidatos.get(getPosition()).id));
            //intent.putExtra(ProfileActivity.ID_CANDIDATO, String.valueOf(candidatos.get(getPosition()).id));
            v.getContext().startActivity(intent);
        }
    }
}
