package com.example.foodsellappproject.controller.manage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.Food_Category;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AddNewProductActivity extends AppCompatActivity {

    private Spinner spinnerFoodCategory, spinnerStatusFood;
    private EditText etName, etPrice, etDescription;
    private Button buttonSave;
    private ArrayAdapter<Food_Category> adapter;
    private ArrayAdapter<String> adapter2;

    private void loadView() {
        spinnerFoodCategory = findViewById(R.id.spinnerFoodCategory);
        spinnerStatusFood = findViewById(R.id.spinnerStatusFood);
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private DBConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        getSupportActionBar().hide();

        //load view
        loadView();

        //load db
        db = DBConnection.getDBConnection(getApplicationContext());
        final ArrayList<Food_Category> getFood_categories = (ArrayList<Food_Category>) db.createFoodCategoryDAO().getFoodCategories();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, getFood_categories);
        spinnerFoodCategory.setAdapter(adapter);

        final ArrayList<String> getStatusFoods = new ArrayList<>();
        getStatusFoods.add("active");
        getStatusFoods.add("deleted");
        adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, getStatusFoods);
        spinnerStatusFood.setAdapter(adapter2);

        final int[] id_type = {0};
        spinnerFoodCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_type[0] = getFood_categories.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final String[] status = {"active"};
        spinnerStatusFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status[0] = getStatusFoods.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Food food = new Food();

                food.setId_type(id_type[0]);
                food.setStatusOfFood(status[0]);
                food.setName(etName.getText().toString());
                food.setPrice(etPrice.getText().toString());
                food.setImageId(getResId("h1", R.drawable.class));
                food.setDescription(etDescription.getText().toString());
                db.createFoodDAO().insert(food);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Thêm sản phẩm mới thành công.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Intent intent1 = new Intent(AddNewProductActivity.this,ManageProductActivity.class);
                startActivity(intent1);
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