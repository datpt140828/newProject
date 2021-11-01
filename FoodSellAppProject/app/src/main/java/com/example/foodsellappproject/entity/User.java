package com.example.foodsellappproject.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_user")
public class User {

    @PrimaryKey
    @NonNull
    private String username;
    @ColumnInfo
    private String password;
    @ColumnInfo
    private String email2;
    @ColumnInfo
    private String fullName;
    @ColumnInfo
    private String phone;
    @ColumnInfo
    private String address;
    @ColumnInfo
    private String dob;
    private int avtImageId;
    private int role;
    private String statusOfUser;

    public User() {
    }

    public String getStatusOfUser() {
        return statusOfUser;
    }

    public void setStatusOfUser(String statusOfUser) {
        this.statusOfUser = statusOfUser;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getAvtImageId() {
        return avtImageId;
    }

    public void setAvtImageId(int avtImageId) {
        this.avtImageId = avtImageId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return username;
    }
}
