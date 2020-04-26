package com.example.foodclassificationapp.activity.fitness;

import androidx.annotation.NonNull;

import com.example.foodclassificationapp.entity.FitnessExercise;
import com.example.foodclassificationapp.util.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FitnessListPresenterJr implements FitnessContract.FitnessListPresenter {
    private FitnessContract.FitnessListView fitnessView;
    private ArrayList<FitnessExercise> exerciseList = new ArrayList<>();

    @Override
    public void attachView(FitnessContract.FitnessListView view) {
        this.fitnessView = view;
    }

    @Override
    public void detachView() {
        this.fitnessView = null;
    }

    @Override
    public void getFitnessExercise(final String type, final float weight) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.FITNESS);
        dbRef.child(type.toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    int time = Integer.parseInt(String.valueOf(item.child(Constant.TIME).getValue()));
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
                fitnessView.showActivitiesList(exerciseList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }
}
