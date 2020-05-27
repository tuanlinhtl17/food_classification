package com.example.foodclassificationapp.view.authentication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.contract.LoginContract;
import com.example.foodclassificationapp.contract.presenter.LoginPresenter;
import com.example.foodclassificationapp.view.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    private EditText emailET;
    private EditText passwordET;
    private TextView signUp;
    private Button loginBtn;
    private FirebaseAuth fiAuth;
    private FirebaseAuth.AuthStateListener fiAuthStateListener;
    private TextView forgotPassword;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setEvents();
        initPresenter();

        fiAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    navigateHome();
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * init view
     */
    private void initView() {
        emailET = findViewById(R.id.login_email);
        passwordET = findViewById(R.id.login_password);
        signUp = findViewById(R.id.signUp);
        loginBtn = findViewById(R.id.loginBtn);
        fiAuth = FirebaseAuth.getInstance();
        forgotPassword = findViewById(R.id.forgot_password);
    }

    /**
     * init presenter
     */
    private void initPresenter() {
        loginPresenter = new LoginPresenter();
        loginPresenter.attachView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fiAuth.addAuthStateListener(fiAuthStateListener);
    }

    /**
     * set event listener
     */
    private void setEvents() {
        loginBtn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }

    /**
     * set event onClick
     * @param v view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                login();
                break;
            case R.id.signUp:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.forgot_password:
                dialogForgotPassword();
                break;
            default:
                break;
        }
    }

    /**
     * show dialog forgot password
     */
    private void dialogForgotPassword() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.forgot_email, null);
        final EditText emailReset = view.findViewById(R.id.resetEmail);
        mBuilder.setView(view)
                .setTitle("Forgot your password?")
                .setMessage("Enter your email to reset password!")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                })
                .setPositiveButton("Add Food", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = emailReset.getText().toString().trim();
                        if (!email.isEmpty()) {
                            loginPresenter.resetPassword(email);
                        } else {
                            showToast("Please enter your email!");
                        }
                    }
                });
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }

    /**
     * login action
     */
    private void login() {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
            loginPresenter.handleLogin(email, password);
        } else {
            loginFail("Please fill email and password");
        }
    }

    @Override
    public void loginSuccessful() {
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginFail(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateHome() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
