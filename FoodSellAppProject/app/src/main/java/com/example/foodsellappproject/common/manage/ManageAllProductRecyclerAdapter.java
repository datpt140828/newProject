package com.example.foodsellappproject.common.manage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.AddedFoodToCartRecyclerAdapter;
import com.example.foodsellappproject.controller.manage.ManageOrderActivity;
import com.example.foodsellappproject.controller.manage.ManageProductActivity;
import com.example.foodsellappproject.controller.manage.ViewProductDetailActivity;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Cart;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.Food_Category;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.ui.home.AddToCartFragment;

import java.util.ArrayList;

public class ManageAllProductRecyclerAdapter extends RecyclerView.Adapter<ManageAllProductRecyclerAdapter.ViewHolder> {

    private ArrayList<Food> getFoods;
    private ArrayList<Food_Category> getFoodCategories;

    public ManageAllProductRecyclerAdapter(ArrayList<Food> getFoods, ArrayList<Food_Category> getFoodCategories) {
        this.getFoods = getFoods;
        this.getFoodCategories = getFoodCategories;
    }

    @NonNull
    @Override
    public ManageAllProductRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_product_custom, parent, false);
        return new ManageAllProductRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ManageAllProductRecyclerAdapter.ViewHolder holder, final int position) {
        for (int i = 0; i < getFoodCategories.size(); i++) {
            if (getFoodCategories.get(i).getId() == getFoods.get(position).getId_type()) {
                holder.tvCategoryName.setText(getFoodCategories.get(i).getName());
                break;
            }
        }

        holder.ivFood.setImageResource(getFoods.get(position).getImageId());
        holder.tvFoodName.setText(getFoods.get(position).getName());
        holder.tvPrice.setText(getFoods.get(position).getPrice());

        //handle tvBtnViewDetail
        holder.tvBtnViewDetail.setOnClickListener(new View.OnClickListener() {
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

        //handle tvBtnDelete
        if(getFoods.get(position).getStatusOfFood().equalsIgnoreCase("deleted")){
            holder.tvBtnDelete.setText("M???");
        }else{
            holder.tvBtnDelete.setText("Kh??a");
        }
        holder.tvBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(holder, position);
            }
        });

    }

    private void showAlertDialog(final ViewHolder holder, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setIcon(android.R.drawable.star_big_on);
        if(getFoods.get(position).getStatusOfFood().equalsIgnoreCase("deleted")){
            builder.setMessage("B???n c?? ch???c ch???n mu???n chuy???n m???t h??ng n??y sang tr???ng th??i c?? h??ng kh??ng ?");

        }else{
            builder.setMessage("B???n c?? ch???c ch???n mu???n chuy???n m???t h??ng n??y sang tr???ng th??i h???t h??ng kh??ng ?");

        }
        builder.setPositiveButton("C??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(holder.tvBtnDelete.getText().toString().equalsIgnoreCase("Kh??a")){
                    DBConnection db = DBConnection.getDBConnection(holder.itemView.getContext());
                    Food f = getFoods.get(position);
                    f.setStatusOfFood("deleted");
                    db.createFoodDAO().update(f);

                    Toast.makeText(holder.itemView.getContext(), "???? chuy???n tr???ng th??i sang h???t h??ng.", Toast.LENGTH_SHORT).show();
                }else{
                    DBConnection db = DBConnection.getDBConnection(holder.itemView.getContext());
                    Food f = getFoods.get(position);
                    f.setStatusOfFood("active");
                    db.createFoodDAO().update(f);

                    Toast.makeText(holder.itemView.getContext(), "???? chuy???n tr???ng th??i sang c?? h??ng.", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(holder.itemView.getContext(), ManageProductActivity.class);
                holder.itemView.getContext().startActivity(intent);

            }
        });

        builder.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
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
        return getFoods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFood;
        private TextView tvFoodName, tvCategoryName, tvPrice, tvBtnViewDetail, tvBtnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.ivFood);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvBtnViewDetail = itemView.findViewById(R.id.tvBtnViewDetail);
            tvBtnDelete = itemView.findViewById(R.id.tvBtnDelete);
        }
    }
}
