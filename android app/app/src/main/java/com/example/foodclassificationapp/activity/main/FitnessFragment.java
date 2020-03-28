package com.example.foodclassificationapp.activity.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodclassificationapp.R;

public class FitnessFragment extends Fragment implements View.OnClickListener {
    private View fitnessView;
    private androidx.cardview.widget.CardView beginner;
    private androidx.cardview.widget.CardView intermediate;
    private androidx.cardview.widget.CardView advanced;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fitnessView = inflater.inflate(R.layout.fitness_fragment, container, false);
        init();
        return fitnessView;
    }

    private void init() {
        beginner = fitnessView.findViewById(R.id.beginner);
        intermediate = fitnessView.findViewById(R.id.intermediate);
        advanced = fitnessView.findViewById(R.id.advanced);

        beginner.setOnClickListener(this);
        intermediate.setOnClickListener(this);
        advanced.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.beginner:
                break;
            case R.id.intermediate:
                break;
            case R.id.advanced:
                break;
            default:
                break;
        }
    }
}
