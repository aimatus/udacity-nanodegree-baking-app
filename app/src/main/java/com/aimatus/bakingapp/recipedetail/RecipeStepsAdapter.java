package com.aimatus.bakingapp.recipedetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aimatus.bakingapp.R;
import com.aimatus.bakingapp.model.Recipe;
import com.aimatus.bakingapp.model.Step;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    private static List<Step> steps;
    private static RecipeStepsFragment.OnStepClickListener onStepClickListener;

    RecipeStepsAdapter(Recipe recipe, RecipeStepsFragment.OnStepClickListener onStepClickListener) {
        steps = recipe.getSteps();
        RecipeStepsAdapter.onStepClickListener = onStepClickListener;
    }

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForMovieItem = R.layout.recipe_step_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final boolean shouldAttachToParentImmediately = false;
        View itemView = layoutInflater.inflate(layoutIdForMovieItem, parent, shouldAttachToParentImmediately);
        return new RecipeStepsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        String stepDescription = "";
        if (position > 0) {
            stepDescription += position + ". ";
        }
        stepDescription += steps.get(position).getShortDescription();
        holder.stepTitleTextView.setText(stepDescription);
    }

    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        }
        return steps.size();
    }

    public static class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView stepTitleTextView;

        RecipeStepsViewHolder(View itemView) {
            super(itemView);
            stepTitleTextView = itemView.findViewById(R.id.tv_recipe_step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            onStepClickListener.onStepSelected(adapterPosition);
        }
    }

}
