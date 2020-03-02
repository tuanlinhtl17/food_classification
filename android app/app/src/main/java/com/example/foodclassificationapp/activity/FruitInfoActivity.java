package com.example.foodclassificationapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodclassificationapp.entity.FoodItem;
import com.example.foodclassificationapp.R;

import java.io.File;


public class FruitInfoActivity extends AppCompatActivity {

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
        FoodItem foodItem = (FoodItem) bundle.getSerializable("foodObj");

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
        Log.i("file path: ", filepath);
        try {
            File imgFile = new  File(filepath);

            if(imgFile.exists()){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
                imgPreview.setImageBitmap(myBitmap);
            }
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 8;
//            bitmap = BitmapFactory.decodeFile(filepath, options);
//            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void backPressed(View view) {
        finish();
    }

    public void setupItemValues(FoodItem foodItem) {
        foodName.setText(foodItem.getName());
        itemCalos.setText(String.valueOf(foodItem.getCalories()));
        itemCarbs.setText(String.format("%s gms", foodItem.getCarbs()));
        itemFats.setText(String.format("%s gms", foodItem.getFats()));
        itemProts.setText(String.format("%s gms", foodItem.getProteins()));
    }

}
