package com.example.foodclassificationapp.control;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.fitness.ExerciseDescriptionActivity;
import com.example.foodclassificationapp.util.Constant;
import com.example.foodclassificationapp.entity.FitnessExercise;

import java.util.List;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ListExerciseViewHolder> {
    private Context context;
    private List<FitnessExercise> exerciseList;

    public ExerciseListAdapter(List<FitnessExercise> exerciseList, Context context) {
        this.context = context;
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseListAdapter.ListExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fitness_exercise, parent, false);
        return new ExerciseListAdapter.ListExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListExerciseViewHolder holder, final int position) {
        holder.exerciseName.setText(exerciseList.get(position).getName());
        holder.time.setText(String.format("%s min", exerciseList.get(position).getTime()));
        holder.caloriesBuned.setText(String.format("%s kcal", exerciseList.get(position).getCaloriesBurned()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = Integer.parseInt(exerciseList.get(position).getTime());
                float calorieBurned = Float.parseFloat(exerciseList.get(position).getCaloriesBurned());
                Intent intent = new Intent(context, ExerciseDescriptionActivity.class);
                intent.putExtra(Constant.TYPE, exerciseList.get(position).getType());
                intent.putExtra(Constant.NAME, exerciseList.get(position).getName());
                intent.putExtra(Constant.CALORIE_BURN, calorieBurned/time);
                intent.putExtra(Constant.TIME, time);
                context.startActivity(intent);
            }
        });
        if ("EXERCISE".equals(exerciseList.get(position).getType())) {
            Glide.with(context).asGif().load(exerciseList.get(position).getImage()).into(holder.exerciseImage);
        } else {
            Glide.with(context).load(exerciseList.get(position).getImage()).into(holder.exerciseImage);
        }
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    static class ListExerciseViewHolder extends RecyclerView.ViewHolder {
        private ImageView exerciseImage;
        private TextView exerciseName;
        private TextView time;
        private TextView caloriesBuned;

        LinearLayout parentLayout;

        ListExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            initItemView(itemView);
        }

        private void initItemView(View view) {
            exerciseName = view.findViewById(R.id.exerciseName);
            exerciseImage = view.findViewById(R.id.imgExe);
            time = view.findViewById(R.id.exeTime);
            caloriesBuned = view.findViewById(R.id.caloBurn);
            parentLayout = view.findViewById(R.id.exerciseItem);
        }

    }
}
