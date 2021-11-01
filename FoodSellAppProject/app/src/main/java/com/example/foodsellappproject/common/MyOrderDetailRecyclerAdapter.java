package com.example.foodsellappproject.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.Food_Category;
import com.example.foodsellappproject.entity.OrderDetail;
import com.example.foodsellappproject.ui.cart.OrderDetailFragment;
import com.example.foodsellappproject.ui.home.AddToCartFragment;

import java.util.ArrayList;

public class MyOrderDetailRecyclerAdapter extends RecyclerView.Adapter<MyOrderDetailRecyclerAdapter.ViewHolder> {

    private ArrayList<Food> getFoods;
    private ArrayList<Food_Category> getFoodCategories;
    private ArrayList<OrderDetail> getOrderDetails;
    private Bundle b;

    public MyOrderDetailRecyclerAdapter(ArrayList<Food> getFoods, ArrayList<Food_Category> getFoodCategories, ArrayList<OrderDetail> getOrderDetails, Bundle b) {
        this.getFoods = getFoods;
        this.getFoodCategories = getFoodCategories;
        this.getOrderDetails = getOrderDetails;
        this.b = b;
    }

    @NonNull
    @Override
    public MyOrderDetailRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_detail_custom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyOrderDetailRecyclerAdapter.ViewHolder holder, final int position) {
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

                AddToCartFragment addToCartFragment = new AddToCartFragment();
                FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.orderDetailFragment, addToCartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //handle cbComment
        SharedPreferences prefStatusOrder = holder.itemView.getContext().getSharedPreferences("statusOfOrder", Context.MODE_PRIVATE);
        String statusOfOrder = prefStatusOrder.getString("statusOfOrder", "delivered");

        if(statusOfOrder.equalsIgnoreCase("delivered")){
            if (b != null) {
                boolean checked = b.getBoolean("checked", false);
                int position2 = b.getInt("position", 0);
                if (position2 == position) {
                    holder.cbComment.setChecked(checked);
                    //    Toast.makeText(holder.itemView.getContext(), "Bundle "+position2, Toast.LENGTH_SHORT).show();
                }
            }
            holder.cbComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();

                    if (holder.cbComment.isChecked()) {
                        holder.cbComment.setChecked(true);
                        bundle.putBoolean("checked", true);
                        bundle.putInt("foodId",getOrderDetails.get(position).getFood_id());
                        bundle.putString("nameOfFood",getFoods.get(position).getName());
                    } else {
                        holder.cbComment.setChecked(false);
                        bundle.putBoolean("checked", false);
                    }
                    //     Toast.makeText(holder.itemView.getContext(), ""+bundle.getBoolean("checked",false), Toast.LENGTH_SHORT).show();
                    bundle.putInt("position", position);

                    OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
                    orderDetailFragment.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.orderDetailFragment, orderDetailFragment)
                            .addToBackStack(null)
                            .commit();

                }
            });
        }else {
            holder.cbComment.setFocusable(false);
            holder.cbComment.setVisibility(View.INVISIBLE);
        }




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
