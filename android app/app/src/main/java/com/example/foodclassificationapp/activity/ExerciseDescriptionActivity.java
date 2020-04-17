package com.example.foodclassificationapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.constant.Constant;
import com.example.foodclassificationapp.entity.FitnessExercise;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class ExerciseDescriptionActivity extends AppCompatActivity implements View.OnClickListener{

    private YouTubePlayerView youTubePlayer;
    private TextView exeTitle;
    private TextView exeName;
    private EditText exeTime;
    private TextView exeCalBurn;
    private TextView exeDescription;
    private ImageView backgroundImg;
    private ImageButton plus;
    private ImageButton reduce;
    private ImageView back;

    private String exerciseName;
    private String activityType;
    private float caloBurn;
    private float time;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_description);
        init();
        setEvent();
        getExeDescription();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        youTubePlayer = findViewById(R.id.video);
        getLifecycle().addObserver(youTubePlayer);
        backgroundImg = findViewById(R.id.backgroundImg);
        exeTitle = findViewById(R.id.exeTitle);
        exeName = findViewById(R.id.exeName);
        exeTime = findViewById(R.id.exeDesTime);
        plus = findViewById(R.id.plusTime);
        reduce = findViewById(R.id.reduceTime);
        exeDescription = findViewById(R.id.exeDes);
        exeCalBurn = findViewById(R.id.exeDesCal);
        back = findViewById(R.id.back);

        Intent intent = getIntent();
        exerciseName = intent.getStringExtra(Constant.NAME);
        activityType = intent.getStringExtra(Constant.TYPE);
        caloBurn = intent.getFloatExtra(Constant.CALORIE_BURN, 0);
        time = intent.getFloatExtra(Constant.TIME, 0);
        if ("EXERCISE".equals(activityType)) {
            backgroundImg.setVisibility(View.GONE);
            youTubePlayer.setVisibility(View.VISIBLE);
        }
    }

    private void setEvent() {
        back.setOnClickListener(this);
        plus.setOnClickListener(this);
        reduce.setOnClickListener(this);
        exeTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateCaloriesBurn();
            }
        });
    }

    private void getExeDescription() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.FITNESS);
        dbRef.child(activityType.toLowerCase()).child(exerciseName.toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder exeDes = new StringBuilder();
                for (DataSnapshot repItem : dataSnapshot.child(Constant.DESCRIPTION).getChildren()) {
                    exeDes.append(repItem.getValue()).append("\n\n");
                }
                FitnessExercise fitnessExercise = new FitnessExercise(
                        exerciseName,
                        String.valueOf(dataSnapshot.child(Constant.TIME).getValue()),
                        activityType,
                        null,
                        String.valueOf(dataSnapshot.child(Constant.VIDEO).getValue()),
                        exeDes.toString(),
                        String.valueOf((Math.round(caloBurn * time * 10.0)) / 10.0)
                );
                setExeDescription(fitnessExercise);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    private void setExeDescription(final FitnessExercise exercise) {
        exeTitle.setText(exercise.getName());
        exeName.setText(exercise.getName());
        exeTime.setText(exercise.getTime());
        exeCalBurn.setText(String.format("%s kcal", exercise.getCaloriesBurned()));
        exeDescription.setText(exercise.getDescription());

        if ("EXERCISE".equals(activityType)) {
            youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(exercise.getVideo(), 0f);
                }
            });
        } else {
            Glide.with(ExerciseDescriptionActivity.this).load(exercise.getVideo()).into(backgroundImg);
        }
    }
    private void plusTime() {
        time += 1;
        exeTime.setText(String.valueOf(time));
    }
    private void reduceTime() {
        time -= 1;
        exeTime.setText(String.valueOf(time));
    }
    private void calculateCaloriesBurn() {
        float min = Float.parseFloat(exeTime.getText().toString());
        time = min;
        exeCalBurn.setText(String.format("%s kcal", String.valueOf((Math.round(caloBurn * time * 10.0)) / 10.0)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.plusTime:
                plusTime();
                break;
            case R.id.reduceTime:
                reduceTime();
                break;
            default:
                break;
        }
    }
}
