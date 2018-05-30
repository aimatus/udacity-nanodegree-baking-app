package com.aimatus.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.aimatus.bakingapp.R;
import com.aimatus.bakingapp.model.Ingredient;

import java.util.List;

public class IngredientsWidgetService extends RemoteViewsService {

    private List<Ingredient> ingredientList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteListViewsFactory(getApplicationContext());
    }

    class RemoteListViewsFactory implements IngredientsWidgetService.RemoteViewsFactory {

        final Context mContext;

        RemoteListViewsFactory(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            ingredientList = IngredientsWidgetProvider.ingredients;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredientList == null) return 0;
            return ingredientList.size();
        }

        @Override
        public RemoteViews getViewAt(int index) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget_item);
            Ingredient ingredient = ingredientList.get(index);
            int position = index + 1;
            String widgetItem = String.format(getString(R.string.widget_item_format), position,
                    ingredient.getIngredient(), Double.toString(ingredient.getQuantity()), ingredient.getMeasure());
            views.setTextViewText(R.id.tv_ingredient_widget_item, widgetItem);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
