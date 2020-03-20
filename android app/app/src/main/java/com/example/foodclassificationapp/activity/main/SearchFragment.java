package com.example.foodclassificationapp.activity.main;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.control.FoodListAdapter;
import com.example.foodclassificationapp.entity.FoodItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private View searchView;
    private List<FoodItem> foodList = new ArrayList<>();
    private List<FoodItem> foodSearchList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView searchBar;
    private FoodListAdapter foodListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchView = inflater.inflate(R.layout.search_fragment, container, false);
        initView();
        getAllFood();
        setEvent();
        return searchView;
    }

    private void initView() {
        recyclerView = searchView.findViewById(R.id.foodSearchList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchBar = searchView.findViewById(R.id.searchBar);
    }

    private void setEvent() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* do nothing */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchFood(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFood(searchBar.getText().toString());
            }
        });
    }

    private void getAllFood() {
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
                foodListAdapter = new FoodListAdapter(foodList, getContext());
                recyclerView.setAdapter(foodListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    private void searchFood(String input) {
        foodSearchList.clear();
        for (FoodItem foodItem : foodList) {
            if (foodItem.getName().toLowerCase().contains(input.toLowerCase())) {
                foodSearchList.add(foodItem);
            }
        }
        foodListAdapter = new FoodListAdapter(foodSearchList, getContext());
        recyclerView.setAdapter(foodListAdapter);
    }

    @Override
    public void onClick(View v) {
        /* set event click */
    }
}
