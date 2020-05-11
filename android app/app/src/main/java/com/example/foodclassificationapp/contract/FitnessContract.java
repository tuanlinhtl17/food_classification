package com.example.foodclassificationapp.contract;

import com.example.foodclassificationapp.entity.FitnessExercise;

import java.util.ArrayList;

public interface FitnessContract {
    interface FitnessListView {
        void showActivitiesList(ArrayList<FitnessExercise> exerciseList);
    }

    interface FitnessDetailView {
        void showFitnessExerciseDetail(FitnessExercise exercise);

        void showMessageDialog();

        void showConfirmDialog();

        void shareImage(String image);

        void shareType(String type);

        void showToastAddSuccess();
    }

    interface FitnessListPresenter {
        void attachView(FitnessListView view);

        void detachView();

        void getFitnessExercise(String type, float weight);
    }

    interface FitnessDetailPresenter {
        void attachView(FitnessDetailView view);

        void detachView();

        void getFitnessExercise(String name, String type, float calorieBurn, float time);

        void addDailyActivity(FitnessExercise exercise);
    }
}
