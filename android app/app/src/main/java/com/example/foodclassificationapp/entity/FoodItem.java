package com.example.foodclassificationapp.entity;

import java.io.Serializable;

public class FoodItem implements Serializable {
    private String name;
    private String image;
    private String calories;
    private String cacbohydrat;
    private String fat;
    private String protein;
    private boolean isMyFood;
    private String recipe;

    public FoodItem(String name, String calories, String cacbohydrat, String fat, String protein, String image, boolean isMyFood, String recipe) {
        this.name = name;
        this.calories = calories;
        this.cacbohydrat = cacbohydrat;
        this.fat = fat;
        this.protein = protein;
        this.image = image;
        this.isMyFood = isMyFood;
        this.recipe = recipe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCacbohydrat() {
        return cacbohydrat;
    }

    public void setCacbohydrat(String cacbohydrat) {
        this.cacbohydrat = cacbohydrat;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public boolean isMyFood() {
        return isMyFood;
    }

    public void setMyFood(boolean myFood) {
        isMyFood = myFood;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
