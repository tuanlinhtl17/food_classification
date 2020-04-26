package com.example.foodclassificationapp.entity;

public class DailyNutrition {
    private float totalCalorie;
    private float totalCarbohydrate;
    private float totalFat;
    private float totalProtein;

    public DailyNutrition(float totalCalorie, float totalCarbohydrate, float totalFat, float totalProtein) {
        this.totalCalorie = totalCalorie;
        this.totalCarbohydrate = totalCarbohydrate;
        this.totalFat = totalFat;
        this.totalProtein = totalProtein;
    }

    public float getTotalCalorie() {
        return totalCalorie;
    }

    public void setTotalCalorie(float totalCalorie) {
        this.totalCalorie = totalCalorie;
    }

    public float getTotalCarbohydrate() {
        return totalCarbohydrate;
    }

    public void setTotalCarbohydrate(float totalCarbohydrate) {
        this.totalCarbohydrate = totalCarbohydrate;
    }

    public float getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(float totalFat) {
        this.totalFat = totalFat;
    }

    public float getTotalProtein() {
        return totalProtein;
    }

    public void setTotalProtein(float totalProtein) {
        this.totalProtein = totalProtein;
    }
}
