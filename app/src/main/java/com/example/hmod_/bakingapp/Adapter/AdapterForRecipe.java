package com.example.hmod_.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hmod_.bakingapp.R;


public class AdapterForRecipe extends RecyclerView.Adapter<AdapterForRecipe.RecipeViewHolder> {

    private int[] mIdForRecipe;
    private String[] mNameForRecipe;
    private RecipeClickListener mClickListener;
    private Context mcontext;


    public AdapterForRecipe(Context context, RecipeClickListener listener) {
        mClickListener = listener;
        mcontext = context;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_main_layout, parent, false);
        return new RecipeViewHolder(view);

    }

    @Override
    public int getItemCount() {
        return mNameForRecipe == null ? 0 : mNameForRecipe.length;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.mTextViewRecipeName.setText(mNameForRecipe[position]);
    }

    public void add(int[] id, String[] name) {
        mIdForRecipe = id;
        mNameForRecipe = name;
        notifyDataSetChanged();
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView mTextViewRecipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            mTextViewRecipeName = itemView.findViewById(R.id.title_for_recipe);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickListener.onRecipeClick(mIdForRecipe[position], mNameForRecipe[position]);
        }
    }

    public interface RecipeClickListener {
        void onRecipeClick(int id, String name);
    }


}
