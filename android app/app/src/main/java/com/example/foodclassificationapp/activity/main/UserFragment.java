package com.example.foodclassificationapp.activity.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.example.foodclassificationapp.activity.login.LoginActivity;
import com.example.foodclassificationapp.constant.Constant;
import com.example.foodclassificationapp.entity.MyWeight;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userView = inflater.inflate(R.layout.user_fragment, container, false);
        init();
        setEvent();
        getUserInfo();
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
                            String gender = String.valueOf(dataSnapshot.child("gender").getValue());
                            if ("Female".equals(gender))
                                imgProfile.setImageResource(R.drawable.female);
                            else imgProfile.setImageResource(R.drawable.male_user);
                            List<String> date = new ArrayList<>();
                            ArrayList<Entry> entries = new ArrayList<>();
                            int index = 0;
                            String weight = "";
                            for (DataSnapshot weightData : dataSnapshot.child(Constant.WEIGHT).getChildren()) {
                                date.add(String.valueOf(weightData.child("time").getValue()));
                                weight = String.valueOf(weightData.child(Constant.VALUE).getValue());
                                entries.add(new Entry(index, Float.parseFloat(weight)));
                                index += 1;
                            }
                            weightProfile.setText(weight);
                            initChart(date, entries);
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
                return date.get((int) value % date.size());
            }
        });

        CombinedData data = new CombinedData();
        LineData lineDatas = new LineData();
        lineDatas.addDataSet((ILineDataSet) dataChart(entries));

        data.setData(lineDatas);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        chart.setData(data);
        chart.invalidate();
    }

    private static DataSet dataChart(ArrayList<Entry> entries) {
        LineData d = new LineData();
        LineDataSet set = new LineDataSet(entries, "Your weight");
        set.setColor(Color.RED);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.RED);
        set.setCircleRadius(5f);
        set.setFillColor(Color.RED);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.RED);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);
        return set;
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
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateInfo(String age, String height, final String weight) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.USER_DB);
        final DatabaseReference ref = databaseReference.child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid());
        ref.child(Constant.AGE).setValue(age);
        ref.child(Constant.HEIGHT).setValue(height);

        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LocalDate localDate = LocalDate.now();
                String time = localDate.getDayOfMonth() + "/" + localDate.getMonthValue();
                MyWeight myWeight = new MyWeight(time, weight);
                boolean flag = false;
                for (DataSnapshot weightData : dataSnapshot.child(Constant.WEIGHT).getChildren()) {
                    if (time.equals(String.valueOf(weightData.child("time").getValue()))) {
                        ref.child(Constant.WEIGHT).child(Objects.requireNonNull(weightData.getKey())).child("value").setValue(weight)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    ref.child(Constant.WEIGHT).push().setValue(myWeight).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }
}
