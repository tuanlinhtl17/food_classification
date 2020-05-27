package com.example.foodclassificationapp.view.main.fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.contract.FitnessContract;
import com.example.foodclassificationapp.contract.presenter.FitnessListPresenterJr;
import com.example.foodclassificationapp.util.Constant;
import com.example.foodclassificationapp.control.ExerciseListAdapter;
import com.example.foodclassificationapp.entity.FitnessExercise;

import java.util.ArrayList;

public class FitnessActivity extends AppCompatActivity implements FitnessContract.FitnessListView {

    private RecyclerView recyclerView;
    private String type;
    private FitnessListPresenterJr presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
        init();
        initPresenter();
        getFitnessExercise();
    }

    /**
     * init view
     */
    private void init() {
        getTypeIntent();
        TextView fitnessLevel = findViewById(R.id.level);
        ImageView backImage = findViewById(R.id.back);
        ImageView backgroundImage = findViewById(R.id.background_activity);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fitnessLevel.setText(type.toUpperCase());
        recyclerView = findViewById(R.id.exerciseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(FitnessActivity.this));

        if ("OUTDOOR ACTIVITIES".equals(type)) {
            backgroundImage.setImageResource(R.drawable.outdoor);
        } else if ("SPORTS".equals(type)) {
            backgroundImage.setImageResource(R.drawable.sports);
        } else {
            backgroundImage.setImageResource(R.drawable.fitness_background);
        }
    }

    /**
     * init presenter
     */
    private void initPresenter() {
        presenter = new FitnessListPresenterJr();
        presenter.attachView(this);
    }

    /**
     * get intent
     */
    private void getTypeIntent() {
        Intent intent = getIntent();
        type = intent.getStringExtra(Constant.TYPE);
    }

    /**
     * get activity list
     */
    private void getFitnessExercise() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constant.WEIGHT, MODE_PRIVATE);
        float weight = sharedPreferences.getFloat(Constant.WEIGHT, 0);
        presenter.getFitnessExercise(type, weight);
    }

    @Override
    public void showActivitiesList(ArrayList<FitnessExercise> exerciseList) {
        ExerciseListAdapter exerciseListAdapter = new ExerciseListAdapter(exerciseList, FitnessActivity.this);
        recyclerView.setAdapter(exerciseListAdapter);
    }
}
