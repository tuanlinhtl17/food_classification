package com.example.foodclassificationapp.activity.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.constant.Constant;
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
    private DatabaseReference databaseReference;
    private List<FoodItem> foodList;
    private RecyclerView recyclerView;
    private TextView searchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchView = inflater.inflate(R.layout.search_fragment, container, false);
        initView();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_DB).child("food");
        return searchView;
    }

    private void initView() {
        recyclerView = searchView.findViewById(R.id.foodSearchList);
        searchBar = searchBar.findViewById(R.id.searchBar);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        foodList = new ArrayList<>();
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            foodList.add(item.getValue(FoodItem.class));
                        }
                        FoodListAdapter foodListAdapter = new FoodListAdapter(foodList, getContext());
                        recyclerView.setAdapter(foodListAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    /* do nothing */
                }
            });
        }
        if (searchBar != null) {
            searchBar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    /* do nothing */
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchFood((String) s);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    searchFood((String) searchBar.getText());
                }
            });
        }
    }

    private void searchFood(String input) {
        List<FoodItem> foodSearchList = new ArrayList<>();
        for (FoodItem foodItem : foodSearchList) {
            if (foodItem.getName().toLowerCase().contains(input.toLowerCase())) {
                foodSearchList.add(foodItem);
            }
        }
//        FoodListAdapter foodListAdapter = new FoodListAdapter(foodSearchList, getContext());
//        recyclerView.setAdapter(foodListAdapter);
    }

    @Override
    public void onClick(View v) {
        /* set event click */
    }
}
