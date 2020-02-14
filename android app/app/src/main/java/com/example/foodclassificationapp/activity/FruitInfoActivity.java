package com.example.foodclassificationapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodclassificationapp.entity.FoodEntity;
import com.example.foodclassificationapp.R;


public class FruitInfoActivity extends AppCompatActivity {

    private Bitmap bitmap;

    private ImageView imgPreview;
    private TextView itemCalos, itemCarbs, itemFats, itemProts, foodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_info);
        init();
    }

    private void init() {
        imgPreview = findViewById(R.id.imgPreview);
        itemCalos = findViewById(R.id.itemCal);
        itemCarbs = findViewById(R.id.itemCarbs);
        itemFats = findViewById(R.id.itemFats);
        itemProts = findViewById(R.id.itemProts);
        foodName = findViewById(R.id.foodName);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        FoodEntity foodItem = (FoodEntity) bundle.getSerializable("foodObj");

//        BitmapFactory.Options options = new BitmapFactory.Options();
//
//        options.inSampleSize = 8;
//        Bitmap bitmap;
//
//        bitmap = BitmapFactory.decodeFile(foodItem.getImage(), options);
//        imgPreview.setImageBitmap(bitmap);

        previewCapturedImage(foodItem.getImage());
        setupItemValues(foodItem);
    }

    private void previewCapturedImage(String filepath) {
//        Log.i(TAG, "previewCapturedImage()");
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeFile(filepath, options);
            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void backPressed(View view) {
        finish();
    }

    public void setupItemValues(FoodEntity foodEntity) {
        foodName.setText(foodEntity.getName());
        itemCalos.setText(String.valueOf(foodEntity.getCalories()));
        itemCarbs.setText(String.format("%s gms", foodEntity.getCarbs()));
        itemFats.setText(String.format("%s gms", foodEntity.getFats()));
        itemProts.setText(String.format("%s gms", foodEntity.getProteins()));
    }

}
