package com.aimatus.bakingapp.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aimatus.bakingapp.R;
import com.aimatus.bakingapp.model.Recipe;
import com.aimatus.bakingapp.widget.IngredientsWidgetProvider;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepsFragment.OnStepClickListener {

    public Recipe mRecipe;
    int mStepIndex;
    boolean mIsLargeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mStepIndex = savedInstanceState.getInt(getString(R.string.step_index_tag));
        }
        int smallScreenDeviceColumns = 1;
        mIsLargeScreen = getResources().getInteger(R.integer.grid_columns) > smallScreenDeviceColumns;
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.recipe_tag))) {
            setTitleToRecipeName(intent);
            sendRecipeToWidget();
        }
        setContentView(R.layout.activity_recipe_detail);
        if (savedInstanceState == null) {
            if (mIsLargeScreen) {
                addRecipeStepDetailFragmentToActivity();
            }
        }
    }

    private void setTitleToRecipeName(Intent intent) {
        mRecipe = (Recipe) intent.getSerializableExtra(getString(R.string.recipe_tag));
        setTitle(mRecipe.getName());
    }

    private void addRecipeStepDetailFragmentToActivity() {
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        RecipeStepDetailFragment.stepIndex = 0;
        RecipeStepDetailFragment.recipe = mRecipe;
        RecipeStepDetailFragment.isLargeScreen = true;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.step_detail_fragment, recipeStepDetailFragment).commit();
    }

    private void sendRecipeToWidget() {
        Intent intent = new Intent(this, IngredientsWidgetProvider.class);
        intent.putExtra(getString(R.string.recipe_tag), mRecipe);
        intent.setAction(getString(R.string.widget_intent_action));
        sendBroadcast(intent);
    }

    @Override
    public void onStepSelected(int stepIndex) {
        this.mStepIndex = stepIndex;
        if (mIsLargeScreen) {
            updateRecipeStepDetailFragment(stepIndex);
        } else {
            startRecipeStepDetailActivity(stepIndex);
        }
    }

    private void startRecipeStepDetailActivity(int stepIndex) {
        Intent intent = new Intent(this, RecipeStepDetailActivity.class);
        intent.putExtra(getString(R.string.step_index_tag), stepIndex);
        intent.putExtra(getString(R.string.recipe_tag), mRecipe);
        startActivity(intent);
    }

    private void updateRecipeStepDetailFragment(int stepIndex) {
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        RecipeStepDetailFragment.stepIndex = stepIndex;
        RecipeStepDetailFragment.recipe = mRecipe;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_fragment, recipeStepDetailFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.step_index_tag), mStepIndex);
    }
}
