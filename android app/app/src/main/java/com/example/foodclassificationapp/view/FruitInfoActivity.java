package com.example.foodclassificationapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodclassificationapp.util.Constant;
import com.example.foodclassificationapp.entity.FoodItem;
import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.view.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class FruitInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String FOOD_IMAGE = "FOOD_IMAGE_SHARED";

    private ImageView imgPreview;
    private TextView itemCalos;
    private TextView itemCarbs;
    private TextView itemFats;
    private TextView itemProts;
    private TextView foodName;
    private TextView recipe;
    private ImageView back;
    private ImageView addFood;

    private FirebaseAuth fiAuth;
    String foodNameRe;
    private String imageFood;
    private boolean isUserFood = false;
    SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_info);
        init();
        setEvents();
        getFoodIntent();
    }

    /**
     * init view
     */
    private void init() {
        imgPreview = findViewById(R.id.imgPreview);
        itemCalos = findViewById(R.id.itemCal);
        itemCarbs = findViewById(R.id.itemCarbs);
        itemFats = findViewById(R.id.itemFats);
        itemProts = findViewById(R.id.itemProts);
        foodName = findViewById(R.id.foodName);
        recipe = findViewById(R.id.recipe);
        back = findViewById(R.id.back);
        addFood = findViewById(R.id.addToMyDay);

        fiAuth = FirebaseAuth.getInstance();
    }

    /**
     * set event listener
     */
    private void setEvents() {
        back.setOnClickListener(this);
        addFood.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * get intent
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getFoodIntent() {
        Intent intent = getIntent();
        if (!getIntent().hasExtra("isMyFood")) {
            if (getIntent().hasExtra("foodName")) {
                foodNameRe = intent.getStringExtra("foodName");
            } else if (getIntent().hasExtra(Constant.FOOD_CAMERA)) {
                foodNameRe = intent.getStringExtra(Constant.FOOD_CAMERA);
                imageFood = intent.getStringExtra("imgPath");
                isUserFood = true;
            }
            getFoodInfo(foodNameRe, new Callback() {
                @Override
                public void setFood(FoodItem food) {
                    setupItemValues(food);
                }
            });
        } else {
            Bundle bundle = intent.getExtras();
            FoodItem foodItem = (FoodItem) Objects.requireNonNull(bundle).getSerializable("foodObj");
            setupItemValues(Objects.requireNonNull(foodItem));
            addFood.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * get food info
     * @param foodName food name
     * @param callback callback
     */
    private void getFoodInfo(String foodName, final Callback callback) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.FOOD_DB).child(foodName);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder foodRecipe = new StringBuilder();
                for (DataSnapshot repItem : dataSnapshot.child("recipe").getChildren()) {
                    String key = repItem.getKey();
                    foodRecipe.append("- ").append(repItem.getValue()).append(" ").append(key).append("\n");
                }
                 FoodItem food = new FoodItem(
                        String.valueOf(dataSnapshot.child("name").getValue()),
                        (String.valueOf(dataSnapshot.child("calories").getValue())),
                        (String.valueOf(dataSnapshot.child("cacbohydrat").getValue())),
                        (String.valueOf(dataSnapshot.child("fat").getValue())),
                        (String.valueOf(dataSnapshot.child("protein").getValue())),
                        String.valueOf(dataSnapshot.child("image").getValue()),
                        Boolean.parseBoolean(String.valueOf(dataSnapshot.child("myFood").getValue())),
                        foodRecipe.toString()
                );
                callback.setFood(food);
                sharedPreferences = getApplicationContext().getSharedPreferences(FOOD_IMAGE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(FOOD_IMAGE, food.getImage());
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    /**
     * show food info
     * @param foodItem food item
     */
    public void setupItemValues(FoodItem foodItem) {
        foodName.setText(foodItem.getName());
        itemCalos.setText(String.format("%s kCal", foodItem.getCalories()));
        itemCarbs.setText(String.format("%s g", foodItem.getCacbohydrat()));
        itemFats.setText(String.format("%s g", foodItem.getFat()));
        itemProts.setText(String.format("%s g", foodItem.getProtein()));
        recipe.setText(foodItem.getRecipe());

        if (!foodItem.isMyFood()) {
            if (isUserFood) {
                foodItem.setImage(imageFood);
            }
            Glide.with(FruitInfoActivity.this).load(foodItem.getImage()).into(imgPreview);
        }
    }

    /**
     * add food daily
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addFoodMyDay() {
        Calendar calendar = Calendar.getInstance();
        sharedPreferences = getApplicationContext().getSharedPreferences(FOOD_IMAGE, MODE_PRIVATE);
        String dateKey = calendar.get(Calendar.DAY_OF_MONTH) + Constant.MONTH.get(calendar.get(Calendar.MONTH));
        String carbohydrate = (itemCarbs.getText().toString().split(" ")[0]);
        String calories = (itemCalos.getText().toString().split(" ")[0]);
        String fat = (itemFats.getText().toString().split(" ")[0]);
        String img = sharedPreferences.getString(FOOD_IMAGE, Constant.IMAGE);
        String protein = (itemProts.getText().toString().split(" ")[0]);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.HASAGI_DB)
            .child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid()).child(dateKey);

        FoodItem food = new FoodItem(foodNameRe, calories, carbohydrate, fat, protein, img, false, null);
        dbRef.push().setValue(food).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(FruitInfoActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FruitInfoActivity.this, MainActivity.class));
                }
            }
        });
    }

    /**
     * set event onClick
     * @param v view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (isUserFood)
                    onBackPressed();
                else finish();
                break;
            case R.id.addToMyDay:
                addFoodMyDay();
                break;
            default:
                break;
        }
    }

    public interface Callback {
        void setFood(FoodItem food);
    }
}
