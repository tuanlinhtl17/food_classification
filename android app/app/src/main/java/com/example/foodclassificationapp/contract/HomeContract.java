package com.example.foodclassificationapp.contract;

import com.example.foodclassificationapp.entity.DailyNutrition;
import com.example.foodclassificationapp.entity.FitnessExercise;
import com.example.foodclassificationapp.entity.FoodItem;

import java.util.ArrayList;
import java.util.Calendar;

public interface HomeContract {
    interface View {
        void showFoodList(ArrayList<FoodItem> foodList);

        void showDailyNutrition(DailyNutrition dailyNutrition);

        void showTotalCalorieBurn(double total);

        void setDate(Calendar calendar);

        void setRecommendRate(double brm);

        void shareWeight(float weight);

        void showActivityList(ArrayList<FitnessExercise> activityList);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void getFoodList(String dateKey);

        void getActivityList(String dateKey);

        DailyNutrition calculateDailyNutrition();

        void calculateRecommendRate();

        double calculateBrm(float height, float weight, String gender, int age);

        void setMode();

        boolean getMode();

        double calculateCalorieBurnDaily();
    }
}
