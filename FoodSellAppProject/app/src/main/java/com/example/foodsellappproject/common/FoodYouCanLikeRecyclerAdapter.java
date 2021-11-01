package com.example.foodsellappproject.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.ui.home.AddToCartFragment;

import java.util.ArrayList;

public class FoodYouCanLikeRecyclerAdapter extends RecyclerView.Adapter<FoodYouCanLikeRecyclerAdapter.ViewHolder> {

    private ArrayList<Food> getFoods;

    public FoodYouCanLikeRecyclerAdapter(ArrayList<Food> getFoods) {
        this.getFoods = getFoods;
    }

    @NonNull
    @Override
    public FoodYouCanLikeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_you_can_like_custom, parent, false);
        return new FoodYouCanLikeRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodYouCanLikeRecyclerAdapter.ViewHolder holder, final int position) {
        holder.ivFood.setImageResource(getFoods.get(position).getImageId());
        holder.tvFoodName.setText(getFoods.get(position).getName());
        holder.tvFoodPrice.setText(getFoods.get(position).getPrice() + " đ");

        holder.ivFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = v.getContext().getSharedPreferences("chooseFood", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nameOfFood", getFoods.get(position).getName());
                editor.commit();

                AddToCartFragment addToCartFragment = new AddToCartFragment();
                FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, addToCartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                // getFragmentManager();
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                AddToCartFragment addToCartFragment = new AddToCartFragment();
//                activity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nav_host_fragment, addToCartFragment).addToBackStack(null).commit();
            }
        });
        holder.tvFoodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = v.getContext().getSharedPreferences("chooseFood", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nameOfFood", getFoods.get(position).getName());
                editor.commit();

                // getFragmentManager();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                AddToCartFragment addToCartFragment = new AddToCartFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, addToCartFragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return getFoods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFood;
        private TextView tvFoodName, tvFoodPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.ivFood);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);

        }
    }
}
