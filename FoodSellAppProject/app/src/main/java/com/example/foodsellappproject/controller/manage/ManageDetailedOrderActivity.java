package com.example.foodsellappproject.controller.manage;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.foodsellappproject.common.manage.ManageOrderDetailRecyclerAdapter;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Address;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.Food_Category;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ManageDetailedOrderActivity extends AppCompatActivity {

    private TextView tvOrderId, tvDeliveredStatus, tvOrderTime, tvName, tvAddress, tvPhone, tvTotalOfBill;
    private RecyclerView rvOrderDetail;
    private ImageView ivBack;

    private void loadView() {
        ivBack = findViewById(R.id.ivBack1);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvDeliveredStatus = findViewById(R.id.tvDeliveredStatus);
        tvOrderTime = findViewById(R.id.tvOrderTime);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvTotalOfBill = findViewById(R.id.tvTotalOfBill);
        rvOrderDetail = findViewById(R.id.rvOrderDetail);
    }

    private DBConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_detailed_order);
        getSupportActionBar().hide();

        //load data from layout
        loadView();

        //handle ivBack
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageDetailedOrderActivity.this, ManageOrderActivity.class);
                startActivity(intent);
            }
        });

        //load database
        db = DBConnection.getDBConnection(this);
        final SharedPreferences pref = getSharedPreferences("handleCbComment", Context.MODE_PRIVATE);
        int orderId = pref.getInt("orderId", 0);
        tvOrderId.setText("Mã đơn hàng:   " + orderId);

        ArrayList<OrderDetail> getOrderDetailsByOrderId = (ArrayList<OrderDetail>) db.createOrderDetailDao().getOrderDetailsByOrderId(orderId);
        ArrayList<Food_Category> getFoodCategories = (ArrayList<Food_Category>) db.createFoodCategoryDAO().getFoodCategories();
        final ArrayList<Food> getFoods = (ArrayList<Food>) db.createFoodDAO().listAll();
        ArrayList<Food> getFoods1 = new ArrayList<>();
        //handle array list
        handleArrayList(getOrderDetailsByOrderId, getFoods, getFoods1);

        //handle rvOrderDetail
        final Intent intent = getIntent();
        ManageOrderDetailRecyclerAdapter adapter = new ManageOrderDetailRecyclerAdapter(getFoods1, getFoodCategories, getOrderDetailsByOrderId);
        rvOrderDetail.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvOrderDetail.setLayoutManager(manager);

        //handle text view
        if (orderId != 0) {
            ArrayList<Order> orderById = (ArrayList<Order>) db.createOrderDao().getOrderById(orderId);
            //load all address both of active and blocked
            ArrayList<Address> address = (ArrayList<Address>) db.createAddressDao().listAddressById(orderById.get(0).getAddressId());
            //Toast.makeText(getContext(), "AddressId = " + orderById.get(0).getAddressId(), Toast.LENGTH_SHORT).show();
            tvName.setText(address.get(0).getName());
            tvAddress.setText(address.get(0).getDetail());
            tvPhone.setText(address.get(0).getPhone());
            tvTotalOfBill.setText(String.valueOf(orderById.get(0).getTotalOfBillMoney()));
        } else {
            Toast.makeText(this, "orderId = " + orderId, Toast.LENGTH_SHORT).show();
        }

        SharedPreferences prefStatusOrder = getSharedPreferences("statusOfOrder1", Context.MODE_PRIVATE);
        String statusOfOrder = prefStatusOrder.getString("statusOfOrder1", "delivered");
        if(statusOfOrder.equalsIgnoreCase("delivered")){
            tvDeliveredStatus.setText("Đã giao");
            tvDeliveredStatus.setTextColor(Color.GREEN);
        }else{
            tvDeliveredStatus.setText("Đang chờ xác nhận");
            tvDeliveredStatus.setTextColor(Color.RED);
        }

    }

    private void handleArrayList(ArrayList<OrderDetail> getOrderDetailsByOrderId, ArrayList<Food> getFoods, ArrayList<Food> getFoods1) {

        //  String a = "";
        for (int i = 0; i < getFoods.size(); i++) {
            if (i < 30) {
                getFoods.get(i).setImageId(getResId("image" + (i + 1), R.drawable.class));
            } else {
                getFoods.get(i).setImageId(getResId("h1", R.drawable.class));
            }
            for (int j = 0; j < getOrderDetailsByOrderId.size(); j++) {
                if (getFoods.get(i).getId() == getOrderDetailsByOrderId.get(j).getFood_id()) {
                    getFoods1.add(getFoods.get(i));
                    //a += " " + getFoods.get(i).getId();
                    break;
                }
            }
        }
        //  Toast.makeText(getContext(), "a = " + a + "SizeCart=" + getCarts1.size() + " and SizeFood=" + getFoods1.size(), Toast.LENGTH_SHORT).show();

    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}