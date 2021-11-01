package com.example.foodsellappproject.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.controller.manage.AdminActivity;
import com.example.foodsellappproject.dao.User_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.User;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private TextView tvNotHaveAccount;
    private Button btnLogin;
    private EditText etUsername, etPassword;
    private CheckBox chkRemember;

    private DBConnection db;
    private User_DAO userDAO;
    private List<User> users = new ArrayList<>();

    private void loadView() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        chkRemember = findViewById(R.id.chkRemember);
        tvNotHaveAccount = findViewById(R.id.tvNotHaveAccount);
        btnLogin = findViewById(R.id.btnLogin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //load view from layout
        loadView();

        //load data from room
        db = DBConnection.getDBConnection(this);
        userDAO = db.createUserDAO();
        users = userDAO.listAll();

        //Go to create new account screen
        tvNotHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        //Go to home screen
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCheckLogin();
            }
        });

        //handle remember checkbox
        SharedPreferences pref = getSharedPreferences("login", Context.MODE_PRIVATE);
        String un = pref.getString("username", "");
        String pw = pref.getString("password", "");
        boolean chk = pref.getBoolean("chkRemember", false);
        if (chk == true) {
            etUsername.setText(un);
            etPassword.setText(pw);
            chkRemember.setChecked(true);
        }
    }

    private void dialogCheckLogin() {
        int m = 0;
        for (int i = 0; i < users.size(); i++) {
            if (etUsername.getText().toString().equalsIgnoreCase(users.get(i).getUsername())
                    && users.get(i).getStatusOfUser().equalsIgnoreCase("blocked")) {
                Toast.makeText(this, "Tài khoản này đã bị khóa.", Toast.LENGTH_SHORT).show();
                m = 1;
                break;
            }
        }
        if (m == 0) {
            int markUP = 0;
            for (int i = 0; i < users.size(); i++) {
                if (etUsername.getText().toString().equalsIgnoreCase(users.get(i).getUsername())
                        && etPassword.getText().toString().equalsIgnoreCase(users.get(i).getPassword())) {
                    markUP = 1;
                    break;
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (etUsername.getText().toString().trim().isEmpty()
                    || etPassword.getText().toString().trim().isEmpty()
                    || markUP == 0) {

                builder.setTitle("Error !");
                builder.setMessage("Username or password is incorrect !");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } else if (markUP == 1) {
                SharedPreferences pref = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_logined", etUsername.getText().toString());
                if (chkRemember.isChecked()) {

                    editor.putString("username", etUsername.getText().toString());
                    editor.putString("password", etPassword.getText().toString());
                    editor.putBoolean("chkRemember", true);
                } else {
                    editor.putBoolean("chkRemember", false);
                }
                editor.commit();


                SharedPreferences pref1 = getSharedPreferences("filter", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.putInt("applied", 0);
                editor1.commit();

                User getUsers = db.createUserDAO().getUserById(etUsername.getText().toString());

                if (getUsers.getRole() == 1) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            }


        }
    }

}