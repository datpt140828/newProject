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

import com.example.foodsellappproject.dao.Cart_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Cart;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.R;
import com.example.foodsellappproject.entity.Food_Category;
import com.example.foodsellappproject.ui.cart.CartFragment;
import com.example.foodsellappproject.ui.home.AddToCartFragment;

import java.util.ArrayList;

public class AddedFoodToCartRecyclerAdapter extends RecyclerView.Adapter<AddedFoodToCartRecyclerAdapter.ViewHolder> {

    private ArrayList<Food> getFoods;
    private ArrayList<Food_Category> getFoodCategories;
    private ArrayList<Cart> getCarts;

    public AddedFoodToCartRecyclerAdapter(ArrayList<Food> getFoods, ArrayList<Food_Category> getFoodCategories
            , ArrayList<Cart> getCarts) {
        this.getFoods = getFoods;
        this.getFoodCategories = getFoodCategories;
        this.getCarts = getCarts;
    }

    @NonNull
    @Override
    public AddedFoodToCartRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.added_food_to_cart_custom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedFoodToCartRecyclerAdapter.ViewHolder holder, final int position) {

        for (int i = 0; i < getFoodCategories.size(); i++) {
            if (getFoodCategories.get(i).getId() == getFoods.get(position).getId_type()) {
                holder.tvCategoryName.setText(getFoodCategories.get(i).getName());
                break;
            }
        }
        for (int i = 0; i <getFoods.size() ; i++) {
            if(getCarts.get(position).getFood_id()==getFoods.get(i).getId()){
                holder.ivFood.setImageResource(getFoods.get(i).getImageId());
                holder.tvFoodName.setText(getFoods.get(i).getName());
                break;
            }
        }

        holder.tvQuantity.setText(String.valueOf(getCarts.get(position).getQuantity()));
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
                transaction.replace(R.id.cartFragment, addToCartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        holder.tvFoodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = v.getContext().getSharedPreferences("chooseFood", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nameOfFood", getFoods.get(position).getName());
                editor.commit();

                AddToCartFragment addToCartFragment = new AddToCartFragment();
                FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.cartFragment, addToCartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        //handle when click on icon + or -
        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getCarts.size(); i++) { // getCarts # carts
                    if (i == position) {
                        DBConnection db = DBConnection.getDBConnection(v.getContext());
                        Cart_DAO cart_dao = db.createCartDao();
                        ArrayList<Cart> carts = (ArrayList<Cart>) cart_dao.listAll();
                        for (int j = 0; j < carts.size(); j++) {
                            if (getCarts.get(i).getId() == carts.get(j).getId()) {
                                cart_dao.updateCart(getCarts.get(i).getQuantity() + 1, getCarts.get(i).getUser_id(), getCarts.get(i).getFood_id());
                                break;
                            }
                        }
                        break;
                    }
                }


                CartFragment cartFragment = new CartFragment();
                FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.cartFragment, cartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        holder.tvSubstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getCarts.size(); i++) { // getCarts # carts
                    if (i == position) {
                        DBConnection db = DBConnection.getDBConnection(v.getContext());
                        Cart_DAO cart_dao = db.createCartDao();
                        ArrayList<Cart> carts = (ArrayList<Cart>) cart_dao.listAll();
                        for (int j = 0; j < carts.size(); j++) {
                            if (getCarts.get(i).getId() == carts.get(j).getId()) {
                                if (getCarts.get(i).getQuantity() > 1) {
                                    cart_dao.updateCart(getCarts.get(i).getQuantity() - 1, getCarts.get(i).getUser_id(), getCarts.get(i).getFood_id());
                                }
                                break;
                            }
                        }
                        break;
                    }
                }

                CartFragment cartFragment = new CartFragment();
                FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.cartFragment, cartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        //handle when click on cancel
        holder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < getCarts.size(); i++) { // getCarts # carts
                    if (i == position) {
                        DBConnection db = DBConnection.getDBConnection(v.getContext());
                        Cart_DAO cart_dao = db.createCartDao();
                        ArrayList<Cart> carts = (ArrayList<Cart>) cart_dao.listAll();
                        for (int j = 0; j < carts.size(); j++) {
                            if (getCarts.get(i).getId() == carts.get(j).getId()) {
                                cart_dao.deleteCart(getCarts.get(i).getUser_id(), getCarts.get(i).getFood_id());
                                break;
                            }
                        }
                        break;
                    }
                }

                CartFragment cartFragment = new CartFragment();
                FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.cartFragment, cartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFoodName, tvCancel, tvCategoryName, tvSubstract, tvQuantity, tvAdd, tvPrice;
        private ImageView ivFood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvCancel = itemView.findViewById(R.id.tvCancel);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvSubstract = itemView.findViewById(R.id.tvSubstract);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvAdd = itemView.findViewById(R.id.tvAdd);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivFood = itemView.findViewById(R.id.ivFood);
        }
    }
}
