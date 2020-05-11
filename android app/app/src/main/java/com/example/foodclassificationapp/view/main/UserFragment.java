package com.example.foodclassificationapp.view.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.util.Constant;
import com.example.foodclassificationapp.entity.MyWeight;
import com.example.foodclassificationapp.view.authentication.LoginActivity;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UserFragment extends Fragment implements View.OnClickListener {

    private View userView;
    private ImageView imgProfile;
    private TextView nameProfile;
    private TextView ageProfile;
    private TextView heightProfile;
    private TextView weightProfile;
    private ImageButton switchUser;
    private androidx.cardview.widget.CardView cardView;

    private FirebaseAuth fiAuth;
    private FirebaseAuth.AuthStateListener fiAuthStateListener;
    private DatabaseReference dbRef;
    private float diffDays = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userView = inflater.inflate(R.layout.user_fragment, container, false);
        init();
        setEvent();
        getUserInfo();
        reqUpdateWeight();
        return userView;
    }

    @Override
    public void onStart() {
        super.onStart();
        fiAuth.addAuthStateListener(fiAuthStateListener);
    }

    private void init() {
        nameProfile = userView.findViewById(R.id.profileFullName);
        imgProfile = userView.findViewById(R.id.imgUser);
        ageProfile = userView.findViewById(R.id.agePro);
        heightProfile = userView.findViewById(R.id.heightPro);
        weightProfile = userView.findViewById(R.id.weightPro);
        switchUser = userView.findViewById(R.id.switchUser);
        cardView = userView.findViewById(R.id.cardInfor);

        fiAuth = FirebaseAuth.getInstance();
    }

    private void setEvent() {
        imgProfile.setOnClickListener(this);
        switchUser.setOnClickListener(this);
        cardView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getUserInfo() {
        fiAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (fiAuth.getCurrentUser() != null) {
                    dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.USER_DB);
                    dbRef.child(fiAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            nameProfile.setText(String.valueOf(dataSnapshot.child(Constant.NAME).getValue()));
                            ageProfile.setText(String.valueOf(dataSnapshot.child(Constant.AGE).getValue()));
                            heightProfile.setText(String.valueOf(dataSnapshot.child(Constant.HEIGHT).getValue()));
                            String gender = String.valueOf(dataSnapshot.child(Constant.GENDER).getValue());
                            if ("Female".equals(gender))
                                imgProfile.setImageResource(R.drawable.female);
                            else imgProfile.setImageResource(R.drawable.male_user);
                            List<String> date = new ArrayList<>();
                            ArrayList<Entry> entries = new ArrayList<>();
                            int index = 0;
                            String weight = "";
                            String key = "";
                            String dateValue = "";
                            for (DataSnapshot weightData : dataSnapshot.child(Constant.WEIGHT).getChildren()) {
                                date.add(String.valueOf(weightData.child(Constant.TIME).getValue()));
                                weight = String.valueOf(weightData.child(Constant.VALUE).getValue());
                                entries.add(new Entry(index, Float.parseFloat(weight)));
                                index += 1;

                                key = weightData.getKey();
                                dateValue = String.valueOf(weightData.child("date").getValue());
                            }
                            weightProfile.setText(weight);
                            initChart(date, entries);

                            SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(Constant.LAST_DATE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constant.LAST_DATE, date.get(0));
                            editor.putString("key", key);
                            editor.putString("dateValue", dateValue);
                            editor.putString("lastWeight", weight);
                            editor.apply();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // do nothing
                        }
                    });
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void reqUpdateWeight() {
        if (!checkUpdateStatus()) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext())
                    .setTitle("Update your weight")
                    .setMessage("Do you update your weight?")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showDialog();
                        }
                    });
            AlertDialog dialog = mBuilder.create();
            dialog.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkUpdateStatus() {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(Constant.LAST_DATE, Context.MODE_PRIVATE);
        String lastDate = sharedPreferences.getString("dateValue", "");
        return (calculateNumberDay(lastDate) < 7);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switchUser:
                if (fiAuth.getCurrentUser() != null) {
                    fiAuth.signOut();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.cardInfor:
                showDialog();
                break;
            default: break;
        }
    }

    private void initChart (final List<String> date, ArrayList<Entry> entries) {
        Collections.reverse(entries);
        Collections.reverse(date);
        ArrayList<Entry> subEntry = new ArrayList<>();
        final ArrayList<String> subDate = new ArrayList<>();
        int count = 0;
        int maxEntry = 7;
        int indexEntry = entries.size() - 1;
        for (Entry entry : entries) {
            if (count < maxEntry) {
                subEntry.add(new Entry(indexEntry, entry.getY()));
                indexEntry --;
                count ++;
            } else break;
        }
        count = 0;
        for (String day : date) {
            if (count < maxEntry) {
                subDate.add(day);
                count ++;
            } else break;
        }
        Collections.reverse(subEntry);
        Collections.reverse(subDate);
        int tempX = 0;
        for (Entry en : subEntry) {
            en.setX(tempX);
            tempX++;
        }
        CombinedChart chart = userView.findViewById(R.id.lineChart);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(30);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(30);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return subDate.get((int) value % subDate.size());
            }
        });

        CombinedData data = new CombinedData();
        LineData lineData = new LineData();
        lineData.addDataSet((ILineDataSet) dataChart(subEntry));

        data.setData(lineData);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        chart.setData(data);
        chart.invalidate();
    }

    private static DataSet dataChart(ArrayList<Entry> entries) {
        LineData lineData = new LineData();
        LineDataSet lineDataSet = new LineDataSet(entries, "Your weight");
        lineDataSet.setColor(Color.RED);
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleColor(Color.RED);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setFillColor(Color.RED);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(Color.RED);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineData.addDataSet(lineDataSet);
        return lineDataSet;
    }

    private void showDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.input_info_dialog, null);
        final EditText mAge = view.findViewById(R.id.dialogAge);
        final EditText mHeight = view.findViewById(R.id.dialogHeight);
        final EditText mWeight = view.findViewById(R.id.dialogWeight);
        mAge.setText(ageProfile.getText().toString());
        mHeight.setText(heightProfile.getText().toString());
        mWeight.setText(weightProfile.getText().toString());

        mBuilder.setView(view)
                .setTitle("Update your information")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String age = mAge.getText().toString();
                        String height = mHeight.getText().toString();
                        final String weight = mWeight.getText().toString();

                        if (!age.isEmpty() && !height.isEmpty() && !weight.isEmpty()) {
                            updateInfo(age, height, weight);
                        } else {
                            Toast.makeText(getContext(), "Please fill all input!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateInfo(String age, String height, String weight) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.USER_DB);
        final DatabaseReference ref = databaseReference.child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid());
        ref.child(Constant.AGE).setValue(age);
        ref.child(Constant.HEIGHT).setValue(height);

        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1);
        String date = time + "/" + calendar.get(Calendar.YEAR);
        MyWeight myWeight = new MyWeight(time, weight, date);

        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(Constant.LAST_DATE, Context.MODE_PRIVATE);
        String lastDate = sharedPreferences.getString(Constant.LAST_DATE, "");
        String key = sharedPreferences.getString("key", "");
        String lastWeight = sharedPreferences.getString("lastWeight", "");

        if (time.equals(lastDate)) {
            ref.child(Constant.WEIGHT).child(Objects.requireNonNull(key)).child(Constant.VALUE).setValue(weight)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            ref.child(Constant.WEIGHT).push().setValue(myWeight).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (!lastWeight.isEmpty()) {
            reviewWeight(lastWeight, weight);
        }
    }

    private void reviewWeight(String preWeight, String afterWeight) {
        float pWeight = Float.parseFloat(preWeight);
        float aWeight = Float.parseFloat(afterWeight);
        float diffWeight = pWeight - aWeight;
        if (diffDays < 7 && diffDays > 0) {
            if (diffWeight < 0) // tang can
                showDialogReviewWeight("Oh no! Please eat according to the recommended proportions and do more exercise");
            else showDialogReviewWeight("You are losing weight very well. Please continue to promote!");
        } else if (diffDays >= 7){
            if (diffWeight < 0)
                showDialogReviewWeight("Oh no! Please eat according to the recommended proportions and do more exercise");
            else if (diffWeight < 1)
                showDialogReviewWeight("You are not losing weight well. Please eat according to the recommended proportions and do more exercise");
            else if (diffWeight > 1) {
                int rate = (int)diffDays / 7;
                if (diffWeight < rate)
                    showDialogReviewWeight("You are not losing weight well. Please eat according to the recommended proportions and do more exercise");
                else showDialogReviewWeight("You are losing weight very well. Please continue to promote!");
            }
        }
    }

    private void showDialogReviewWeight(String message) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        mBuilder.setTitle("Review")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        Dialog dialog = mBuilder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private float calculateNumberDay(String strLastDate) {
        Date now = new Date();
        float days = -1;
        try {
            @SuppressLint("SimpleDateFormat") Date lastDate = new SimpleDateFormat("dd/MM/yyyy").parse(strLastDate);
            long diff = now.getTime() - Objects.requireNonNull(lastDate).getTime();
            days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            Log.d("calculateNumberDay", Objects.requireNonNull(e.getMessage()));
        }
        diffDays = days;
        return days;
    }
}
