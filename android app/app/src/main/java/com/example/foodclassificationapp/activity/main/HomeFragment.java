package com.example.foodclassificationapp.activity.main;

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
import com.example.foodclassificationapp.activity.GetImageActivity;
import com.example.foodclassificationapp.constant.Constant;
import com.example.foodclassificationapp.control.FoodListAdapter;
import com.example.foodclassificationapp.entity.FoodItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private View homeView;
    private ImageView addFood;
    private ImageView datePicker;
    private ImageView preDay;
    private ImageView nextDay;
    private RecyclerView recyclerView;
    private TextView date;
    private TextView dayOfWeek;
    private TextView totalCalo;
    private TextView totalCacbo;
    private TextView totalFat;
    private TextView totalPro;
    private TextView recommendCal;
    private TextView recommendCarb;
    private TextView recommendFat;
    private TextView recommendPro;

    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };
    private static final int MULTIPLE_PERMISSION = 1;

    private LocalDate currentDate;
    private ArrayList<FoodItem> foodList = new ArrayList<>();
    private FoodListAdapter foodListAdapter;
    private String dateKey;

    private FirebaseAuth fiAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.home_fragment, container, false);
        initialize();
        requestPermissions();
        setEvents();
        calRecommendRate();
        getFoodList();
        return homeView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initialize() {
        addFood = homeView.findViewById(R.id.addFood);
        date = homeView.findViewById(R.id.activity_main_text_day_of_month);
        dayOfWeek = homeView.findViewById(R.id.activity_main_text_day_of_week);
        datePicker = homeView.findViewById(R.id.datePicker);
        nextDay = homeView.findViewById(R.id.nextDay);
        preDay = homeView.findViewById(R.id.preDay);
        totalCalo = homeView.findViewById(R.id.totalCal);
        totalCacbo = homeView.findViewById(R.id.totalCarbs);
        totalFat = homeView.findViewById(R.id.totalFats);
        totalPro = homeView.findViewById(R.id.totalProts);
        recommendCal = homeView.findViewById(R.id.calRecommend);
        recommendCarb = homeView.findViewById(R.id.carbsRecommend);
        recommendFat = homeView.findViewById(R.id.fatsRecommend);
        recommendPro = homeView.findViewById(R.id.protsRecommend);

        recyclerView = homeView.findViewById(R.id.foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fiAuth = FirebaseAuth.getInstance();

        currentDate = LocalDate.now();
        setDate(currentDate);
    }

    private void setEvents() {
        addFood.setOnClickListener(this);
        datePicker.setOnClickListener(this);
        nextDay.setOnClickListener(this);
        preDay.setOnClickListener(this);
    }

    @SuppressLint({"SetTextI18n"})
    private void setValue(List<FoodItem> foodList) {
        float totalCalories = 0;
        float totalCarb = 0;
        float totalFats = 0;
        float totalProtein = 0;

        for (FoodItem foodItem : foodList) {
            totalCalories += Float.parseFloat(foodItem.getCalories());
            totalCarb += Float.parseFloat(foodItem.getCacbohydrat());
            totalFats += Float.parseFloat(foodItem.getFat());
            totalProtein += Float.parseFloat(foodItem.getProtein());
        }
        totalCalo.setText(Float.toString((float) (Math.round(totalCalories * 100.0) / 100.0)));
        totalFat.setText(Float.toString((float) (Math.round(totalFats * 100.0) / 100.0)));
        totalCacbo.setText(Float.toString((float) (Math.round(totalCarb * 100.0) / 100.0)));
        totalPro.setText(Float.toString((float) (Math.round(totalProtein * 100.0) / 100.0)));

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDate(LocalDate localDate) {
        dateKey = String.valueOf(localDate.getDayOfMonth()) + localDate.getMonth();
        dayOfWeek.setText(localDate.getDayOfWeek().toString());
        date.setText(dateKey);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFood:
                requestPermissions();
                Intent intent = new Intent(getContext(), GetImageActivity.class);
                startActivity(intent);
                break;
            case R.id.datePicker:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            currentDate = LocalDate.of(year, month+1, dayOfMonth);
                            setDate(currentDate);
                            getFoodList();
                        }
                    }, currentDate.getYear(), currentDate.getMonthValue()-1, currentDate.getDayOfMonth());
                    datePickerDialog.show();
                }
                break;
            case R.id.nextDay:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    currentDate = currentDate.plusDays(1);
                    setDate(currentDate);
                    getFoodList();
                }
                break;
            case R.id.preDay:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    currentDate = currentDate.minusDays(1);
                    setDate(currentDate);
                    getFoodList();
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void requestPermissions() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), PERMISSIONS, MULTIPLE_PERMISSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getFoodList() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.HASAGI_DB);
        dbRef.child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid()).child(dateKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    FoodItem foodItem = new FoodItem(
                            String.valueOf(item.child(Constant.NAME).getValue()),
                            String.valueOf(item.child(Constant.CALORIES).getValue()),
                            String.valueOf(item.child(Constant.CACBOHYDRAT).getValue()),
                            String.valueOf(item.child(Constant.FAT).getValue()),
                            String.valueOf(item.child(Constant.PROTEIN).getValue()),
                            String.valueOf(item.child(Constant.IMAGE).getValue()),
                            true,
                            null
                    );
                    foodList.add(foodItem);
                }
                foodListAdapter = new FoodListAdapter(foodList, getContext());
                recyclerView.setAdapter(foodListAdapter);
                setValue(foodList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void calRecommendRate () {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.USER_DB);
        dbRef.child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String gender = String.valueOf(dataSnapshot.child("gender").getValue());
                float height = Float.parseFloat(String.valueOf(dataSnapshot.child(Constant.HEIGHT).getValue()));
                float weight = 0;
                for (DataSnapshot weightData : dataSnapshot.child(Constant.WEIGHT).getChildren()) {
                    weight = Float.parseFloat(String.valueOf(weightData.child("value").getValue()));
                }
                int age = Integer.parseInt(String.valueOf(dataSnapshot.child(Constant.AGE).getValue()));

                SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(Constant.WEIGHT, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(Constant.WEIGHT, weight);
                editor.apply();

                double bmr;
                if ("Female".equals(gender)) {
                    bmr = (10*weight) + (6.25*height) - (5*age) - 161;
                } else {
                    bmr = (10*weight) + (6.25*height) - (5*age) + 5;
                }
                setRecommendRate(bmr * 1.375);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    private void setRecommendRate(double bmr) {
        recommendCal.setText(String.valueOf((int) bmr));
        recommendCarb.setText(String.valueOf((int) (bmr*0.45*0.25)));
        recommendPro.setText(String.valueOf((int) (bmr*0.3*0.25)));
        recommendFat.setText(String.valueOf((int) ((bmr*0.25)/9)));
    }

}

