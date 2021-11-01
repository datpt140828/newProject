package com.example.foodsellappproject.common;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.entity.Food_Category;

import java.util.ArrayList;


public class FoodCategoryRecyclerAdapter extends RecyclerView.Adapter<FoodCategoryRecyclerAdapter.ViewHolder> {


    private static ArrayList<Food_Category> food_categories;

    public FoodCategoryRecyclerAdapter(ArrayList<Food_Category> food_categories) {
        this.food_categories = food_categories;
    }

    @NonNull
    @Override
    public FoodCategoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_category_item_custom, parent, false);
        return new FoodCategoryRecyclerAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FoodCategoryRecyclerAdapter.ViewHolder holder, int position) {
        holder.btnFoodCategory.setText(food_categories.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return food_categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Button btnFoodCategory;
        int mark = 0;
        public static ArrayList<Integer> getFoodCategoryIdsFromBtnApply = new ArrayList<>();

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            btnFoodCategory = itemView.findViewById(R.id.btnFoodCategory);
            btnFoodCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (mark == 0) {
                        btnFoodCategory.setTextColor(Color.WHITE);
                        btnFoodCategory.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.my_apply_button));
                        mark = 1;

                        for (int i = 0; i < food_categories.size(); i++) {
                            if (food_categories.get(i).getName().equalsIgnoreCase(btnFoodCategory.getText().toString())) {
                                getFoodCategoryIdsFromBtnApply.add(food_categories.get(i).getId());
                            }
                        }
                    } else if (mark == 1) {
                        btnFoodCategory.setTextColor(Color.BLACK);
                        btnFoodCategory.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.my_food_category_button));
                        mark = 0;
                        for (int i = 0; i < food_categories.size(); i++) {
                            if (food_categories.get(i).getName().equalsIgnoreCase(btnFoodCategory.getText().toString())) {
                                for (int j = 0; j < getFoodCategoryIdsFromBtnApply.size(); j++) {
                                    if(getFoodCategoryIdsFromBtnApply.get(j)==food_categories.get(i).getId()){
                                        getFoodCategoryIdsFromBtnApply.remove(j);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }
            });
        }
    }
}
