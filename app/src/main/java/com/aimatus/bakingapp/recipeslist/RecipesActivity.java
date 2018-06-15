package com.aimatus.bakingapp.recipeslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aimatus.bakingapp.R;
import com.aimatus.bakingapp.model.Recipe;
import com.aimatus.bakingapp.recipedetail.RecipeDetailActivity;
import com.aimatus.bakingapp.restclient.ApiService;
import com.aimatus.bakingapp.restclient.RetroClient;
import com.aimatus.bakingapp.uitest.SimpleIdlingResource;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity implements RecipesAdapter.RecipesOnClickHandler {

    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageTextView;
    @BindView(R.id.pb_loading_indicator) ProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeContainer;

    RecipesAdapter mRecipesAdapter;
    private List<Recipe> recipes;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);
        initSwipeContainerRefreshListener();
        initRecyclerView();
        getIdlingResource();
        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.recipes_tag))) {
            mProgressBar.setVisibility(View.VISIBLE);
            getRecipes();
        } else {
            recipes = (List<Recipe>) savedInstanceState.getSerializable(getString(R.string.recipes_tag));
        }
    }

    private void initRecyclerView() {
        mRecipesAdapter = new RecipesAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mRecipesAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    private void initSwipeContainerRefreshListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecipes();
            }
        });
    }

    private void getRecipes() {
        mIdlingResource.setIdleState(false);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        ApiService apiService = RetroClient.getApiService();
        Call<List<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                recipes = response.body();
                RecipesAdapter.recipes = recipes;
                updateRecipesViewsOnSuccess();
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.e(this.getClass().getSimpleName(), t.toString());
                updateRecipesViewsOnFailure();
            }
        });
    }

    private void updateRecipesViewsOnFailure() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        swipeContainer.setRefreshing(false);
        mIdlingResource.setIdleState(true);
    }

    private void updateRecipesViewsOnSuccess() {
        mRecipesAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        swipeContainer.setRefreshing(false);
        mIdlingResource.setIdleState(true);
    }

    @Override
    public void onClick(Recipe recipe) {
        System.out.println(recipe.toString());
        Intent intent = new Intent(RecipesActivity.this, RecipeDetailActivity.class);
        intent.putExtra(getString(R.string.recipe_tag), recipe);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (recipes != null) {
            outState.putSerializable(getString(R.string.recipes_tag), (Serializable) recipes);
        }
        super.onSaveInstanceState(outState);
    }
}
