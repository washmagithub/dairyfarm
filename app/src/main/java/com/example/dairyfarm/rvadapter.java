package com.example.dairyfarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class rvadapter extends RecyclerView.Adapter<rvadapter.ViewHolder> {
    Context context;
    List<Model> rv_list; // Change to List<Model>

    public rvadapter(Context context, List<Model> rv_list) {
        this.context = context;
        this.rv_list = rv_list;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model model = rv_list.get(position);
        holder.t11.setText(model.getCid());
        holder.t22.setText(model.getDname());
        holder.t33.setText(model.getDr());
        holder.t44.setText(model.getDt());
        holder.t55.setText(model.getDis());
    }

    @Override
    public int getItemCount() {
        return rv_list.size();
    }

    public void setRv_list(List<Model> models) {
        this.rv_list = models;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView t11, t22, t33, t44, t55;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t11 = itemView.findViewById(R.id.t11);
            t22 = itemView.findViewById(R.id.t22);
            t33 = itemView.findViewById(R.id.t33);
            t44 = itemView.findViewById(R.id.t44);
            t55 = itemView.findViewById(R.id.t55);
        }
    }
}
