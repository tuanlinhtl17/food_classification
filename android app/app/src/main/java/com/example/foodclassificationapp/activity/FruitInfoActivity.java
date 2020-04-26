package com.example.foodclassificationapp.activity;

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
import com.example.foodclassificationapp.activity.main.MainActivity;
import com.example.foodclassificationapp.util.Constant;
import com.example.foodclassificationapp.entity.FoodItem;
import com.example.foodclassificationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
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
    boolean isMyFood;
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

        // code
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        FoodItem foodItem = (FoodItem) bundle.getSerializable("foodObj");

        // review
//        BitmapFactory.Options options = new BitmapFactory.Options();
//
//        options.inSampleSize = 8;
//        Bitmap bitmap;
//
//        bitmap = BitmapFactory.decodeFile(foodItem.getImage(), options);
//        imgPreview.setImageBitmap(bitmap);

        // code
//        previewCapturedImage(foodItem.getImage());
//        setupItemValues(foodItem);
    }

    private void setEvents() {
        back.setOnClickListener(this);
        addFood.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getFoodIntent() {
        Intent intent = getIntent();
        if (getIntent().hasExtra("foodName")) {
//            Bundle bundle = intent.getExtras();
//            FoodItem foodItem = (FoodItem) Objects.requireNonNull(bundle).getSerializable("foodItem");
            foodNameRe = intent.getStringExtra("foodName");
            isMyFood = intent.getBooleanExtra("isMyFood", false);
        } else if (getIntent().hasExtra("foodCamera")) {
            foodNameRe = intent.getStringExtra("foodCamera");
            isMyFood = false;
        }
        getFoodInfo(foodNameRe, isMyFood, new Callback() {
            @Override
            public void setFood(FoodItem food) {
                setupItemValues(food);
            }
        });
    }

    private void getFoodInfo(String foodName, final boolean isMyFood, final Callback callback) {
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
                         isMyFood,
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

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void previewCapturedImage(String filepath) {
//        Log.i("file path: ", filepath);
//        try {
//            File imgFile = new  File(filepath);
//
//            if(imgFile.exists()){
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 8;
//                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
//                imgPreview.setImageBitmap(myBitmap);
//            }
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 8;
//            bitmap = BitmapFactory.decodeFile(filepath, options);
//            imgPreview.setImageBitmap(bitmap);
//        } catch (NullPointerException e) {
//            Log.d("previewCapturedImage", Objects.requireNonNull(e.getMessage()));
//        }
//    }

    public void setupItemValues(FoodItem foodItem) {
        foodName.setText(foodItem.getName());
        itemCalos.setText(String.valueOf(foodItem.getCalories()));
        itemCarbs.setText(String.valueOf(foodItem.getCacbohydrat()));
        itemFats.setText(String.valueOf(foodItem.getFat()));
        itemProts.setText(String.valueOf(foodItem.getProtein()));
        recipe.setText(foodItem.getRecipe());

        if (foodItem.isMyFood()) {
            addFood.setVisibility(View.GONE);
        }

        Glide.with(FruitInfoActivity.this).load(foodItem.getImage()).into(imgPreview);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addFoodMyDay() {
        LocalDate localDate = LocalDate.now();
        sharedPreferences = getApplicationContext().getSharedPreferences(FOOD_IMAGE, MODE_PRIVATE);
        String dateKey = String.valueOf(localDate.getDayOfMonth()) + localDate.getMonth();
        String carbohydrate = (itemCarbs.getText().toString());
        String calories = (itemCalos.getText().toString());
        String fat = (itemFats.getText().toString());
        String img = sharedPreferences.getString(FOOD_IMAGE, "image");
        String protein = (itemProts.getText().toString());

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.HASAGI_DB)
            .child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid()).child(dateKey);

        FoodItem food = new FoodItem(foodNameRe, calories, carbohydrate, fat, protein, img, true, null);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
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
