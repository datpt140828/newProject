package com.example.foodsellappproject.common.manage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.controller.manage.ViewProductDetailActivity;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.Food_Category;
import com.example.foodsellappproject.entity.OrderDetail;

import java.util.ArrayList;

public class ManageOrderDetailRecyclerAdapter extends RecyclerView.Adapter<ManageOrderDetailRecyclerAdapter.ViewHolder> {

    private ArrayList<Food> getFoods;
    private ArrayList<Food_Category> getFoodCategories;
    private ArrayList<OrderDetail> getOrderDetails;


    public ManageOrderDetailRecyclerAdapter(ArrayList<Food> getFoods, ArrayList<Food_Category> getFoodCategories, ArrayList<OrderDetail> getOrderDetails) {
        this.getFoods = getFoods;
        this.getFoodCategories = getFoodCategories;
        this.getOrderDetails = getOrderDetails;

    }

    @NonNull
    @Override
    public ManageOrderDetailRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_detail_custom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ManageOrderDetailRecyclerAdapter.ViewHolder holder, final int position) {
        for (int i = 0; i < getFoodCategories.size(); i++) {
            if (getFoodCategories.get(i).getId() == getFoods.get(position).getId_type()) {
                holder.tvCategoryName.setText(getFoodCategories.get(i).getName());
                break;
            }
        }
        for (int i = 0; i < getFoods.size(); i++) {
            if (getOrderDetails.get(position).getFood_id() == getFoods.get(i).getId()) {
                holder.ivFood.setImageResource(getFoods.get(i).getImageId());
                holder.tvFoodName.setText(getFoods.get(i).getName());
                break;
            }
        }
        holder.tvQuantity.setText(String.valueOf(getOrderDetails.get(position).getQuantity()));
        holder.tvPrice.setText(getFoods.get(position).getPrice());

        //handle when click on image or text of food
        holder.ivFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = v.getContext().getSharedPreferences("chooseFood", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nameOfFood", getFoods.get(position).getName());
                editor.commit();

                Intent intent = new Intent(holder.itemView.getContext(), ViewProductDetailActivity.class);
                holder.itemView.getContext().startActivity(intent);

            }
        });

        //handle cbComment
        holder.cbComment.setFocusable(false);
        holder.cbComment.setVisibility(View.INVISIBLE);


    }

    @Override
    public int getItemCount() {
        return getOrderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFood;
        private TextView tvFoodName, tvCategoryName, tvQuantity, tvPrice;
        private CheckBox cbComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.ivFood);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            cbComment = itemView.findViewById(R.id.cbComment);
        }
    }
}
