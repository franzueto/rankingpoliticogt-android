package com.hackatoncivico.rankingpolitico.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackatoncivico.rankingpolitico.R;
import com.hackatoncivico.rankingpolitico.models.Person;

import java.util.List;

/**
 * Created by franz on 7/11/2015.
 */
public class RVRankingAdapter extends RecyclerView.Adapter<RVRankingAdapter.PersonViewHolder> {

    private List<Person> persons;

    public RVRankingAdapter(List<Person> persons){
        this.persons = persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.personName.setText(persons.get(position).getName());
        holder.personPuesto.setText(persons.get(position).getPuesto());
        holder.personPhoto.setImageResource(persons.get(position).getPhotoId());
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        TextView personPuesto;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            personName = (TextView)itemView.findViewById(R.id.ranking_name);
            personPuesto = (TextView)itemView.findViewById(R.id.ranking_puesto);
            personPhoto = (ImageView)itemView.findViewById(R.id.ranking_photo);
        }
    }
}
