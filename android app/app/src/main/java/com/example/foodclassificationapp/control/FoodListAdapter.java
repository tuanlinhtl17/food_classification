package com.example.foodclassificationapp.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.foodclassificationapp.view.FruitInfoActivity;
import com.example.foodclassificationapp.entity.FoodItem;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ListFoodViewHolder> {
    private Context context;
    private List<FoodItem> foodList;

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
    public void onBindViewHolder(@NonNull ListFoodViewHolder holder, final int position) {
        holder.itemName.setText(foodList.get(position).getName());
        holder.itemCal.setText(String.valueOf(foodList.get(position).getCalories()));
        holder.itemCarb.setText(String.valueOf(foodList.get(position).getCacbohydrat()));
        holder.itemFat.setText(String.valueOf(foodList.get(position).getFat()));
        holder.itemProtein.setText((String.valueOf(foodList.get(position).getProtein())));

        final boolean isMyFood = foodList.get(position).isMyFood();
        if (!isMyFood)
            Glide.with(context).load(foodList.get(position).getImage()).into(holder.imageFood);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FruitInfoActivity.class);
                if (!isMyFood) {
                    intent.putExtra("foodName", foodList.get(position).getName());
                    context.startActivity(intent);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("foodObj", foodList.get(position));
                    intent.putExtras(bundle);
                    intent.putExtra("isMyFood", true);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class ListFoodViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageFood;
        private TextView itemCal;
        private TextView itemCarb;
        private TextView itemFat;
        private TextView itemProtein;
        private TextView itemName;

        LinearLayout parentLayout;

        ListFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            initItemView(itemView);
        }

        /**
         * init view
         * @param view view
         */
        private void initItemView(View view) {
            imageFood = view.findViewById(R.id.imgFood);
            itemCal = view.findViewById(R.id.itemCals);
            itemCarb = view.findViewById(R.id.itemCarbs);
            itemFat = view.findViewById(R.id.itemFats);
            itemProtein = view.findViewById(R.id.itemProtein);
            itemName = view.findViewById(R.id.foodItemName);
            parentLayout = view.findViewById(R.id.foodItem);
        }

    }

}
