package com.example.foodclassificationapp.activity.login;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.main.MainActivity;
import com.example.foodclassificationapp.constant.Constant;
import com.example.foodclassificationapp.entity.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText fullNameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText ageET;
    private EditText heightET;
    private EditText weightET;
    private RadioGroup genderGroup;
    private TextView alreadyUser;
    private Button signUp;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener fiAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();
        setEvents();
        fiAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(fiAuthStateListener);
    }

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        fullNameET = findViewById(R.id.fullName);
        emailET = findViewById(R.id.userEmail);
        ageET = findViewById(R.id.age);
        heightET = findViewById(R.id.height);
        weightET = findViewById(R.id.weight);
        genderGroup = findViewById(R.id.gender);
        passwordET = findViewById(R.id.password);
        alreadyUser = findViewById(R.id.alreadyUser);
        signUp = findViewById(R.id.signUpBtn);
    }

    private void setEvents() {
        alreadyUser.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alreadyUser:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                break;
            case R.id.signUpBtn:
                register();
                if (register()) {
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                }
                break;
            default:
                // do nothing
        }
    }

    private boolean register() {
        Log.i("Sign up", fullNameET.getText().toString());
        final String fullName = fullNameET.getText().toString().trim();
        final String email = emailET.getText().toString().trim();
        final String age = ageET.getText().toString().trim();
        final String height = heightET.getText().toString().trim();
        final String weight = weightET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
        final boolean[] res = new boolean[1];

        int selectedId = genderGroup.getCheckedRadioButtonId();
        RadioButton genderBtn = findViewById(selectedId);
        final String gender = genderBtn.getText().toString();

        if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(age)
                && !TextUtils.isEmpty(height) && !TextUtils.isEmpty(weight) && !TextUtils.isEmpty(password)) {
            final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.AppTheme_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Resgistering...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()) {
                                firebaseAuth.signInWithEmailAndPassword(email, password);

                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.USER_DB);
                                DatabaseReference registerUserDB = dbRef.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
                                registerUserDB.child("name").setValue(fullName);
                                registerUserDB.child("age").setValue(Integer.parseInt(age));
                                registerUserDB.child("image").setValue("img");
                                registerUserDB.child("height").setValue(Float.parseFloat(height));
                                registerUserDB.child("weight").setValue(Float.parseFloat(weight));
                                registerUserDB.child("gender").setValue(gender);
                                registerUserDB.child("email").setValue(email);

//                                UserProfile userProfile = new UserProfile("", fullName, email, Integer.parseInt(age),
//                                        Float.parseFloat(height), Float.parseFloat(weight), gender);
//                                registerUserDB.setValue(userProfile);
                                res[0] = true;
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, "Register Error!", Toast.LENGTH_SHORT).show();
                                res[0] = false;
                            }
                        }
                    });
            res[0] = true;
        } else {
            Toast.makeText(SignUpActivity.this, "Please fill all input!", Toast.LENGTH_SHORT).show();
        }
        return res[0];
    }
}
