package com.example.foodsellappproject.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_cart")
public class Cart {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ForeignKey(entity = User.class, parentColumns = {"username"},
            childColumns = {"user_id"},
            onDelete = ForeignKey.CASCADE)
    @ColumnInfo
    private String user_id;
    @ForeignKey(entity = Food.class, parentColumns = {"id"},
            childColumns = {"food_id"}, onDelete = ForeignKey.CASCADE)
    @ColumnInfo
    private int food_id;
    @ColumnInfo
    private int quantity;

    public Cart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
