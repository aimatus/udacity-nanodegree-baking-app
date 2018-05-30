package com.aimatus.bakingapp.model;

import java.io.Serializable;

public class Ingredient implements Serializable {

    private double quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String meassure) {
        this.measure = meassure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", meassure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }
}
