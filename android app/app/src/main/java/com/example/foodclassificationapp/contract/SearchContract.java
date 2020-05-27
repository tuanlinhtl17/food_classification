package com.example.foodclassificationapp.contract;

import com.example.foodclassificationapp.entity.FoodItem;

import java.util.ArrayList;

public interface SearchContract {
    interface View {
        /**
         * show food list
         * @param foodItems food list
         */
        void showSearchFood(ArrayList<FoodItem> foodItems);
    }

    interface Presenter {
        /**
         * attachView
         * @param view view
         */
        void attachView(View view);

        /**
         * detachView
         */
        void detachView();

        /**
         * get food list
         */
        void getAllFood();

        /**
         * search food
         * @param query keyword
         */
        void searchFood(String query);
    }
}
