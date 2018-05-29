package com.aimatus.bakingapp.recipedetail;

import android.content.Context;
import android.os.Bundle;
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

/**
 * Baking App Project
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 * October 2017
 *
 * @author Abraham Matus
 */
public class RecipeStepsFragment extends Fragment {

    @BindView(R.id.tv_ingredients)
    TextView ingredientsTextView;

    OnStepClickListener mCallback;

    public RecipeStepsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecipeDetailActivity activity = (RecipeDetailActivity) getActivity();
        final View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recipe_steps);
        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(activity.recipe, activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recipeStepsAdapter);
        recyclerView.setHasFixedSize(false);

        ButterKnife.bind(this, rootView);

        ingredientsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), getString(R.string.ingredients_toast_message), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    public interface OnStepClickListener {
        void onStepSelected(int stepIndex);
    }
}
