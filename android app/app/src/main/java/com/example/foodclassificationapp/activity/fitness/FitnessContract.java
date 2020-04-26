package com.example.foodclassificationapp.activity.fitness;

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
    }
}
