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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.GetImageActivity;

import java.time.LocalDate;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private View homeView;
    private ImageView addFood;
    private static final String[] PERMISSONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int MULTIPLE_PERMISSON = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.home_fragment, container, false);
        initialize();
        setEvents();
        return homeView;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initialize() {
        addFood = homeView.findViewById(R.id.addFood);
        TextView date = homeView.findViewById(R.id.activity_main_text_day_of_month);
        TextView dayOfWeek = homeView.findViewById(R.id.activity_main_text_day_of_week);
        LocalDate currentdate = LocalDate.now();
        date.setText(String.valueOf(currentdate.getDayOfMonth()) + currentdate.getMonth());
        dayOfWeek.setText(currentdate.getDayOfWeek().toString());

    }

    private void setEvents() {
        addFood.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFood:
//              if (!checkPermissons()) {
                    requestPermissons();
    //                } else {
                    System.out.println("intent");
                    Intent intent = new Intent(getContext(), GetImageActivity.class);
                    startActivity(intent);
//                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkPermissons() {
        for (String permisson : PERMISSONS) {
            if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), permisson) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void requestPermissons() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), PERMISSONS, MULTIPLE_PERMISSON);
    }

}
