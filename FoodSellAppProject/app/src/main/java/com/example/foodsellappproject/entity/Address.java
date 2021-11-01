package com.example.foodsellappproject.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_address")
public class Address {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String detail;
    @ForeignKey(entity = User.class, parentColumns = {"username"},
            childColumns = {"user_id"}, onDelete = ForeignKey.CASCADE)
    private String user_id;

    private String name;
    private String phone;
    private String statusOfDelete;

    public Address() {
    }

    public String getStatusOfDelete() {
        return statusOfDelete;
    }

    public void setStatusOfDelete(String statusOfDelete) {
        this.statusOfDelete = statusOfDelete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
