package com.example.foodclassificationapp.view.main.fitness;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.contract.FitnessContract;
import com.example.foodclassificationapp.contract.presenter.FitnessDetailPresenterJr;
import com.example.foodclassificationapp.util.Constant;
import com.example.foodclassificationapp.entity.FitnessExercise;

import com.example.foodclassificationapp.view.main.MainActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.concurrent.TimeUnit;

public class ExerciseDescriptionActivity extends AppCompatActivity implements FitnessContract.FitnessDetailView, View.OnClickListener{

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
    private ImageView addExercise;
    private Button addCalendar;
    private TextView tvTimer;
    private Button start;

    private String exerciseName;
    private String activityType;
    private float calorieBurn;
    private int time;
    private FitnessDetailPresenterJr presenter;
    private CountDown timer;
    private boolean isTimer = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_description);
        init();
        setEvent();
        initPresenter();
        presenter.getFitnessExercise(exerciseName, activityType, calorieBurn, time);
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
        addCalendar = findViewById(R.id.addCalendar);
        tvTimer = findViewById(R.id.timer);
        start = findViewById(R.id.startExe);
        addExercise = findViewById(R.id.addExe);

        Intent intent = getIntent();
        exerciseName = intent.getStringExtra(Constant.NAME);
        activityType = intent.getStringExtra(Constant.TYPE);
        calorieBurn = intent.getFloatExtra(Constant.CALORIE_BURN, 0);
        time = intent.getIntExtra(Constant.TIME, 0);
        if ("EXERCISE".equals(activityType)) {
            backgroundImg.setVisibility(View.GONE);
            youTubePlayer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * init presenter
     */
    private void initPresenter() {
        presenter = new FitnessDetailPresenterJr();
        presenter.attachView(this);
    }

    /**
     * set event listener
     */
    private void setEvent() {
        back.setOnClickListener(this);
        plus.setOnClickListener(this);
        reduce.setOnClickListener(this);
        addExercise.setOnClickListener(this);
        addCalendar.setOnClickListener(this);
        start.setOnClickListener(this);
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

    /**
     * action plus time
     */
    private void plusTime() {
        time += 1;
        exeTime.setText(String.valueOf(time));
        reduce.setEnabled(true);
    }

    /**
     * action reduce time
     */
    private void reduceTime() {
        if (time > 1) {
            time -= 1;
            exeTime.setText(String.valueOf(time));
        } else reduce.setEnabled(false);
    }

    /**
     * calculate calorie burned
     */
    private void calculateCaloriesBurn() {
        String timeRef = exeTime.getText().toString().trim();
        time = timeRef.isEmpty() ? 0 : Integer.parseInt(timeRef);
        exeCalBurn.setText(String.format("%s kCal", String.valueOf((Math.round(calorieBurn * time * 10.0)) / 10.0)));
    }

    /**
     * action add to calendar
     */
    private void addToCalendar() {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, exeName.getText())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(intent);
    }

    /**
     * set event onClick
     * @param v view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                viewEditTime();
                if (isTimer) timer.cancel();
                finish();
                break;
            case R.id.plusTime:
                plusTime();
                break;
            case R.id.reduceTime:
                reduceTime();
                break;
            case R.id.addCalendar:
                addToCalendar();
                break;
            case R.id.startExe:
                showConfirmDialog();
                break;
            case R.id.addExe:
                addExercise();
                break;
            default:
                break;
        }
    }

    /**
     * show exercise timer
     */
    private void viewTimer() {
        reduce.setVisibility(View.GONE);
        plus.setVisibility(View.GONE);
        exeTime.setVisibility(View.GONE);
        tvTimer.setVisibility(View.VISIBLE);
    }

    /**
     * show edit time
     */
    private void viewEditTime() {
        tvTimer.setVisibility(View.GONE);
        reduce.setVisibility(View.VISIBLE);
        plus.setVisibility(View.VISIBLE);
        exeTime.setVisibility(View.VISIBLE);
    }

    /**
     * add exercise daily
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addExercise() {
        String timeRef = exeTime.getText().toString().trim();
        if (timeRef.isEmpty() || Float.parseFloat(timeRef) <= 0) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("Error")
                    .setMessage("Time value invalid!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    });
            Dialog dialog = mBuilder.create();
            dialog.show();
        } else {
            SharedPreferences sharedPreferencesImg = getApplicationContext().getSharedPreferences(Constant.ACTIVITY_IMG, MODE_PRIVATE);
            String img = sharedPreferencesImg.getString(Constant.ACTIVITY_IMG, Constant.IMAGE);
            SharedPreferences sharedPreferencesType = getApplicationContext().getSharedPreferences(Constant.ACTIVITY_TYPE, MODE_PRIVATE);
            String type = sharedPreferencesType.getString(Constant.ACTIVITY_TYPE, Constant.TYPE);
            String strCalorieBurn = exeCalBurn.getText().toString().split(" ")[0];
            FitnessExercise exercise = new FitnessExercise(exeName.getText().toString(),
                    timeRef, type,
                    img, null, null, strCalorieBurn);
            presenter.addDailyActivity(exercise);
        }
    }

    /**
     * show activity info
     * @param exercise activity
     */
    @Override
    public void showFitnessExerciseDetail(final FitnessExercise exercise) {
        exeTitle.setText(exercise.getName());
        exeName.setText(exercise.getName());
        exeTime.setText(exercise.getTime());
        exeCalBurn.setText(String.format("%s kCal", exercise.getCaloriesBurned()));
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
            start.setVisibility(View.GONE);
            addCalendar.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2f));
        }
    }

    /**
     * show message calculate calorie burned
     */
    @Override
    public void showMessageDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Notification")
                .setMessage("Congratulation! You burned " + exeCalBurn.getText())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewEditTime();
                    }
                });
        Dialog dialog = mBuilder.create();
        dialog.show();
        start.setEnabled(true);
    }

    /**
     * show exercise confirm dialog
     */
    @Override
    public void showConfirmDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Confirm")
                .setMessage("Are your ready?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewTimer();
                        timer = new CountDown(Long.parseLong(exeTime.getText().toString()) * 60 * 1000, 1000);
                        timer.start();
                        isTimer = true;
                        start.setEnabled(false);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        Dialog dialog = mBuilder.create();
        dialog.show();
    }

    /**
     * share image path
     * @param image image path
     */
    @Override
    public void shareImage(String image) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ACTIVITY_IMG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ACTIVITY_IMG", image);
        editor.apply();
    }

    /**
     * share activity type
     * @param type activity type
     */
    @Override
    public void shareType(String type) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ACTIVITY_TYPE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ACTIVITY_TYPE", type);
        editor.apply();
    }

    /**
     * show toast add successful
     */
    @Override
    public void showToastAddSuccess() {
        Toast.makeText(ExerciseDescriptionActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ExerciseDescriptionActivity.this, MainActivity.class));
    }

    public class CountDown extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            @SuppressLint("DefaultLocale") String countTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            tvTimer.setText(countTime);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onFinish() {
            tvTimer.setText("00:00");
            showMessageDialog();
        }
    }
}
