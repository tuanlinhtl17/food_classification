package com.example.foodclassificationapp.contract;

import com.example.foodclassificationapp.entity.DailyNutrition;
import com.example.foodclassificationapp.entity.FitnessExercise;
import com.example.foodclassificationapp.entity.FoodItem;

import java.util.ArrayList;
import java.util.Calendar;

public interface HomeContract {
    interface View {

        /**
         * Show user's food list
         * @param foodList food list
         */
        void showFoodList(ArrayList<FoodItem> foodList);

        /**
         * show nutrition info
         * @param dailyNutrition DailyNutrition entity
         */
        void showDailyNutrition(DailyNutrition dailyNutrition);

        /**
         * show total calories burned from physical activity
         * @param total total calories burned
         */
        void showTotalCalorieBurn(double total);

        /**
         * show date chosen
         * @param calendar calendar object
         */
        void setDate(Calendar calendar);

        /**
         * show nutrition rate recommended
         * @param brm brm value
         */
        void setRecommendRate(double brm);

        /**
         * share weight for fitness screen
         * @param weight weight
         */
        void shareWeight(float weight);

        /**
         * show user's activity list
         * @param activityList FitnessExercise list
         */
        void showActivityList(ArrayList<FitnessExercise> activityList);
    }

    interface Presenter {

        /**
         * attach view
         * @param view view
         */
        void attachView(View view);

        /**
         * detach view
         */
        void detachView();

        /**
         * get food list from date key
         * @param dateKey dateKey
         */
        void getFoodList(String dateKey);

        /**
         * get activity list from date key
         * @param dateKey dateKey
         */
        void getActivityList(String dateKey);

        /**
         * calculate total daily nutrition from food
         * @return DailyNutrition object
         */
        DailyNutrition calculateDailyNutrition();

        /**
         * calculate nutrition rate recommended for user
         */
        void calculateRecommendRate();

        /**
         * calculate BRM
         * @param height height
         * @param weight weight
         * @param gender gender
         * @param age age
         * @return BRM value
         */
        double calculateBrm(float height, float weight, String gender, int age);

        /**
         * set mode show food or activity list
         */
        void setMode();

        /**
         * get mode show list
         * @return mode list
         */
        boolean getMode();

        /**
         * calculate total calories burn from physical activity
         * @return total calories burned
         */
        double calculateCalorieBurnDaily();
    }
}
