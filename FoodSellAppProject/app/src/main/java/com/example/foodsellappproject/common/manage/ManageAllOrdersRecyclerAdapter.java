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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.controller.manage.ManageDetailedOrderActivity;
import com.example.foodsellappproject.controller.manage.ManageOrderActivity;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ManageAllOrdersRecyclerAdapter extends RecyclerView.Adapter<ManageAllOrdersRecyclerAdapter.ViewHolder> {

    private ArrayList<Order> getOrders;
    private ArrayList<OrderDetail> getOrderDetails;

    public ManageAllOrdersRecyclerAdapter(ArrayList<Order> getOrders, ArrayList<OrderDetail> getOrderDetails) {
        this.getOrders = getOrders;
        this.getOrderDetails = getOrderDetails;
    }

    @NonNull
    @Override
    public ManageAllOrdersRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_custom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ManageAllOrdersRecyclerAdapter.ViewHolder holder, final int position) {

        SharedPreferences prefStatusOrder = holder.itemView.getContext().getSharedPreferences("statusOfOrder1", Context.MODE_PRIVATE);
        String statusOfOrder = prefStatusOrder.getString("statusOfOrder1", "delivered");
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

                    Intent intent = new Intent(v.getContext(), ManageDetailedOrderActivity.class);
                    v.getContext().startActivity(intent);

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
            holder.tvDeliveredStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(holder, getOrders.get(position).getId());
                }
            });

            holder.tvBtnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = v.getContext().getSharedPreferences("handleCbComment", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("orderId", getOrders.get(position).getId());
                    editor.commit();

                    Intent intent = new Intent(v.getContext(), ManageDetailedOrderActivity.class);
                    v.getContext().startActivity(intent);

                }
            });
        }

    }

    private void showAlertDialog(final ViewHolder holder, final int orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setMessage("Bạn có muốn chuyển mặt hàng này sang cho vận chuyển không ?");
        builder.setIcon(android.R.drawable.star_big_on);


        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBConnection db = DBConnection.getDBConnection(holder.itemView.getContext());
//                final SharedPreferences pref = holder.itemView.getContext().getSharedPreferences("handleCbComment", Context.MODE_PRIVATE);
//                int orderId = pref.getInt("orderId", 0);
                ArrayList<Order> getOrderById = (ArrayList<Order>) db.createOrderDao().getOrderById(orderId);
                getOrderById.get(0).setStatusOfOrder("delivered");
                db.createOrderDao().update(getOrderById.get(0));

                Toast.makeText(holder.itemView.getContext(), "Đã chuyển cho đơn vị vận chuyển.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(), ManageOrderActivity.class);
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
        return getOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId, tvOrderDate, tvOrderQuantity, tvBtnDetail, tvDeliveredStatus, tvTotalOfBill;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderQuantity = itemView.findViewById(R.id.tvOrderQuantity);
            tvBtnDetail = itemView.findViewById(R.id.tvBtnDetail);
            tvDeliveredStatus = itemView.findViewById(R.id.tvDeliveredStatus);
            tvTotalOfBill = itemView.findViewById(R.id.tvTotalOfBill);

        }

    }
}
