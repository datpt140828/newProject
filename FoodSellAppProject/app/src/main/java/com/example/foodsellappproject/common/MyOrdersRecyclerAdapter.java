package com.example.foodsellappproject.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;
import com.example.foodsellappproject.ui.cart.OrderDetailFragment;
import com.example.foodsellappproject.ui.profile.MyOrderFragment;
import com.example.foodsellappproject.ui.profile.ProfileFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyOrdersRecyclerAdapter extends RecyclerView.Adapter<MyOrdersRecyclerAdapter.ViewHolder> {

    private ArrayList<Order> getOrders;
    private ArrayList<OrderDetail> getOrderDetails;

    public MyOrdersRecyclerAdapter(ArrayList<Order> getOrders, ArrayList<OrderDetail> getOrderDetails) {
        this.getOrders = getOrders;
        this.getOrderDetails = getOrderDetails;
    }

    @NonNull
    @Override
    public MyOrdersRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_custom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersRecyclerAdapter.ViewHolder holder, final int position) {

        SharedPreferences prefStatusOrder = holder.itemView.getContext().getSharedPreferences("statusOfOrder", Context.MODE_PRIVATE);
        String statusOfOrder = prefStatusOrder.getString("statusOfOrder", "delivered");
        //handle delivered
        if (statusOfOrder.equalsIgnoreCase("delivered")) {
            holder.tvOrderId.setText(String.valueOf(getOrders.get(position).getId()));
            holder.tvOrderDate.setText(new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss").format(getOrders.get(position).getOrderTime()));
            holder.tvTotalOfBill.setText(String.valueOf(getOrders.get(position).getTotalOfBillMoney()));
            int count = 0;
            for (int i = 0; i < getOrderDetails.size(); i++) {
                if (getOrderDetails.get(i).getOrder_id() == getOrders.get(position).getId()) {
                    count++;
                }
            }
            holder.tvOrderQuantity.setText(String.valueOf(count));
            holder.tvDeliveredStatus.setText("Đã giao");//getOrders.get(position).getStatusOfOrder()
            holder.tvBtnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = v.getContext().getSharedPreferences("handleCbComment", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("orderId", getOrders.get(position).getId());
                    editor.commit();
                    OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                Bundle b = new Bundle();
//                b.putInt("orderId",getOrders.get(position).getId());
//                orderDetailFragment.setArguments(b);
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.myOrderFragment, orderDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else {//handle delivering
            holder.tvOrderId.setText(String.valueOf(getOrders.get(position).getId()));
            holder.tvOrderDate.setText(new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss").format(getOrders.get(position).getOrderTime()));
            holder.tvTotalOfBill.setText(String.valueOf(getOrders.get(position).getTotalOfBillMoney()));

            int count = 0;
            for (int i = 0; i < getOrderDetails.size(); i++) {
                if (getOrderDetails.get(i).getOrder_id() == getOrders.get(position).getId()) {
                    count++;
                }
            }
            holder.tvOrderQuantity.setText(String.valueOf(count));
            holder.tvDeliveredStatus.setTextColor(Color.RED);
            holder.tvDeliveredStatus.setText("Đang chờ xác nhận");//getOrders.get(position).getStatusOfOrder()
            holder.tvBtnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = v.getContext().getSharedPreferences("handleCbComment", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("orderId", getOrders.get(position).getId());
                    editor.commit();
                    OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.myOrderFragment, orderDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return getOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId, tvOrderDate, tvOrderQuantity, tvBtnDetail, tvDeliveredStatus, tvTotalOfBill;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            SharedPreferences prefStatusOrder = itemView.getContext().getSharedPreferences("statusOfOrder", Context.MODE_PRIVATE);
//            String statusOfOrder = prefStatusOrder.getString("statusOfOrder", "delivered");
//            if (statusOfOrder.equalsIgnoreCase("delivering")) {
//                IntentFilter filter2 = new IntentFilter("CHECK_DELIVERING");
//                itemView.getContext().registerReceiver(receiver2, filter2);
//            }


            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderQuantity = itemView.findViewById(R.id.tvOrderQuantity);
            tvBtnDetail = itemView.findViewById(R.id.tvBtnDetail);
            tvDeliveredStatus = itemView.findViewById(R.id.tvDeliveredStatus);
            tvTotalOfBill = itemView.findViewById(R.id.tvTotalOfBill);

        }

//        private BroadcastReceiver receiver2 = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                int percent = intent.getIntExtra("percent", -1);
//                tvDeliveredStatus.setText("Đang xử lý: " + percent + "%");
//                if (percent == 100) {
//                    //tvDeliveredStatus.setText("Đã giao");
//                    //tvDeliveredStatus.setTextColor(Color.GREEN);
//
//                    SharedPreferences prefStatusOrder = context.getSharedPreferences("statusOfOrder", Context.MODE_PRIVATE);
//                    final SharedPreferences.Editor editorStatusOrder = prefStatusOrder.edit();
//                    editorStatusOrder.putString("statusOfOrder", "delivered");
//                    editorStatusOrder.commit();
//
//                    MyOrderFragment myOrderFragment = new MyOrderFragment();
//                    AppCompatActivity activity = (AppCompatActivity) context;
//                    activity.getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.myOrderFragment, myOrderFragment)
//                            .addToBackStack(null)
//                            .commit();
//                }
//            }
//        };

    }
}
