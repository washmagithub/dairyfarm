package com.example.dairyfarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<MainModel> cowList;
    private final OnItemClickListener listener;

    public Adapter(List<MainModel> cowList, OnItemClickListener listener) {
        this.cowList = cowList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainModel mainModel = cowList.get(position);
        holder.bind(mainModel, listener);
    }

    @Override
    public int getItemCount() {
        return cowList.size();
    }

    public void setFilteredList(List<MainModel> filteredList){
        this.cowList = filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView cowidTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cowidTextView = itemView.findViewById(R.id.cow1);
        }

        public void bind(final MainModel mainModel, final OnItemClickListener listener) {
            String cowId = mainModel.getCowid();
            cowidTextView.setText(" " + cowId);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(mainModel);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MainModel item);
    }
}