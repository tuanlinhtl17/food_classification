package com.example.foodclassificationapp.contract.presenter;

import androidx.annotation.NonNull;

import com.example.foodclassificationapp.contract.LoginContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View loginView;

    @Override
    public void handleLogin(String email, String password) {
        FirebaseAuth fiAuth = FirebaseAuth.getInstance();
        fiAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginView.loginSuccessful();
                        } else {
                            loginView.loginFail("Email or password incorrect");
                        }
                    }
                });
    }

    @Override
    public void resetPassword(String email) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loginView.showToast("Check email to reset your password!");
                        } else {
                            loginView.showToast("Fail to send reset password email!");
                        }
                    }
                });
    }

    @Override
    public void attachView(LoginContract.View view) {
        this.loginView = view;
    }

    @Override
    public void detachView() {
        this.loginView = null;
    }
}
