package com.example.foodclassificationapp.activity.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodclassificationapp.R;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private View searchView;

    public SearchFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchView = inflater.inflate(R.layout.search_fragment, container, false);
        return searchView;
    }

    @Override
    public void onClick(View v) {

    }
}
