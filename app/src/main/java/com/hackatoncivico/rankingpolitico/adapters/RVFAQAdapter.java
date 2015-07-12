package com.hackatoncivico.rankingpolitico.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackatoncivico.rankingpolitico.R;
import com.hackatoncivico.rankingpolitico.models.FAQ;
import com.hackatoncivico.rankingpolitico.models.Logro;

import java.util.List;

/**
 * Created by franz on 7/11/2015.
 */
public class RVFAQAdapter extends RecyclerView.Adapter<RVFAQAdapter.FAQViewHolder>{

    private List<FAQ> faqs;

    public RVFAQAdapter(List<FAQ> faqs){
        this.faqs = faqs;
    }

    @Override
    public FAQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logros, parent, false);
        FAQViewHolder pvh = new FAQViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FAQViewHolder holder, int position) {
        holder.question.setText(faqs.get(position).question);
        holder.answer.setText(faqs.get(position).answer);
    }

    @Override
    public int getItemCount() {
        return faqs.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class FAQViewHolder extends RecyclerView.ViewHolder{
        TextView question;
        TextView answer;

        FAQViewHolder(View itemView) {
            super(itemView);
            question = (TextView)itemView.findViewById(R.id.logro_descipcion);
            answer = (TextView)itemView.findViewById(R.id.logro_fechas);
        }
    }
}
