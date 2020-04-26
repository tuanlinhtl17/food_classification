package com.example.foodclassificationapp.activity.signup;

import com.example.foodclassificationapp.activity.login.LoginContract;

public interface SignUpContract {
    interface View {
        void signUpSuccess();

        void signUpFail(String error);
    }

    interface SignUnPresenter {
        void attachView(View view);

        void detachView();

        void handleSignUp(String fullName, String email, String age, String height,
                          String weight, String password, String gender);
    }
}
