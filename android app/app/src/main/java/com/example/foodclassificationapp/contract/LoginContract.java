package com.example.foodclassificationapp.contract;

public interface LoginContract {
    interface View {

        /**
         * login successful
         */
        void loginSuccessful();

        /**
         * login fail
         * @param error error code
         */
        void loginFail(String error);

        /**
         * navigate home screen
         */
        void navigateHome();
    }

    interface Presenter {

        /**
         * attachView
         * @param view view
         */
        void attachView(View view);

        /**
         * detachView
         */
        void detachView();

        /**
         * handle login action
         * @param email email
         * @param password password
         */
        void handleLogin(String email, String password);
    }
}
