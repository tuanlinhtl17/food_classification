package com.example.foodclassificationapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.foodclassificationapp.R;

public class MainActivity extends AppCompatActivity {

    ImageView btnAddFood;
    public static final String[] PERMISSONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int MULTIPLE_PERMISSON = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        btnAddFood = findViewById(R.id.addFood);
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissons()) {
                    requestPermissons();
                } else {
                    Intent intent = new Intent(getApplicationContext(), GetImageActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkPermissons() {
        for (String permisson : PERMISSONS) {
            if(ContextCompat.checkSelfPermission(this, permisson) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissons() {
        ActivityCompat.requestPermissions(this, PERMISSONS, MULTIPLE_PERMISSON);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSON: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                } else {
                    // show explanation
                }
            }
            return;
        }
    }
}
