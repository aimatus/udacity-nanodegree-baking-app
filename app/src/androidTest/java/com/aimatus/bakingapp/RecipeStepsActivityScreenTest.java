package com.aimatus.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aimatus.bakingapp.model.Recipe;
import com.aimatus.bakingapp.model.Step;
import com.aimatus.bakingapp.recipedetail.RecipeDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeStepsActivityScreenTest {

    private final String RECIPE_INTRODUCTION = "Recipe introduction";

    /**
     * Based on https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item
     */
    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityRule =
            new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, RecipeDetailActivity.class);

                    Recipe recipe = new Recipe();

                    Step step = new Step();
                    step.setDescription(RECIPE_INTRODUCTION);
                    step.setShortDescription(RECIPE_INTRODUCTION);

                    List<Step> steps = new ArrayList<>();
                    steps.add(step);

                    recipe.setSteps(steps);

                    String RECIPE_TAG = "mRecipe";
                    result.putExtra(RECIPE_TAG, recipe);
                    return result;
                }
            };

    @Test
    public void testHasRecipeIntroItem() {
        onView(withId(R.id.rv_recipe_steps))
                .check(matches(hasDescendant(withText(RECIPE_INTRODUCTION))));
    }

    @Test
    public void testClickOnRecipeIntro() {
        onView(withId(R.id.rv_recipe_steps))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(RECIPE_INTRODUCTION)), click()));
    }

    @Test
    public void testHasIngredientsItem() {
        String INGREDIENTS = "Ingredients";
        onView(withId(R.id.tv_ingredients)).check(matches(withText(INGREDIENTS)));
    }


}
