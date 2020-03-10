package com.example.foodclassificationapp.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.entity.FoodItem;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ListFoodViewHolder> {
    Context context;
    List<FoodItem> foodList;

    public FoodListAdapter(List<FoodItem> foodList, Context context) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ListFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new ListFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFoodViewHolder holder, int position) {
        holder.itemName.setText(foodList.get(position).getName());
        holder.itemCal.setText(String.valueOf(foodList.get(position).getCalories()));
        holder.itemCarb.setText(String.valueOf(foodList.get(position).getCarbs()));
        holder.itemFat.setText(String.valueOf(foodList.get(position).getFats()));
        holder.itemProtenin.setText((String.valueOf(foodList.get(position).getProteins())));

        Glide.with(context).load(foodList.get(position).getImage()).into(holder.imageFood);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

     static class ListFoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFood;
        TextView itemCal;
        TextView itemCarb;
        TextView itemFat;
        TextView itemProtenin;
        TextView itemName;

        ListFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            initItemView(itemView);
        }

        private void initItemView(View view) {
            imageFood = view.findViewById(R.id.imgFood);
            itemCal = view.findViewById(R.id.itemCals);
            itemCarb = view.findViewById(R.id.itemCarbs);
            itemFat = view.findViewById(R.id.itemFats);
            itemProtenin = view.findViewById(R.id.itemProtein);
            itemName = view.findViewById(R.id.foodItemName);
        }

    }
}
