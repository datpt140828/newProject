package com.example.foodsellappproject.controller.manage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.manage.ManageUserRecyclerAdapter;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Admin;
import com.example.foodsellappproject.entity.User;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ManageUserActivity extends AppCompatActivity {

    private RecyclerView rvListUser;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
        getSupportActionBar().hide();

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageUserActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        rvListUser = findViewById(R.id.rvListUser);
        DBConnection db = DBConnection.getDBConnection(getApplicationContext());
        ArrayList<User> getUsers = (ArrayList<User>) db.createUserDAO().listUserByRole();

        int mark = 0;
        for (int i = 0; i < getUsers.size(); i++) {
            mark++;
            getUsers.get(i).setAvtImageId(getResId("ava" + mark, R.drawable.class));
            if (mark == 4) {
                mark = 0;
            }
        }

        ManageUserRecyclerAdapter adapter = new ManageUserRecyclerAdapter(getUsers);
        rvListUser.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rvListUser.setLayoutManager(manager);
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