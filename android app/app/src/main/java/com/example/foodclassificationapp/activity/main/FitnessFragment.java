package com.example.foodclassificationapp.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.FitnessActivity;
import com.example.foodclassificationapp.constant.Constant;

public class FitnessFragment extends Fragment implements View.OnClickListener {
    private View fitnessView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fitnessView = inflater.inflate(R.layout.fitness_fragment, container, false);
        init();
        return fitnessView;
    }

    private void init() {
        androidx.cardview.widget.CardView exercise = fitnessView.findViewById(R.id.exercise);
        androidx.cardview.widget.CardView outdoor = fitnessView.findViewById(R.id.outdoor);
        androidx.cardview.widget.CardView sports = fitnessView.findViewById(R.id.sports);

        exercise.setOnClickListener(this);
        outdoor.setOnClickListener(this);
        sports.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), FitnessActivity.class);
        switch (v.getId()) {
            case R.id.exercise:
                intent.putExtra(Constant.TYPE, "EXERCISE");
                startActivity(intent);
                break;
            case R.id.outdoor:
                intent.putExtra(Constant.TYPE, "OUTDOOR ACTIVITIES");
                startActivity(intent);
                break;
            case R.id.sports:
                intent.putExtra(Constant.TYPE, "SPORTS");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
