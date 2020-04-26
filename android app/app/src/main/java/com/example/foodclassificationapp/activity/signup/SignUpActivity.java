package com.example.foodclassificationapp.activity.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.login.LoginActivity;
import com.example.foodclassificationapp.activity.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View, View.OnClickListener {

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

    private SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
        setEvents();
        initPresenter();

        fiAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // nothing
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(fiAuthStateListener);
    }

    private void initView() {
        fullNameET = findViewById(R.id.fullName);
        emailET = findViewById(R.id.userEmail);
        ageET = findViewById(R.id.age);
        heightET = findViewById(R.id.height);
        weightET = findViewById(R.id.weight);
        genderGroup = findViewById(R.id.gender);
        passwordET = findViewById(R.id.password);
        alreadyUser = findViewById(R.id.alreadyUser);
        signUp = findViewById(R.id.signUpBtn);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initPresenter() {
        signUpPresenter = new SignUpPresenter();
        signUpPresenter.attachView(this);
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
                break;
            default:
                break;
        }
    }

    private void register() {
        final String fullName = fullNameET.getText().toString().trim();
        final String email = emailET.getText().toString().trim();
        final String age = ageET.getText().toString().trim();
        final String height = heightET.getText().toString().trim();
        final String weight = weightET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();

        int selectedId = genderGroup.getCheckedRadioButtonId();
        RadioButton genderBtn = findViewById(selectedId);
        final String gender = genderBtn.getText().toString();

        if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(age)
                && !TextUtils.isEmpty(height) && !TextUtils.isEmpty(weight) && !TextUtils.isEmpty(password)) {
            ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.AppTheme_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Resgistering...");
            progressDialog.show();

            signUpPresenter.handleSignUp(fullName, email, age, height, weight, password, gender);
        } else {
            signUpFail("Please fill all input!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signUpPresenter.detachView();
    }

    @Override
    public void signUpSuccess() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }

    @Override
    public void signUpFail(String error) {
        Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
    }
}
