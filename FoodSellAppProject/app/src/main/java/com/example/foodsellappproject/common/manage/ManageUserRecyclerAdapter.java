package com.example.foodsellappproject.common.manage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.controller.manage.ManageDetailedOrderActivity;
import com.example.foodsellappproject.controller.manage.ManageOrderActivity;
import com.example.foodsellappproject.controller.manage.ManageUserActivity;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;
import com.example.foodsellappproject.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ManageUserRecyclerAdapter extends RecyclerView.Adapter<ManageUserRecyclerAdapter.ViewHolder> {

    private ArrayList<User> getUsers;

    public ManageUserRecyclerAdapter(ArrayList<User> getUsers) {
        this.getUsers = getUsers;
    }

    @NonNull
    @Override
    public ManageUserRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_user_custom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ManageUserRecyclerAdapter.ViewHolder holder, final int position) {
        holder.tvName.setText(getUsers.get(position).getFullName());
        holder.ivAvt.setImageResource(getUsers.get(position).getAvtImageId());
        if (getUsers.get(position).getStatusOfUser().equalsIgnoreCase("blocked")) {
            holder.btnBlock.setText("Mở");
        } else {
            holder.btnBlock.setText("Khóa");
        }
        holder.btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(holder, getUsers.get(position).getUsername(), position);
            }
        });

    }

    private void showAlertDialog(final ViewHolder holder, final String username, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());

        builder.setIcon(android.R.drawable.star_big_on);
        if (getUsers.get(position).getStatusOfUser().equalsIgnoreCase("blocked")) {
            builder.setMessage("Bạn có muốn mở tài khoản này không ?");
        } else {
            builder.setMessage("Bạn có muốn khóa tài khoản này không ?");
        }

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (holder.btnBlock.getText().toString().equalsIgnoreCase("Khóa")) {
                    DBConnection db = DBConnection.getDBConnection(holder.itemView.getContext());
                    User getUsers = db.createUserDAO().getUserById(username);
                    getUsers.setStatusOfUser("blocked");
                    db.createUserDAO().update(getUsers);

                    Toast.makeText(holder.itemView.getContext(), "Đã khóa tài khoản này", Toast.LENGTH_SHORT).show();
                } else {
                    DBConnection db = DBConnection.getDBConnection(holder.itemView.getContext());
                    User getUsers = db.createUserDAO().getUserById(username);
                    getUsers.setStatusOfUser("active");
                    db.createUserDAO().update(getUsers);

                    Toast.makeText(holder.itemView.getContext(), "Đã mở tài khoản này", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(holder.itemView.getContext(), ManageUserActivity.class);
                holder.itemView.getContext().startActivity(intent);
            }
        });


        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //textView.setText("Button cancel is clicked");
            }
        });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return getUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private Button btnBlock;
        private ImageView ivAvt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            btnBlock = itemView.findViewById(R.id.btnBlock);
            ivAvt = itemView.findViewById(R.id.ivAvt);
        }

    }
}
