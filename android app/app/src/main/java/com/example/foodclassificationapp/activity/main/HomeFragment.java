package com.example.foodclassificationapp.activity.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener, FoodListAdapter.OnFoodListener {

    private View homeView;
    private ImageView addFood;
    private ImageView datePicker;
    private ImageView preDay;
    private ImageView nextDay;
    private RecyclerView recyclerView;
    private TextView date;
    private TextView dayOfWeek;
    TextView totalCalo;
    TextView totalCacbo;
    TextView totalFat;
    TextView totalPro;
    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int MULTIPLE_PERMISSION = 1;

    private LocalDate currentDate;
    private List<FoodItem> foodList = new ArrayList<>();
    private FoodListAdapter foodListAdapter;
    String dateKey;

    private FirebaseAuth fiAuth;
    private FirebaseDatabase firebaseDb;
    private FirebaseAuth.AuthStateListener fiAuthStateListener;
    private StorageReference storageRef;
    private DatabaseReference dbRef;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.home_fragment, container, false);
        initialize();
        getFoodList();
        setEvents();
        return homeView;
    }

    @SuppressLint("SetTextI18n")
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
        firebaseDb = FirebaseDatabase.getInstance();

        currentDate = LocalDate.now();
        date.setText((currentDate.getDayOfMonth()) + " " + currentDate.getMonth());
        dateKey = String.valueOf(currentDate.getDayOfMonth()) + currentDate.getMonth();
        dayOfWeek.setText(currentDate.getDayOfWeek().toString());
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFood:
//              if (!checkPermissons()) {
                    requestPermissons();
    //                } else {
                    Intent intent = new Intent(getContext(), GetImageActivity.class);
                    startActivity(intent);
//                }
                break;
            case R.id.datePicker:
                break;
            case R.id.nextDay:
                break;
            case R.id.preDay:
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
    private void requestPermissons() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), PERMISSIONS, MULTIPLE_PERMISSION);
    }

    @Override
    public void onStart() {
        super.onStart();
        fiAuth.addAuthStateListener(fiAuthStateListener);
    }

    @Override
    public void onFoodClick(int position) {

    }

    private void getFoodList() {
        fiAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (fiAuth.getCurrentUser() != null) {
                    storageRef = FirebaseStorage.getInstance().getReference();
                    dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.HASAGI_DB);
                    dbRef.child(fiAuth.getCurrentUser().getUid()).child(dateKey).addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
//                            Toast.makeText(getContext(), foodList.size(), Toast.LENGTH_SHORT).show();
                            foodListAdapter = new FoodListAdapter(foodList, getContext());
                            recyclerView.setAdapter(foodListAdapter);
                            setValue(foodList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // do nothing
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Error load food", Toast.LENGTH_SHORT).show();
                }
            }
        };

    }
}
