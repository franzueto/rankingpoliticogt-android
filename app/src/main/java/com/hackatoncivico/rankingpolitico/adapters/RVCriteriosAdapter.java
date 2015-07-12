package com.hackatoncivico.rankingpolitico.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackatoncivico.rankingpolitico.R;
import com.hackatoncivico.rankingpolitico.models.Criterio;
import com.hackatoncivico.rankingpolitico.models.CriterioCandidato;
import com.hackatoncivico.rankingpolitico.models.Logro;

import java.util.List;

/**
 * Created by franz on 7/11/2015.
 */
public class RVCriteriosAdapter extends RecyclerView.Adapter<RVCriteriosAdapter.CriterioViewHolder>{

    private List<CriterioCandidato> criterios;
    private Context context;

    public RVCriteriosAdapter(Context context, List<CriterioCandidato> criterios){
        this.context = context;
        this.criterios = criterios;
    }

    @Override
    public CriterioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_criterios, parent, false);
        CriterioViewHolder pvh = new CriterioViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CriterioViewHolder holder, int position) {
        holder.titulo.setText(criterios.get(position).criterio.titulo);
        holder.descripcion.setText(criterios.get(position).descripcion);
        holder.puntuacion.setText(criterios.get(position).criterio.puntuacion);
    }

    @Override
    public int getItemCount() {
        return criterios.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class CriterioViewHolder extends RecyclerView.ViewHolder{
        TextView titulo;
        TextView descripcion;
        TextView puntuacion;

        CriterioViewHolder(View itemView) {
            super(itemView);
            titulo = (TextView)itemView.findViewById(R.id.criterio_titulo);
            descripcion = (TextView)itemView.findViewById(R.id.criterio_descripcion);
            puntuacion = (TextView)itemView.findViewById(R.id.criterio_puntuacion);
        }
    }
}
