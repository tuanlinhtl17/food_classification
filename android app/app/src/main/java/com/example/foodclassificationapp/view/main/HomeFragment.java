package com.example.foodclassificationapp.view.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.contract.HomeContract;
import com.example.foodclassificationapp.contract.presenter.HomePresenter;
import com.example.foodclassificationapp.control.ExerciseListAdapter;
import com.example.foodclassificationapp.entity.DailyNutrition;
import com.example.foodclassificationapp.entity.FitnessExercise;
import com.example.foodclassificationapp.util.Constant;
import com.example.foodclassificationapp.control.FoodListAdapter;
import com.example.foodclassificationapp.entity.FoodItem;
import com.example.foodclassificationapp.view.CameraActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HomeFragment extends Fragment implements HomeContract.View, View.OnClickListener {

    private View homeView;
    private ImageView addFood;
    private ImageView datePicker;
    private ImageView preDay;
    private ImageView nextDay;
    private ImageView changeTab;
    private RecyclerView recyclerView;
    private TextView date;
    private TextView totalCalorie;
    private TextView totalCarbohydrate;
    private TextView totalFat;
    private TextView totalPro;
    private TextView recommendCal;
    private TextView recommendCarbohydrate;
    private TextView recommendFat;
    private TextView recommendPro;
    private TextView totalCalorieBurn;
    private TextView title;

    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    private static final int MULTIPLE_PERMISSION = 1;

    private String dateKey;
    private Calendar calendar;

    private HomePresenter homePresenter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.home_fragment, container, false);
        initialize();
        requestPermissions();
        setEvents();
        initPresenter();
        getDataList();
        homePresenter.calculateRecommendRate();
        return homeView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initialize() {
        addFood = homeView.findViewById(R.id.addFood);
        date = homeView.findViewById(R.id.activity_main_text_day_of_month);
        changeTab = homeView.findViewById(R.id.changeTab);
        datePicker = homeView.findViewById(R.id.datePicker);
        nextDay = homeView.findViewById(R.id.nextDay);
        preDay = homeView.findViewById(R.id.preDay);
        totalCalorie = homeView.findViewById(R.id.totalCal);
        totalCarbohydrate = homeView.findViewById(R.id.totalCarbs);
        totalFat = homeView.findViewById(R.id.totalFats);
        totalPro = homeView.findViewById(R.id.totalProts);
        recommendCal = homeView.findViewById(R.id.calRecommend);
        recommendCarbohydrate = homeView.findViewById(R.id.carbsRecommend);
        recommendFat = homeView.findViewById(R.id.fatsRecommend);
        recommendPro = homeView.findViewById(R.id.protsRecommend);
        totalCalorieBurn = homeView.findViewById(R.id.totalCalorieBurn);
        title = homeView.findViewById(R.id.title);

        recyclerView = homeView.findViewById(R.id.foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        calendar = Calendar.getInstance();
        setDate(calendar);
    }

    private void initPresenter(){
        homePresenter = new HomePresenter();
        homePresenter.attachView(this);
    }

    private void setEvents() {
        addFood.setOnClickListener(this);
        datePicker.setOnClickListener(this);
        nextDay.setOnClickListener(this);
        preDay.setOnClickListener(this);
        changeTab.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDate(Calendar calendar) {
        dateKey = calendar.get(Calendar.DAY_OF_MONTH) + Constant.MONTH.get(calendar.get(Calendar.MONTH));
        date.setText(dateKey);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFood:
                Intent intent = new Intent(getContext(), CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.datePicker:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(year, month, dayOfMonth);
                            setDate(calendar);
                            getDataList();
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
                break;
            case R.id.nextDay:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    setDate(calendar);
                    getDataList();
                }
                break;
            case R.id.preDay:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    setDate(calendar);
                    getDataList();
                }
                break;
            case R.id.changeTab:
                homePresenter.setMode();
                setTitle();
                showList();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void requestPermissions() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), PERMISSIONS, MULTIPLE_PERMISSION);
    }

    @Override
    public void setRecommendRate(double bmr) {
        recommendCal.setText(String.valueOf((int) bmr));
        recommendCarbohydrate.setText(String.valueOf((int) (bmr*0.45*0.25)));
        recommendPro.setText(String.valueOf((int) (bmr*0.3*0.25)));
        recommendFat.setText(String.valueOf((int) ((bmr*0.25)/9)));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void shareWeight(float weight) {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(Constant.WEIGHT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(Constant.WEIGHT, weight);
        editor.apply();
    }

    @Override
    public void showActivityList(ArrayList<FitnessExercise> activityList) {
        ExerciseListAdapter exerciseListAdapter = new ExerciseListAdapter(activityList, getContext());
        recyclerView.setAdapter(exerciseListAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showList() {
        if (homePresenter.getMode()) {
            homePresenter.getFoodList(dateKey);
        } else homePresenter.getActivityList(dateKey);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homePresenter.detachView();
    }

    @Override
    public void showFoodList(ArrayList<FoodItem> foodList) {
        FoodListAdapter foodListAdapter = new FoodListAdapter(foodList, getContext());
        recyclerView.setAdapter(foodListAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showDailyNutrition(DailyNutrition dailyNutrition) {
        totalCalorie.setText(Float.toString((float) (Math.round(dailyNutrition.getTotalCalorie() * 100.0) / 100.0)));
        totalFat.setText(Float.toString((float) (Math.round(dailyNutrition.getTotalFat() * 100.0) / 100.0)));
        totalCarbohydrate.setText(Float.toString((float) (Math.round(dailyNutrition.getTotalCarbohydrate() * 100.0) / 100.0)));
        totalPro.setText(Float.toString((float) (Math.round(dailyNutrition.getTotalProtein() * 100.0) / 100.0)));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showTotalCalorieBurn(double total) {
        totalCalorieBurn.setText((total) + " kCal");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDataList() {
        homePresenter.getActivityList(dateKey);
        homePresenter.getFoodList(dateKey);
    }

    @SuppressLint("SetTextI18n")
    private void setTitle() {
        if (homePresenter.getMode()) {
            title.setText("My Food");
        } else title.setText("My Activities");
    }
}

