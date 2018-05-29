package com.aimatus.bakingapp.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aimatus.bakingapp.R;
import com.aimatus.bakingapp.model.Recipe;
import com.aimatus.bakingapp.widget.IngredientsWidgetProvider;

/**
 * Baking App Project
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 * October 2017
 *
 * @author Abraham Matus
 */
public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepsFragment.OnStepClickListener {

    public Recipe recipe;
    public int stepIndex;
    boolean isLargeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getInteger(R.integer.grid_columns) > 1) {
            isLargeScreen = true;
        } else {
            isLargeScreen = false;
        }

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.recipe_tag))) {
            recipe = (Recipe) intent.getSerializableExtra(getString(R.string.recipe_tag));
            setTitle(recipe.getName());

            Intent i = new Intent(this, IngredientsWidgetProvider.class);
            i.putExtra(getString(R.string.recipe_tag), recipe);
            i.setAction(getString(R.string.widget_intent_action));
            sendBroadcast(i);
        }

        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {
            if (isLargeScreen) {
                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                recipeStepDetailFragment.stepIndex = 0;
                recipeStepDetailFragment.recipe = recipe;
                recipeStepDetailFragment.isLargeScreen = true;
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_detail_fragment, recipeStepDetailFragment)
                        .commit();
            }
        }

    }

    @Override
    public void onStepSelected(int stepIndex) {
        if (isLargeScreen) {
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.stepIndex = stepIndex;
            recipeStepDetailFragment.recipe = recipe;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_fragment, recipeStepDetailFragment)
                    .commit();
        } else {
            this.stepIndex = stepIndex;
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(getString(R.string.step_index_tag), stepIndex);
            intent.putExtra(getString(R.string.recipe_tag), recipe);
            startActivity(intent);
        }

    }

}
