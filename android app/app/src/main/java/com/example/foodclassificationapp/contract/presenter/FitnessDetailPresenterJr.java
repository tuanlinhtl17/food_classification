package com.example.foodclassificationapp.contract.presenter;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.foodclassificationapp.contract.FitnessContract;
import com.example.foodclassificationapp.entity.FitnessExercise;
import com.example.foodclassificationapp.util.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class FitnessDetailPresenterJr implements FitnessContract.FitnessDetailPresenter {
    private FitnessContract.FitnessDetailView mView;
    private FirebaseAuth fiAuth;

    public FitnessDetailPresenterJr() {
        fiAuth = FirebaseAuth.getInstance();
    }

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
                mView.shareImage(String.valueOf(dataSnapshot.child(Constant.IMAGE).getValue()));
                mView.shareType(type);
                mView.showFitnessExerciseDetail(fitnessExercise);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addDailyActivity(FitnessExercise exercise) {
        Calendar calendar = Calendar.getInstance();
        String dateKey = calendar.get(Calendar.DAY_OF_MONTH) + Constant.MONTH.get(calendar.get(Calendar.MONTH));
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.DAILY_ACTIVITIES)
                .child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid()).child(dateKey);
        dbRef.push().setValue(exercise).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mView.showToastAddSuccess();
                }
            }
        });
    }
}
