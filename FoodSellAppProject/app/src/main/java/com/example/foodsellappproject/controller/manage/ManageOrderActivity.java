package com.example.foodsellappproject.controller.manage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.manage.ManageAllOrdersRecyclerAdapter;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;

import java.util.ArrayList;

public class ManageOrderActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvBtnDelivered, tvBtnDelivering;
    private RecyclerView rvOrdersByUserId;

    private void loadView() {
        ivBack = findViewById(R.id.ivBack);
        tvBtnDelivered = findViewById(R.id.tvBtnDelivered);
        tvBtnDelivering = findViewById(R.id.tvBtnDelivering);
        rvOrdersByUserId = findViewById(R.id.rvOrdersByUserId);

    }

    private DBConnection db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);
        getSupportActionBar().hide();

        //load view from layout
        loadView();

        //handle ivBack
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageOrderActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });




        //handle both btn delivered and delivering
        SharedPreferences prefStatusOrder = getSharedPreferences("statusOfOrder1", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editorStatusOrder = prefStatusOrder.edit();

        String statusOfOrder1 = prefStatusOrder.getString("statusOfOrder1","delivered");


        if (statusOfOrder1.equalsIgnoreCase("delivered")) {
            tvBtnDelivered.setTextColor(Color.WHITE);
            tvBtnDelivered.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dagiaohang));
            tvBtnDelivering.setTextColor(Color.RED);
            tvBtnDelivering.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.chuagiaohang));
        } else {
            tvBtnDelivering.setTextColor(Color.WHITE);
            tvBtnDelivering.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dagiaohang));
            tvBtnDelivered.setTextColor(Color.RED);
            tvBtnDelivered.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.chuagiaohang));
        }

        tvBtnDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editorStatusOrder.putString("statusOfOrder1", "delivered");
                editorStatusOrder.commit();

                Intent intent = new Intent(ManageOrderActivity.this, ManageOrderActivity.class);
                startActivity(intent);
            }
        });
        tvBtnDelivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editorStatusOrder.putString("statusOfOrder1", "delivering");
                editorStatusOrder.commit();



                Intent intent = new Intent(ManageOrderActivity.this, ManageOrderActivity.class);
                startActivity(intent);
            }
        });
        String statusOfOrder = prefStatusOrder.getString("statusOfOrder1", "delivered");
        Toast.makeText(this, "status = " + statusOfOrder, Toast.LENGTH_SHORT).show();

        db = DBConnection.getDBConnection(this);

        ArrayList<Order> getOrders = (ArrayList<Order>) db.createOrderDao().listAllByStatusOfOrder(statusOfOrder);
        ArrayList<OrderDetail> getOrderDetails = (ArrayList<OrderDetail>) db.createOrderDetailDao().listAll();

        ManageAllOrdersRecyclerAdapter adapter = new ManageAllOrdersRecyclerAdapter(getOrders, getOrderDetails);
        rvOrdersByUserId.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvOrdersByUserId.setLayoutManager(manager);


    }
}