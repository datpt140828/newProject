package com.example.foodsellappproject.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "tbl_order_detail")
public class OrderDetail {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ForeignKey(entity = User.class, parentColumns = {"username"},
            childColumns = {"user_id"}, onDelete = ForeignKey.CASCADE)
    private String user_id;
    @ForeignKey(entity = Food.class, parentColumns = {"id"},
            childColumns = {"food_id"}, onDelete = ForeignKey.CASCADE)
    private int food_id;
    private Double moneyOfShip;

    @ColumnInfo(defaultValue = "1")
    private int quantity;
    private Double moneyOfOrderByFood;
    @ForeignKey(entity = Order.class, parentColumns = {"id"},
            childColumns = {"order_id"}, onDelete = ForeignKey.CASCADE)
    private int order_id;

    public OrderDetail() {
    }

    public Double getMoneyOfShip() {
        return moneyOfShip;
    }

    public void setMoneyOfShip(Double moneyOfShip) {
        this.moneyOfShip = moneyOfShip;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public Double getMoneyOfOrderByFood() {
        return moneyOfOrderByFood;
    }

    public void setMoneyOfOrderByFood(Double moneyOfOrderByFood) {
        this.moneyOfOrderByFood = moneyOfOrderByFood;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
