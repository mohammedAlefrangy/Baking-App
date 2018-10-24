package com.example.hmod_.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hmod_.bakingapp.R;


public class AdapterForStep extends RecyclerView.Adapter<AdapterForStep.StepViewHolder> {


    private Context mContext;
    private StepClickListener ClickListener;
    private String[] Description;

    public AdapterForStep(Context context, StepClickListener listener) {
        ClickListener = listener;
        mContext = context;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return Description == null ? 0 : Description.length;
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.titleName.setText(Description[position]);
    }

    public void add(String[] description) {
        Description = description;
        notifyDataSetChanged();
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleName;
        public StepViewHolder(View itemView) {
            super(itemView);
            titleName =itemView.findViewById(R.id.title_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ClickListener.onStepsClick(position);
        }
    }

    public interface StepClickListener {
        void onStepsClick(int id);
    }

}

