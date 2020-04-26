package com.example.foodclassificationapp.activity.login;

public interface LoginContract {
    interface View {
        void loginSuccessful();

        void loginFail(String error);

        void navigateHome();

    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void handleLogin(String email, String password);
    }
}
