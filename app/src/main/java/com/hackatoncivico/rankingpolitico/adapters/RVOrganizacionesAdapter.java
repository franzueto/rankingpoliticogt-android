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
import com.hackatoncivico.rankingpolitico.models.Organizacion;
import com.hackatoncivico.rankingpolitico.utils.ApiAccess;
import com.hackatoncivico.rankingpolitico.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by franz on 7/11/2015.
 */
public class RVOrganizacionesAdapter extends RecyclerView.Adapter<RVOrganizacionesAdapter.OrganizacionViewHolder>{

    private List<Organizacion> organizaciones;
    private Context context;

    public RVOrganizacionesAdapter(Context context, List<Organizacion> organizaciones){
        this.context = context;
        this.organizaciones = organizaciones;
    }

    @Override
    public OrganizacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_organizaciones, parent, false);
        OrganizacionViewHolder pvh = new OrganizacionViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(OrganizacionViewHolder holder, int position) {
        holder.nombre.setText(organizaciones.get(position).nombre);

        Picasso.with(context)
                .load(ApiAccess.DOMINIO_URL + organizaciones.get(position).logo)
                .placeholder(R.drawable.avatar)
                .into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return organizaciones.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class OrganizacionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nombre;
        ImageView logo;

        OrganizacionViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView)itemView.findViewById(R.id.organizaciones_nombre);
            logo = (ImageView) itemView.findViewById(R.id.organizaciones_logo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            //intent.putExtra(ProfileActivity.ID_CANDIDATO, String.valueOf(candidatos.get(getPosition()).id));
            v.getContext().startActivity(intent);
        }
    }
}
