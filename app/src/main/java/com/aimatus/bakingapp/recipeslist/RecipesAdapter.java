package com.aimatus.bakingapp.recipeslist;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aimatus.bakingapp.R;
import com.aimatus.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Baking App Project
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 * October 2017
 *
 * @author Abraham Matus
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private Context mContext;
    public static List<Recipe> recipes;
    private static RecipesOnClickHandler recipesOnClickHandler;

    public RecipesAdapter(RecipesOnClickHandler recipesOnClickHandler, Context context) {
        this.recipesOnClickHandler = recipesOnClickHandler;
        this.mContext = context;
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForMovieItem = R.layout.recipes_grid_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final boolean shouldAttachToParentImmediately = false;

        View itemView = layoutInflater.inflate(layoutIdForMovieItem, parent, shouldAttachToParentImmediately);

        return new RecipesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipesViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.recipeTitleTextView.setText(recipe.getName());
        String recipeImage = recipe.getImage().isEmpty() ? null : recipe.getImage();
        Picasso.get()
                .load(recipeImage)
                .placeholder(R.drawable.placeholder_dessert)
                .error(R.drawable.placeholder_dessert)
                .into(holder.recipeImageView);
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        }
        return recipes.size();
    }

    public static class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView recipeCardView;
        TextView recipeTitleTextView;
        ImageView recipeImageView;

        public RecipesViewHolder(View itemView) {
            super(itemView);
            recipeTitleTextView = (TextView) itemView.findViewById(R.id.tv_recipe_title);
            recipeCardView = (CardView) itemView.findViewById(R.id.cv_recipes);
            recipeImageView = (ImageView) itemView.findViewById(R.id.iv_recipe_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = recipes.get(adapterPosition);
            recipesOnClickHandler.onClick(recipe);
        }
    }

}
