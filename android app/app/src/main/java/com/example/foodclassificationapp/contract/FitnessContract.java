package com.example.foodclassificationapp.contract;

import com.example.foodclassificationapp.entity.FitnessExercise;

import java.util.ArrayList;

public interface FitnessContract {
    interface FitnessListView {

        /**
         * show activity list
         * @param exerciseList activity list
         */
        void showActivitiesList(ArrayList<FitnessExercise> exerciseList);
    }

    interface FitnessDetailView {

        /**
         * show activity detail
         * @param exercise activity
         */
        void showFitnessExerciseDetail(FitnessExercise exercise);

        /**
         * show message
         */
        void showMessageDialog();

        /**
         * show confirm dialog timer
         */
        void showConfirmDialog();

        /**
         * share image path
         * @param image image path
         */
        void shareImage(String image);

        /**
         * share activity type
         * @param type activity type
         */
        void shareType(String type);

        /**
         * show Toast add activity successful
         */
        void showToastAddSuccess();
    }

    interface FitnessListPresenter {

        /**
         * attachView
         * @param view view
         */
        void attachView(FitnessListView view);

        /**
         * detachView
         */
        void detachView();

        /**
         * get activity list
         * @param type activity type
         * @param weight weight to calculate calorie burned
         */
        void getFitnessExercise(String type, float weight);
    }

    interface FitnessDetailPresenter {

        /**
         * attachView
         * @param view view
         */
        void attachView(FitnessDetailView view);

        /**
         * detachView
         */
        void detachView();

        /**
         * get Activity detail
         * @param name activity name
         * @param type activity type
         * @param calorieBurn calories burned
         * @param time time
         */
        void getFitnessExercise(String name, String type, float calorieBurn, float time);

        /**
         * add activity
         * @param exercise activity
         */
        void addDailyActivity(FitnessExercise exercise);
    }
}
