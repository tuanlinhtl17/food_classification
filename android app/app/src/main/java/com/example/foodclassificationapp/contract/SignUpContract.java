package com.example.foodclassificationapp.contract;

public interface SignUpContract {
    interface View {
        /**
         * sign up success
         */
        void signUpSuccess();

        /**
         * sign up fail
         * @param error error message
         */
        void signUpFail(String error);
    }

    interface SignUnPresenter {
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
         * handle sign up
         * @param fullName fullName
         * @param email email
         * @param age age
         * @param height height
         * @param weight weight
         * @param password password
         * @param gender gender
         */
        void handleSignUp(String fullName, String email, String age, String height,
                          String weight, String password, String gender);
    }
}
