package com.example.foodclassificationapp.activity.signup;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.foodclassificationapp.activity.login.LoginContract;
import com.example.foodclassificationapp.util.Constant;
import com.example.foodclassificationapp.entity.MyWeight;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.Objects;

public class SignUpPresenter implements SignUpContract.SignUnPresenter {
    private SignUpContract.View signUpView;

    @Override
    public void attachView(SignUpContract.View view) {
        this.signUpView = view;
    }

    @Override
    public void detachView() {
        this.signUpView = null;
    }

    @Override
    public void handleSignUp(final String fullName, final String email, final String age, final String height,
                             final String weight, final String password, final String gender) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            firebaseAuth.signInWithEmailAndPassword(email, password);

                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constant.USER_DB);
                            DatabaseReference registerRef = dbRef.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

                            registerRef.child(Constant.NAME).setValue(fullName);
                            registerRef.child(Constant.EMAIL).setValue(email);
                            registerRef.child(Constant.AGE).setValue(Integer.parseInt(age));
                            registerRef.child(Constant.HEIGHT).setValue(height);
                            registerRef.child(Constant.GENDER).setValue(gender);
                            LocalDate localDate;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                localDate = LocalDate.now();
                                String time = localDate.getDayOfMonth() + "/" + localDate.getMonthValue();
                                String date = time + "/" + localDate.getYear();
                                MyWeight myWeight = new MyWeight(time, weight, date);

                                registerRef.child(Constant.WEIGHT).push().setValue(myWeight);
                            }

                            signUpView.signUpSuccess();
                        }
                        else {
                            signUpView.signUpFail("Sign up error!");
                        }
                    }
                });
    }
}
