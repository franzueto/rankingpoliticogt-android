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
import com.hackatoncivico.rankingpolitico.models.Logro;
import com.hackatoncivico.rankingpolitico.models.Organizacion;
import com.hackatoncivico.rankingpolitico.utils.ApiAccess;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by franz on 7/11/2015.
 */
public class RVLogrosAdapter extends RecyclerView.Adapter<RVLogrosAdapter.LogroViewHolder>{

    private List<Logro> logros;
    private Context context;

    public RVLogrosAdapter(Context context, List<Logro> logros){
        this.context = context;
        this.logros = logros;
    }

    @Override
    public LogroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logros, parent, false);
        LogroViewHolder pvh = new LogroViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(LogroViewHolder holder, int position) {
        holder.descripcion.setText(logros.get(position).descripcion);
        holder.fechas.setText(logros.get(position).inicio + " - " + logros.get(position).fin);
    }

    @Override
    public int getItemCount() {
        return logros.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class LogroViewHolder extends RecyclerView.ViewHolder{
        TextView descripcion;
        TextView fechas;

        LogroViewHolder(View itemView) {
            super(itemView);
            descripcion = (TextView)itemView.findViewById(R.id.logro_descipcion);
            fechas = (TextView)itemView.findViewById(R.id.logro_fechas);
        }
    }
}
