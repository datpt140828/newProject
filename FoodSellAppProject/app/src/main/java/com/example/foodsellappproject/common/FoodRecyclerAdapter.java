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
import com.example.foodsellappproject.controller.HomeActivity;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.ui.home.AddToCartFragment;

import java.util.ArrayList;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.ViewHolder> {
    private ArrayList<Food> foods;
    private HomeActivity activity;

    public FoodRecyclerAdapter(ArrayList<Food> foods, HomeActivity activity) {
        this.foods = foods;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FoodRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRecyclerAdapter.ViewHolder holder, final int position) {
        holder.iv1.setImageResource(foods.get(position).getImageId());
        holder.tvFood1.setText((position + 1) + "." + foods.get(position).getName());
        holder.tvPrice1.setText(foods.get(position).getPrice() + " đ");


        holder.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = v.getContext().getSharedPreferences("chooseFood", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nameOfFood", foods.get(position).getName());
                editor.commit();

                AddToCartFragment addToCartFragment = new AddToCartFragment();
                //FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentManager manager = activity.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, addToCartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
        //        Intent intent = new Intent()
                // getFragmentManager();
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                AddToCartFragment addToCartFragment = new AddToCartFragment();
//                activity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nav_host_fragment, addToCartFragment).addToBackStack(null).commit();
            }
        });
        holder.tvFood1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = v.getContext().getSharedPreferences("chooseFood", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nameOfFood", foods.get(position).getName());
                editor.commit();

                // getFragmentManager();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                AddToCartFragment addToCartFragment = new AddToCartFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framecontainer, addToCartFragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv1;
        private TextView tvFood1, tvPrice1;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            iv1 = itemView.findViewById(R.id.ivFood1);
            tvFood1 = itemView.findViewById(R.id.tvFood1);
            tvPrice1 = itemView.findViewById(R.id.tvPrice1);

        }
    }
}
