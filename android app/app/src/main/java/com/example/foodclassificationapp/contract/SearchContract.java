package com.example.foodclassificationapp.contract;

import com.example.foodclassificationapp.entity.FoodItem;

import java.util.ArrayList;

public interface SearchContract {
    interface View {
        void showSearchFood(ArrayList<FoodItem> foodItems);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void getAllFood();

        void searchFood(String query);
    }
}
