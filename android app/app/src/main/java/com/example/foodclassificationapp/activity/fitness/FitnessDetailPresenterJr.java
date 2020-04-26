package com.example.foodclassificationapp.activity.fitness;

import androidx.annotation.NonNull;

import com.example.foodclassificationapp.entity.FitnessExercise;
import com.example.foodclassificationapp.util.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FitnessDetailPresenterJr implements FitnessContract.FitnessDetailPresenter {
    private FitnessContract.FitnessDetailView mView;

    @Override
    public void attachView(FitnessContract.FitnessDetailView view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void getFitnessExercise(final String name, final String type, final float calorieBurn, final float time) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.FITNESS);
        dbRef.child(type.toLowerCase()).child(name.toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder exeDes = new StringBuilder();
                for (DataSnapshot repItem : dataSnapshot.child(Constant.DESCRIPTION).getChildren()) {
                    exeDes.append(repItem.getValue()).append("\n\n");
                }
                FitnessExercise fitnessExercise = new FitnessExercise(
                        name,
                        String.valueOf(dataSnapshot.child(Constant.TIME).getValue()),
                        type,
                        null,
                        String.valueOf(dataSnapshot.child(Constant.VIDEO).getValue()),
                        exeDes.toString(),
                        String.valueOf((Math.round(calorieBurn * time * 10.0)) / 10.0)
                );
                mView.showFitnessExerciseDetail(fitnessExercise);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }
}
