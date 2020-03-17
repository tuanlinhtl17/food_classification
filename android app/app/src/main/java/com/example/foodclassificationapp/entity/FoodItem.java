package com.example.foodclassificationapp.entity;

import java.io.Serializable;

public class FoodItem implements Serializable {
    private String name;
    private String image;
    private double calories;
    private double cacbohydrat;
    private double fat;
    private double protein;

    public FoodItem(String name, double calories, double carbs, double fats, double proteins, String image) {
        this.name = name;
        this.calories = calories;
        this.cacbohydrat = carbs;
        this.fat = fats;
        this.protein = proteins;
        this.image = image;
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

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getCarbs() {
        return cacbohydrat;
    }

    public void setCarbs(double carbs) {
        this.cacbohydrat = carbs;
    }

    public double getFats() {
        return fat;
    }

    public void setFats(double fats) {
        this.fat = fats;
    }

    public double getProteins() {
        return protein;
    }

    public void setProteins(double proteins) {
        this.protein = proteins;
    }
}
