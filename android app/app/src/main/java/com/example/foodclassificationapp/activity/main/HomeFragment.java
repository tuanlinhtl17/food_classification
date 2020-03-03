package com.example.foodclassificationapp.activity.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.GetImageActivity;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private View homeView;
    private ImageView addFood;
    private static final String[] PERMISSONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int MULTIPLE_PERMISSON = 1;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.home_fragment, container, false);
        initialize();
        setEvents();
        return homeView;
    }

    private void initialize() {
        addFood = homeView.findViewById(R.id.addFood);
    }

    private void setEvents() {
        addFood.setOnClickListener(this);
    }

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

    private boolean checkPermissons() {
        for (String permisson : PERMISSONS) {
            if(ContextCompat.checkSelfPermission(getActivity(), permisson) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissons() {
        ActivityCompat.requestPermissions(getActivity(), PERMISSONS, MULTIPLE_PERMISSON);
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
            break;
        }
    }
}
