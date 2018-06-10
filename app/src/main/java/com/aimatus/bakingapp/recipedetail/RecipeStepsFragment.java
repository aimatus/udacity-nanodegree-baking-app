package com.aimatus.bakingapp.recipedetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aimatus.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsFragment extends Fragment {

    @BindView(R.id.tv_ingredients) TextView ingredientsTextView;

    public RecipeStepsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecipeDetailActivity activity = (RecipeDetailActivity) getActivity();
        final View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        initRecyclerView(activity, rootView);
        ButterKnife.bind(this, rootView);
        initIngredientsTextViewOnClickListener();
        return rootView;
    }

    private void initIngredientsTextViewOnClickListener() {
        ingredientsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), getString(R.string.ingredients_toast_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initRecyclerView(RecipeDetailActivity activity, View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_recipe_steps);
        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(activity.mRecipe, activity, activity.mStepIndex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recipeStepsAdapter);
        recyclerView.setHasFixedSize(false);
    }

    public interface OnStepClickListener {
        void onStepSelected(int stepIndex);
    }
}
