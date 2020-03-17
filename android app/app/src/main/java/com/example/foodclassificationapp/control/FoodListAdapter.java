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

import java.util.ArrayList;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ListFoodViewHolder> {
    private Context context;
    private List<FoodItem> foodList = new ArrayList<>();
    private OnFoodListener mOnFoodListener;

    public FoodListAdapter(List<FoodItem> foodList, Context context) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ListFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new ListFoodViewHolder(view, mOnFoodListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFoodViewHolder holder, int position) {
        holder.itemName.setText(foodList.get(position).getName());
        holder.itemCal.setText(String.valueOf(foodList.get(position).getCalories()));
        holder.itemCarb.setText(String.valueOf(foodList.get(position).getCarbs()));
        holder.itemFat.setText(String.valueOf(foodList.get(position).getFats()));
        holder.itemProtein.setText((String.valueOf(foodList.get(position).getProteins())));

        Glide.with(context).load(foodList.get(position).getImage()).into(holder.imageFood);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ListFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageFood;
        TextView itemCal;
        TextView itemCarb;
        TextView itemFat;
        TextView itemProtein;
        TextView itemName;
        OnFoodListener mOnFoodListener;

        ListFoodViewHolder(@NonNull View itemView, OnFoodListener onFoodListener) {
            super(itemView);
            initItemView(itemView);
            mOnFoodListener = onFoodListener;
            itemView.setOnClickListener(this);
        }

        private void initItemView(View view) {
            imageFood = view.findViewById(R.id.imgFood);
            itemCal = view.findViewById(R.id.itemCals);
            itemCarb = view.findViewById(R.id.itemCarbs);
            itemFat = view.findViewById(R.id.itemFats);
            itemProtein = view.findViewById(R.id.itemProtein);
            itemName = view.findViewById(R.id.foodItemName);
        }

        @Override
        public void onClick(View v) {
            mOnFoodListener.onFoodClick(getAdapterPosition());
        }
    }

    public interface OnFoodListener{
        void onFoodClick(int position);
    }
}
