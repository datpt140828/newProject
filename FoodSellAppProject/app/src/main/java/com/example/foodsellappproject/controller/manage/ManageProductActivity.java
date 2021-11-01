package com.example.foodsellappproject.controller.manage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.AddedFoodToCartRecyclerAdapter;
import com.example.foodsellappproject.common.manage.ManageAllProductRecyclerAdapter;
import com.example.foodsellappproject.dao.Cart_DAO;
import com.example.foodsellappproject.dao.Food_Category_DAO;
import com.example.foodsellappproject.dao.Food_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Cart;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.Food_Category;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ManageProductActivity extends AppCompatActivity {

    private RecyclerView rvOrdersByUserId;
    private ImageView ivBack;

    private DBConnection db;
    private Food_DAO foodDAO;
    private Food_Category_DAO foodCategoryDAO;
    private Button btnAddNewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);
        getSupportActionBar().hide();

        //ivBack
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageProductActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
        btnAddNewProduct = findViewById(R.id.btnAddNewProduct);
        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageProductActivity.this, AddNewProductActivity.class);
                startActivity(intent);
            }
        });

        rvOrdersByUserId = findViewById(R.id.rvOrdersByUserId);

        //load data from room
        db = DBConnection.getDBConnection(this);
        foodDAO = db.createFoodDAO();
        foodCategoryDAO = db.createFoodCategoryDAO();

        ArrayList<Food> getFoods = (ArrayList<Food>) foodDAO.listAll();
        for (int i = 0; i < getFoods.size(); i++) {
            if (i < 30) {
                getFoods.get(i).setImageId(getResId("image" + (i + 1), R.drawable.class));
            } else {
                getFoods.get(i).setImageId(getResId("h1", R.drawable.class));
            }
        }
        ArrayList<Food_Category> getFoodCategories = (ArrayList<Food_Category>) foodCategoryDAO.getFoodCategories();
        setRvListOfAddedFood(getFoods, getFoodCategories);
    }

    private void setRvListOfAddedFood(ArrayList<Food> getFoods, ArrayList<Food_Category> getFoodCategories) {
        ManageAllProductRecyclerAdapter adapter = new ManageAllProductRecyclerAdapter(getFoods, getFoodCategories);
        rvOrdersByUserId.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrdersByUserId.setLayoutManager(layoutManager);
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