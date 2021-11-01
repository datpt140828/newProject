package com.example.foodsellappproject.controller.manage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.CommentRecyclerViewAdapter;
import com.example.foodsellappproject.dao.Cart_DAO;
import com.example.foodsellappproject.dao.Food_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Comment;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.User;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ViewProductDetailActivity extends AppCompatActivity {

    private TextView tvNameOfFood1, tvDescription,tvPriceA;
    private ImageView ivFood;
    private Button btnModify;

    private DBConnection db;
    private Food_DAO foodDAO;
    private Cart_DAO cartDAO;
    private ArrayList<Food> getFoods = new ArrayList<>();

    private void loadView() {
        tvNameOfFood1 = findViewById(R.id.tvNameOfFood1);
        tvDescription = findViewById(R.id.tvDescription);
        ivFood = findViewById(R.id.ivFood);
        btnModify = findViewById(R.id.btnModify);
        tvPriceA = findViewById(R.id.tvPriceA);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_detail);
        getSupportActionBar().hide();

        //load view from layout
        loadView();

        //load data from room
        db = DBConnection.getDBConnection(this);
        foodDAO = db.createFoodDAO();
        getFoods = (ArrayList<Food>) foodDAO.listAll();
        for (int i = 0; i < getFoods.size(); i++) {
            if (i < 30) {
                getFoods.get(i).setImageId(getResId("image" + (i + 1), R.drawable.class));
            } else {
                getFoods.get(i).setImageId(getResId("h1", R.drawable.class));
            }
        }
        SharedPreferences pref = getSharedPreferences("chooseFood", Context.MODE_PRIVATE);
        String nameOfFood = pref.getString("nameOfFood", "");
        int id_type = 0;
        int food_id = 0;
        if (!nameOfFood.trim().isEmpty()) {

            //handle some content of textview
            for (int i = 0; i < getFoods.size(); i++) {
                if (nameOfFood.equalsIgnoreCase(getFoods.get(i).getName())) {
                    tvNameOfFood1.setText(getFoods.get(i).getName());
                    tvPriceA.setText(getFoods.get(i).getPrice());
                    if (i < 30) {
                        ivFood.setImageResource(getResId("image" + (i + 1), R.drawable.class));
                    } else {
                        ivFood.setImageResource(getResId("h1", R.drawable.class));
                    }

                    tvDescription.setText(getFoods.get(i).getDescription());
                    id_type = getFoods.get(i).getId_type();
                    food_id = i + 1;
                    break;
                }
            }

            //modify
        }
        final int finalFood_id = food_id;
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProductDetailActivity.this, ModifyProductActivity.class);
                intent.putExtra("food_id", finalFood_id);
                startActivity(intent);
            }
        });

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