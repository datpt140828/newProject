package com.example.foodsellappproject.controller.manage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.manage.ManagementListRecyclerAdapter;
import com.example.foodsellappproject.controller.LoginActivity;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView rvAdmin;
    private Button btnLogoutAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().hide();
        rvAdmin = findViewById(R.id.rvAdmin);
        ManagementListRecyclerAdapter adapter = new ManagementListRecyclerAdapter();
        rvAdmin.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAdmin.setLayoutManager(layoutManager);


        btnLogoutAdmin = findViewById(R.id.btnLogoutAdmin);
        btnLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}