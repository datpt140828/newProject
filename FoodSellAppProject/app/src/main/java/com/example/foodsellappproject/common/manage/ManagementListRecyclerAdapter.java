package com.example.foodsellappproject.common.manage;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.controller.manage.ManageOrderActivity;
import com.example.foodsellappproject.controller.manage.ManageProductActivity;
import com.example.foodsellappproject.controller.manage.ManageUserActivity;

public class ManagementListRecyclerAdapter extends RecyclerView.Adapter<ManagementListRecyclerAdapter.ViewHolder> {


    @NonNull
    @Override
    public ManagementListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_custom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ManagementListRecyclerAdapter.ViewHolder holder, int position) {
        holder.tvQuantity.setText("");
        if (position == 0) {
            holder.tvManage.setText("Quản lý đơn hàng");
            holder.ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), ManageOrderActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        } else if (position == 1) {
            holder.tvManage.setText("Quản lý sản phẩm");
            holder.ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), ManageProductActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        } else if (position == 2) {
            holder.tvManage.setText("Quản lý người dùng");
            holder.ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), ManageUserActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvManage, tvQuantity;
        private ImageView ivNext;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvManage = itemView.findViewById(R.id.tvMyOrder);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivNext = itemView.findViewById(R.id.ivNext);
        }
    }
}
