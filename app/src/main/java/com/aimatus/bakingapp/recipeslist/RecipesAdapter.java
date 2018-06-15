package com.aimatus.bakingapp.recipeslist;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    public static List<Recipe> recipes;
    private static RecipesOnClickHandler recipesOnClickHandler;

    RecipesAdapter(RecipesOnClickHandler recipesOnClickHandler) {
        RecipesAdapter.recipesOnClickHandler = recipesOnClickHandler;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForMovieItem = R.layout.recipes_grid_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final boolean shouldAttachToParentImmediately = false;
        View itemView = layoutInflater.inflate(layoutIdForMovieItem, parent, shouldAttachToParentImmediately);
        return new RecipesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.recipeTitleTextView.setText(recipe.getName());
        String recipeImage = recipe.getImage().isEmpty() ? null : recipe.getImage();
        Picasso.get().load(recipeImage).placeholder(R.drawable.placeholder_dessert)
                .error(R.drawable.placeholder_dessert).into(holder.recipeImageView);
    }

    @Override
    public int getItemCount() {
        return recipes == null ?  0 : recipes.size();
    }

    interface RecipesOnClickHandler {
        void onClick(Recipe recipe);
    }

    public static class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final CardView recipeCardView;
        final TextView recipeTitleTextView;
        final ImageView recipeImageView;

        RecipesViewHolder(View itemView) {
            super(itemView);
            recipeTitleTextView = itemView.findViewById(R.id.tv_recipe_title);
            recipeCardView = itemView.findViewById(R.id.cv_recipes);
            recipeImageView = itemView.findViewById(R.id.iv_recipe_image);
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
