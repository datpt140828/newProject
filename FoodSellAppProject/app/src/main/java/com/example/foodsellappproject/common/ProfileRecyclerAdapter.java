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
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.entity.Address;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.ui.cart.CheckOutFragment;
import com.example.foodsellappproject.ui.profile.AddressFragment;
import com.example.foodsellappproject.ui.profile.EditProfileFragment;
import com.example.foodsellappproject.ui.profile.MyOrderFragment;
import com.example.foodsellappproject.ui.profile.ProfileFragment;

import java.util.ArrayList;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ViewHolder> {

    private ArrayList<Order> getOrdersByUserId;
    private ArrayList<Address> getAddressesByUserId;

    public ProfileRecyclerAdapter(ArrayList<Order> getOrdersByUserId, ArrayList<Address> getAddressesByUserId) {
        this.getOrdersByUserId = getOrdersByUserId;
        this.getAddressesByUserId = getAddressesByUserId;
    }

    @NonNull
    @Override
    public ProfileRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_custom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRecyclerAdapter.ViewHolder holder, int position) {
        if (position == 0) {
            SharedPreferences prefStatusOrder = holder.itemView.getContext().getSharedPreferences("statusOfOrder", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editorStatusOrder = prefStatusOrder.edit();
            editorStatusOrder.putString("statusOfOrder", "delivered");
            editorStatusOrder.commit();

            holder.tvMyOrder.setText("Đơn hàng của tôi");
            holder.tvQuantity.setText(""+getOrdersByUserId.size());
            holder.ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyOrderFragment myOrderFragment = new MyOrderFragment();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    ProfileFragment profileFragment = new ProfileFragment();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .remove(profileFragment)
                            .replace(R.id.profileFragment, myOrderFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else if (position == 1) {
            holder.tvMyOrder.setText("Thông tin cá nhân");
            holder.tvQuantity.setText("");
            holder.ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditProfileFragment editProfileFragment = new EditProfileFragment();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    ProfileFragment profileFragment = new ProfileFragment();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .remove(profileFragment)
                            .replace(R.id.profileFragment, editProfileFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else if (position == 2) {
            holder.tvMyOrder.setText("Địa chỉ nhận");
            holder.tvQuantity.setText(""+getAddressesByUserId.size());
            holder.ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddressFragment addressFragment = new AddressFragment();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.profileFragment, addressFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMyOrder, tvQuantity;
        private ImageView ivNext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMyOrder = itemView.findViewById(R.id.tvMyOrder);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivNext = itemView.findViewById(R.id.ivNext);
        }
    }
}
