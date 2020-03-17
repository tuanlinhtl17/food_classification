package com.example.foodclassificationapp.activity.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.content.ContextCompat;
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
    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int MULTIPLE_PERMISSION = 1;

    private LocalDate currentDate;
    private ArrayList<FoodItem> foodList = new ArrayList<>();
    private FoodListAdapter foodListAdapter;
    private String dateKey;

    private FirebaseAuth fiAuth;
//    private FirebaseAuth.AuthStateListener fiAuthStateListener;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.home_fragment, container, false);
        initialize();
        setEvents();
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

    @SuppressLint("SetTextI18n")
    private void setValue(List<FoodItem> foodList) {
        double totalCalories = 0.0;
        double totalCarb = 0.0;
        double totalFats = 0.0;
        double totalProtein = 0.0;
        for (FoodItem foodItem : foodList) {
            totalCalories += foodItem.getCalories();
            totalCarb += foodItem.getCarbs();
            totalFats += foodItem.getFats();
            totalProtein += foodItem.getProteins();
        }
        totalCalo.setText(Double.toString(totalCalories));
        totalFat.setText(Double.toString(totalFats));
        totalCacbo.setText(Double.toString(totalCarb));
        totalPro.setText(Double.toString(totalProtein));
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
//              if (!checkPermissons()) {
                    requestPermissions();
    //                } else {
                    Intent intent = new Intent(getContext(), GetImageActivity.class);
                    startActivity(intent);
//                }
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
    private boolean checkPermissons() {
        for (String permisson : PERMISSIONS) {
            if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), permisson) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
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
                            String.valueOf(item.child("name").getValue()),
                            Double.parseDouble(String.valueOf(item.child("calories").getValue())),
                            Double.parseDouble(String.valueOf(item.child("cacbohydrat").getValue())),
                            Double.parseDouble(String.valueOf(item.child("fat").getValue())),
                            Double.parseDouble(String.valueOf(item.child("protein").getValue())),
                            String.valueOf(item.child("image").getValue())
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
}
