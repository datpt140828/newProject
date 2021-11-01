package com.example.foodsellappproject.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.dao.User_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.User;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    private EditText etLastNameFirstName, etLoginName, etPasswordEnter, etPasswordAgainEnter;
    private Button btnSignUp;
    private DBConnection db;
    private User_DAO userDAO;
    private List<User> getUsers = new ArrayList<>();

    private void loadView() {
        etLastNameFirstName = findViewById(R.id.etFullName);
        etLoginName = findViewById(R.id.etLoginName);
        etPasswordEnter = findViewById(R.id.etPassword);
        etPasswordAgainEnter = findViewById(R.id.etPassword2);
        btnSignUp = findViewById(R.id.btnSignUp);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        //load view from layout
        loadView();

        //load data from room
        db = DBConnection.getDBConnection(this);
        userDAO = db.createUserDAO();
        getUsers = userDAO.listAll();

        //handle button sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUp();
            }
        });
    }

    private void singUp() {
        int markSignUp = 0;
        for (User u : getUsers) {
            if (etLoginName.getText().toString().equalsIgnoreCase(u.getUsername())
                    || etLoginName.getText().toString().equalsIgnoreCase(u.getPhone())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setTitle("Error !");
                builder.setMessage("Email or phone is existed.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                markSignUp = 1;
                break;
            }
        }

        if (markSignUp == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            if (etLastNameFirstName.getText().toString().trim().isEmpty()) {
                builder.setTitle("Error !");
                builder.setMessage("Họ và tên không được trống.");
            } else if (etLoginName.getText().toString().trim().isEmpty()) {
                builder.setTitle("Error !");
                builder.setMessage("Tên đăng nhập không được trống.");
            } else if (etPasswordEnter.getText().toString().trim().isEmpty()) {
                builder.setTitle("Error !");
                builder.setMessage("Mật khẩu không được trống.");
            } else if (etPasswordEnter.getText().toString().equalsIgnoreCase(etPasswordAgainEnter.getText().toString())) {
                User u = new User();
                u.setUsername(etLoginName.getText().toString());
                u.setPassword(etPasswordEnter.getText().toString());
                u.setFullName(etLastNameFirstName.getText().toString());
                u.setStatusOfUser("active");
                u.setRole(1);
                userDAO.insert(u);
                builder.setTitle("Success !");
                builder.setMessage(" You have just registered successful.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

            } else {
                builder.setTitle("Error !");
                builder.setMessage("Please enter password2 equal password1.");
            }


            AlertDialog alertDialog = builder.create();
//alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }
}