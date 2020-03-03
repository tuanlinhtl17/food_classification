package com.example.foodclassificationapp.activity.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodclassificationapp.R;

public class UserFragment extends Fragment implements View.OnClickListener {

    private View userView;

    public UserFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userView = inflater.inflate(R.layout.user_fragment, container, false);
        return userView;
    }

    @Override
    public void onClick(View v) {

    }
}
