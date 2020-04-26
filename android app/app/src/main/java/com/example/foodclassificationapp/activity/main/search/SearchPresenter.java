package com.example.foodclassificationapp.activity.main.search;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.foodclassificationapp.entity.FoodItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View searchView;
    private ArrayList<FoodItem> foodList = new ArrayList<>();
    private ArrayList<FoodItem> foodSearchList = new ArrayList<>();

    @Override
    public void attachView(SearchContract.View view) {
        this.searchView = view;
    }

    @Override
    public void detachView() {
        this.searchView = null;
    }

    @Override
    public void getAllFood() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("food");
        dbRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    StringBuilder recipe = new StringBuilder();
                    for (DataSnapshot repItem : item.child("recipe").getChildren()) {
                        String key = repItem.getKey();
                        recipe.append("- ").append(repItem.getValue()).append(" ").append(key).append("\n");
                    }

                    FoodItem foodItem = new FoodItem(
                            String.valueOf(item.child("name").getValue()),
                            (String.valueOf(item.child("calories").getValue())),
                            (String.valueOf(item.child("cacbohydrat").getValue())),
                            (String.valueOf(item.child("fat").getValue())),
                            (String.valueOf(item.child("protein").getValue())),
                            String.valueOf(item.child("image").getValue()),
                            false,
                            recipe.toString()
                    );
                    foodList.add(foodItem);
                }
                searchView.showSearchFood(foodList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    @Override
    public void searchFood(String query) {
        foodSearchList.clear();
        for (FoodItem foodItem : foodList) {
            if (foodItem.getName().toLowerCase().contains(query.toLowerCase())) {
                foodSearchList.add(foodItem);
            }
        }
        searchView.showSearchFood(foodSearchList);
    }
}
