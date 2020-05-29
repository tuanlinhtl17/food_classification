package com.example.foodclassificationapp.view.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.contract.SearchContract;
import com.example.foodclassificationapp.contract.presenter.SearchPresenter;
import com.example.foodclassificationapp.control.FoodListAdapter;
import com.example.foodclassificationapp.entity.FoodItem;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment implements SearchContract.View, View.OnClickListener {

    private View searchView;
    private RecyclerView recyclerView;
    private EditText searchBar;

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

    /**
     * init view
     */
    private void initView() {
        recyclerView = searchView.findViewById(R.id.foodSearchList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchBar = searchView.findViewById(R.id.searchBar);
    }

    /**
     * init presenter
     */
    private void initPresenter() {
        searchPresenter = new SearchPresenter();
        searchPresenter.attachView(this);
    }

    @SuppressLint("ClickableViewAccessibility")
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
        searchBar.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP && (event.getRawX() >= (searchBar.getRight() - searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                    getSpeechInput();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * call intent Speech TO Text
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(getContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchBar.setText(Objects.requireNonNull(result).get(0));
        }
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
