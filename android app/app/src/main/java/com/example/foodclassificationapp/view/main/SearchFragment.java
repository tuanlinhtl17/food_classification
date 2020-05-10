package com.example.foodclassificationapp.view.main;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.contract.SearchContract;
import com.example.foodclassificationapp.contract.presenter.SearchPresenter;
import com.example.foodclassificationapp.control.FoodListAdapter;
import com.example.foodclassificationapp.entity.FoodItem;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchContract.View, View.OnClickListener {

    private View searchView;
    private RecyclerView recyclerView;
    private TextView searchBar;

    private SearchContract.Presenter searchPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchView = inflater.inflate(R.layout.search_fragment, container, false);
        initView();
        initPresenter();
        searchPresenter.getAllFood();
        setEvent();
        return searchView;
    }

    private void initView() {
        recyclerView = searchView.findViewById(R.id.foodSearchList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchBar = searchView.findViewById(R.id.searchBar);
    }

    private void initPresenter() {
        searchPresenter = new SearchPresenter();
        searchPresenter.attachView(this);
    }

    private void setEvent() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* do nothing */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchPresenter.searchFood(searchBar.getText().toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        /* set event click */
    }

    @Override
    public void showSearchFood(ArrayList<FoodItem> foodItems) {
        FoodListAdapter foodListAdapter = new FoodListAdapter(foodItems, getContext());
        recyclerView.setAdapter(foodListAdapter);
    }
}
