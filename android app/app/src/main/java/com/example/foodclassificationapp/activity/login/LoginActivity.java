package com.example.foodclassificationapp.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailET;
    private EditText passwordET;
    private TextView signUp;
    private Button loginBtn;
    private FirebaseAuth fiAuth;
    private FirebaseAuth.AuthStateListener fiAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        fragmentManager = getSupportFragmentManager();
//        if (savedInstanceState == null) {
//            openFragment(new LoginFragment());
//        }

        initialize();
        setEvents();

        fiAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
    }

    private void initialize() {
        emailET = findViewById(R.id.login_email);
        passwordET = findViewById(R.id.login_password);
        signUp = findViewById(R.id.signUp);
        loginBtn = findViewById(R.id.loginBtn);
        fiAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        fiAuth.addAuthStateListener(fiAuthStateListener);
    }

    private void setEvents() {
        loginBtn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                login();
                break;
            case R.id.signUp:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            default:
                break;
        }
    }

    private void login() {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            fiAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Email or password incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "Please fill email and password", Toast.LENGTH_SHORT).show();
        }
    }

//    public void startLoginFragment() {
//        fragmentManager.beginTransaction()
//                .replace(R.id.loginContainer, new LoginFragment(), Constant.LOGIN_FRAGMENT).commit();
//    }
//
//    public void openFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.loginContainer, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    @Override
//    public void onBackPressed() {
//        Fragment signupFragment = fragmentManager.findFragmentByTag(Constant.SIGNUP_FRAGMENT);
//        if (signupFragment != null)
////            startLoginFragment();
//            openFragment(new LoginFragment());
//        else
//            super.onBackPressed();
//    }

}
