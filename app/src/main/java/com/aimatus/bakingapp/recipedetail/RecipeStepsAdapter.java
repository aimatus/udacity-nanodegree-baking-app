package com.aimatus.bakingapp.recipedetail;

import android.content.Context;
import android.graphics.Color;
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
    private View lastSelectedItemView;

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
        String stepDescription = steps.get(position).getShortDescription();
        String stepNumber = Integer.toString(position);
        if (position == 0) stepNumber = "";
        holder.stepTitleTextView.setText(stepDescription);
        holder.stepNumberTextView.setText(stepNumber);
    }

    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        }
        return steps.size();
    }

    public  class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView stepTitleTextView;
        final TextView stepNumberTextView;
        final View listDivider;
        final boolean isLargeScreen;

        RecipeStepsViewHolder(View itemView) {
            super(itemView);
            stepTitleTextView = itemView.findViewById(R.id.tv_recipe_step);
            stepNumberTextView = itemView.findViewById(R.id.tv_recipe_step_number);
            listDivider = itemView.findViewById(R.id.horizontal_separator);
            itemView.setOnClickListener(this);
            isLargeScreen = itemView.getResources().getInteger(R.integer.grid_columns) > 1;
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            onStepClickListener.onStepSelected(adapterPosition);
            if (isLargeScreen) {
                applyLargeScreenStyles(view);
            }
        }

        private void applyLargeScreenStyles(View view) {
            applyColorAccent(view);
            if (lastSelectedItemView != null) {
                removeColorAccent(view);
            }
            lastSelectedItemView = view;
        }

        private void removeColorAccent(View view) {
            int stepNumberBackgroundColor = view.getResources().getColor(R.color.stepNumberBackgroundColor);
            int separatorColor = view.getResources().getColor(R.color.light_gray);
            TextView lastSelectedStepNumberTextView = lastSelectedItemView.findViewById(R.id.tv_recipe_step_number);
            View lastSelectedItemViewSeparator = lastSelectedItemView.findViewById(R.id.horizontal_separator);
            lastSelectedStepNumberTextView.setBackgroundColor(stepNumberBackgroundColor);
            lastSelectedItemViewSeparator.setBackgroundColor(separatorColor);
        }

        private void applyColorAccent(View view) {
            int colorAccent = view.getResources().getColor(R.color.colorAccent);
            stepNumberTextView.setBackgroundColor(colorAccent);
            listDivider.setBackgroundColor(colorAccent);
        }
    }

}
