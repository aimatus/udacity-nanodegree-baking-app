package com.aimatus.bakingapp.recipedetail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.aimatus.bakingapp.R;
import com.aimatus.bakingapp.model.Recipe;
import com.aimatus.bakingapp.model.Step;

public class RecipeStepDetailActivity extends AppCompatActivity {

    int stepIndex;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateActionBarVisibility();
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.step_index_tag))) {
            getRecipe(intent);
        }
        setContentView(R.layout.activity_recipe_step_detail);
    }

    private void updateActionBarVisibility() {
        // https://stackoverflow.com/questions/11856886/hiding-title-bar-notification-bar-when-device-is-oriented-to-landscape
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void getRecipe(Intent intent) {
        stepIndex = (int) intent.getSerializableExtra(getString(R.string.step_index_tag));
        recipe = (Recipe) intent.getSerializableExtra(getString(R.string.recipe_tag));
        Step step = recipe.getSteps().get(stepIndex);
        setTitle(step.getShortDescription());
    }
}
