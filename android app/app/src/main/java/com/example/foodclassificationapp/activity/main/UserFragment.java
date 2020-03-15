package com.example.foodclassificationapp.activity.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.login.LoginActivity;
import com.example.foodclassificationapp.constant.Constant;
import com.example.foodclassificationapp.entity.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;


public class UserFragment extends Fragment implements View.OnClickListener {

    private View userView;
    private ImageView imgProfile;
    private TextView nameProfile;
    private EditText ageProfile;
    private EditText heightProfile;
    private EditText weightProfile;
    private TextView emailProfile;
    private TextView genderProfile;
    private Button switchUser;

    private FirebaseAuth fiAuth;
    private FirebaseDatabase firebaseDb;
    private FirebaseAuth.AuthStateListener fiAuthStateListener;
//    private StorageReference storageRef;
    private DatabaseReference dbRef;

//    private int CAMRERA_REQUEST_CODE = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userView = inflater.inflate(R.layout.user_fragment, container, false);
        init();
        setEvent();
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
        emailProfile = userView.findViewById(R.id.emailPro);
        genderProfile = userView.findViewById(R.id.genderPro);
        switchUser = userView.findViewById(R.id.switchUser);

        fiAuth = FirebaseAuth.getInstance();
        firebaseDb = FirebaseDatabase.getInstance();
    }

    private void setEvent() {
        imgProfile.setOnClickListener(this);
        switchUser.setOnClickListener(this);
        ageProfile.setOnClickListener(this);
        heightProfile.setOnClickListener(this);
        weightProfile.setOnClickListener(this);


        fiAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (fiAuth.getCurrentUser() != null) {
//                    storageRef = FirebaseStorage.getInstance().getReference();
                    dbRef = FirebaseDatabase.getInstance().getReference().child("user");
                    dbRef.child(fiAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            String imgUrl = String.valueOf(dataSnapshot.child("image").getValue());
                            nameProfile.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                            ageProfile.setText(String.valueOf(dataSnapshot.child("age").getValue()));
                            heightProfile.setText(String.valueOf(dataSnapshot.child("height").getValue()));
                            weightProfile.setText(String.valueOf(dataSnapshot.child("weight").getValue()));
                            emailProfile.setText(String.valueOf(dataSnapshot.child("email").getValue()));
                            genderProfile.setText(String.valueOf(dataSnapshot.child("gender").getValue()));

//                            if (URLUtil.isValidUrl(imgUrl)) {
//                                Glide.with(Objects.requireNonNull(getContext())).load(Uri.parse(imgUrl)).into(imgProfile);
//                            }
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgUser:
/*
                Intent imgIntent = new Intent();
                imgIntent.setType("image/*");
                imgIntent.setAction(Intent.ACTION_GET_CONTENT);
                if (imgIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(imgIntent, "Select your profile image"), CAMRERA_REQUEST_CODE);
                }
*/
                break;
            case R.id.switchUser:
                if (fiAuth.getCurrentUser() != null) {
                    fiAuth.signOut();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.agePro:
                ageProfile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //do nothing
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // do thing
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        updateProfile();
                    }
                });
                break;
            case R.id.heightPro:
                heightProfile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //do nothing
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //do nothing
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        updateProfile();
                    }
                });
                break;
            case R.id.weightPro:
                weightProfile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //do nothing
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //do nothing
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        updateProfile();
                    }
                });
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateProfile() {
        int age = Integer.parseInt(ageProfile.getText().toString().trim());
        float height = Float.parseFloat(heightProfile.getText().toString().trim());
        float weight = Float.parseFloat(weightProfile.getText().toString().trim());
        String fullName = nameProfile.getText().toString().trim();
        String email = emailProfile.getText().toString().trim();
        String image = "image";
        String gender = genderProfile.getText().toString().trim();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.USER_DB);
        DatabaseReference registerUserDB = databaseReference.child(Objects.requireNonNull(fiAuth.getCurrentUser()).getUid());

        UserProfile userProfile = new UserProfile(image, fullName, email, age, height, weight, gender);
        registerUserDB.setValue(userProfile);
    }
}
