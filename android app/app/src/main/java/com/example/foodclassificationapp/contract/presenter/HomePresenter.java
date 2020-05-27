package com.example.foodclassificationapp.contract.presenter;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.foodclassificationapp.contract.HomeContract;
import com.example.foodclassificationapp.entity.DailyNutrition;
import com.example.foodclassificationapp.entity.FitnessExercise;
import com.example.foodclassificationapp.entity.FoodItem;
import com.example.foodclassificationapp.util.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View homeView;
    private ArrayList<FoodItem> foodList = new ArrayList<>();
    private ArrayList<FitnessExercise> fitnessList = new ArrayList<>();
    private FirebaseAuth fiAuth;
    private boolean foodMode = true;
    private Calendar calendar = Calendar.getInstance();
    private String dateKey = calendar.get(Calendar.DAY_OF_MONTH) + Constant.MONTH.get(calendar.get(Calendar.MONTH));

    public HomePresenter() {
        fiAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void attachView(HomeContract.View view) {
        this.homeView = view;
    }

    @Override
    public void detachView() {
        this.homeView = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void getFoodList(String dateKey) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.HASAGI_DB);
        dbRef.child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid()).child(dateKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    FoodItem foodItem = new FoodItem(
                            String.valueOf(item.child(Constant.NAME).getValue()),
                            String.valueOf(item.child(Constant.CALORIES).getValue()),
                            String.valueOf(item.child(Constant.CARBOHYDRATE).getValue()),
                            String.valueOf(item.child(Constant.FAT).getValue()),
                            String.valueOf(item.child(Constant.PROTEIN).getValue()),
                            String.valueOf(item.child(Constant.IMAGE).getValue()),
                            Boolean.parseBoolean(Objects.requireNonNull(item.child("myFood").getValue()).toString()),
                            String.valueOf(item.child("recipe").getValue())
                    );
                    foodList.add(foodItem);
                }
                if (foodMode) {
                    homeView.showFoodList(foodList);
                }
                homeView.showDailyNutrition(calculateDailyNutrition());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //do nothing
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void getActivityList(String dateKey) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.DAILY_ACTIVITIES);
        dbRef.child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid()).child(dateKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fitnessList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    FitnessExercise fitnessExercise = new FitnessExercise(
                        String.valueOf(item.child(Constant.NAME).getValue()),
                        String.valueOf(item.child(Constant.TIME).getValue()),
                        String.valueOf(item.child(Constant.TYPE).getValue()),
                        String.valueOf(item.child(Constant.IMAGE).getValue()),
                        null,
                        null,
                        String.valueOf(item.child(Constant.CALORIE_BURNED).getValue())
                    );
                    fitnessList.add(fitnessExercise);
                }
                if (!foodMode) {
                    homeView.showActivityList(fitnessList);
                }
                homeView.showTotalCalorieBurn(calculateCalorieBurnDaily());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //do nothing
            }
        });
    }

    @Override
    public DailyNutrition calculateDailyNutrition() {
        float totalCalorie = 0;
        float totalCarbohydrate = 0;
        float totalFat = 0;
        float totalProtein = 0;

        for (FoodItem foodItem : foodList) {
            totalCalorie += Float.parseFloat(foodItem.getCalories());
            totalCarbohydrate += Float.parseFloat(foodItem.getCacbohydrat());
            totalFat += Float.parseFloat(foodItem.getFat());
            totalProtein += Float.parseFloat(foodItem.getProtein());
        }
        return new DailyNutrition(totalCalorie, totalCarbohydrate, totalFat, totalProtein);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void calculateRecommendRate() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.USER_DB);
        dbRef.child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String gender = String.valueOf(dataSnapshot.child(Constant.GENDER).getValue());
                float height = Float.parseFloat(String.valueOf(dataSnapshot.child(Constant.HEIGHT).getValue()));
                float weight = 0;
                for (DataSnapshot weightData : dataSnapshot.child(Constant.WEIGHT).getChildren()) {
                    weight = Float.parseFloat(String.valueOf(weightData.child(Constant.VALUE).getValue()));
                }
                int age = Integer.parseInt(String.valueOf(dataSnapshot.child(Constant.AGE).getValue()));

                homeView.setRecommendRate(calculateBrm(height, weight, gender, age) * 1.375);
                homeView.shareWeight(weight);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    @Override
    public double calculateBrm(float height, float weight, String gender, int age) {
        double bmr;
        if ("Female".equals(gender)) {
            bmr = (10*weight) + (6.25*height) - (5*age) - 161;
        } else {
            bmr = (10*weight) + (6.25*height) - (5*age) + 5;
        }
        return bmr;
    }

    @Override
    public void setMode() {
        foodMode = !foodMode;
    }

    @Override
    public boolean getMode() {
        return foodMode;
    }

    @Override
    public double calculateCalorieBurnDaily() {
        float totalCalorieBurn = 0;
        for (FitnessExercise exercise : fitnessList) {
            totalCalorieBurn += Float.parseFloat(exercise.getCaloriesBurned());
        }
        return (Math.round(totalCalorieBurn * 10.0) / 10.0);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void addNewFood(FoodItem foodItem) {
        if (validateInfoFood(foodItem)) {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.HASAGI_DB)
                    .child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid()).child(dateKey);
            dbRef.push().setValue(foodItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        homeView.showToast("Add Successful");
                    } else {
                        homeView.showToast("Error!");
                    }
                }
            });
        } else {
            homeView.showToast("Value invalid!");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void addUserFood(FoodItem foodItem) {
        if (validateInfoFood(foodItem)) {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("user_food")
                    .child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid());
            dbRef.push().setValue(foodItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // do nothing
                }
            });
        } else {
            homeView.showToast("Value invalid!");
        }
    }

    @Override
    public boolean validateInfoFood(FoodItem foodItem) {
        float calorie = Float.parseFloat(foodItem.getCalories());
        float fat = Float.parseFloat(foodItem.getFat());
        float carbohydrate = Float.parseFloat(foodItem.getCacbohydrat());
        float protein = Float.parseFloat(foodItem.getProtein());
        return calorie > 0 && fat >= 0 && carbohydrate >=0 && protein >= 0;
    }
}
