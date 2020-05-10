package com.example.foodclassificationapp.contract;

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
