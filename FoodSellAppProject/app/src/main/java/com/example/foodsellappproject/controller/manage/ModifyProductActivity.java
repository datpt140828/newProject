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

import java.util.ArrayList;

public class ModifyProductActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_modify_product);
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
        Intent intent = getIntent();
        int food_id = 0;
        if (intent != null) {
            food_id = intent.getIntExtra("food_id", 0);
        }

        ArrayList<Food> getFoods = (ArrayList<Food>) db.createFoodDAO().listFoodById(food_id);
        etName.setText(getFoods.get(0).getName());
        etPrice.setText(getFoods.get(0).getPrice());
        etDescription.setText(getFoods.get(0).getDescription());
        int positionFoodCategory = 0;
        for (int i = 0; i < getFood_categories.size(); i++) {
            if (getFoods.get(0).getId_type() == getFood_categories.get(i).getId()) {
                positionFoodCategory = i;
                break;
            }
        }
        spinnerFoodCategory.setSelection(positionFoodCategory);

        int positionStatus = 0;
        for (int i = 0; i < getStatusFoods.size(); i++) {
            if (getFoods.get(0).getStatusOfFood().equalsIgnoreCase(getStatusFoods.get(i))) {
                positionStatus = i;
                break;
            }
        }
        spinnerStatusFood.setSelection(positionStatus);


        final int finalFood_id = food_id;
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Food> getFoods = (ArrayList<Food>) db.createFoodDAO().listFoodById(finalFood_id);
                getFoods.get(0).setId_type(id_type[0]);
                getFoods.get(0).setStatusOfFood(status[0]);
                getFoods.get(0).setName(etName.getText().toString());
                getFoods.get(0).setPrice(etPrice.getText().toString());
                getFoods.get(0).setDescription(etDescription.getText().toString());
                db.createFoodDAO().update(getFoods.get(0));

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Cập nhật thông tin sản phẩm thành công.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Intent intent1 = new Intent(ModifyProductActivity.this,ViewProductDetailActivity.class);
                startActivity(intent1);

            }
        });

    }
}