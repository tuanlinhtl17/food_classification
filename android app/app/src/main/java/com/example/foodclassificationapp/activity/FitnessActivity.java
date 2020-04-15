package com.example.foodclassificationapp.activity;

import androidx.annotation.NonNull;
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
import com.example.foodclassificationapp.constant.Constant;
import com.example.foodclassificationapp.control.ExerciseListAdapter;
import com.example.foodclassificationapp.entity.FitnessExercise;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FitnessActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<FitnessExercise> exerciseList = new ArrayList<>();
    private ExerciseListAdapter exerciseListAdapter;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
        init();
        getFitnessExercise();
    }

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

    private void getTypeIntent() {
        Intent intent = getIntent();
        type = intent.getStringExtra(Constant.TYPE);
    }

    private void getFitnessExercise() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.FITNESS);
        dbRef.child(type.toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseList.clear();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("weight", MODE_PRIVATE);
                float weight = sharedPreferences.getFloat("weight", 0);
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    float time = Float.parseFloat(String.valueOf(item.child(Constant.TIME).getValue()));
                    float mets = Float.parseFloat(String.valueOf(item.child(Constant.METS).getValue()));
                    double caloBurn = Math.round(((time * mets * 3.5 * weight)/200)*10.0) / 10.0;
                    FitnessExercise fitnessExercise = new FitnessExercise(
                            String.valueOf(item.child(Constant.NAME).getValue()),
                            String.valueOf(time),
                            type,
                            String.valueOf(item.child(Constant.IMAGE).getValue()),
                            String.valueOf(item.child(Constant.VIDEO).getValue()),
                            null,
                            String.valueOf(caloBurn)
                    );
                    exerciseList.add(fitnessExercise);
                }
                exerciseListAdapter = new ExerciseListAdapter(exerciseList, FitnessActivity.this);
                recyclerView.setAdapter(exerciseListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }
}
