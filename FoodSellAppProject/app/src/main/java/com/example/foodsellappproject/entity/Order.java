package com.example.foodsellappproject.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.foodsellappproject.common.Converters;

import java.util.Date;

@Entity(tableName = "tbl_order")
@TypeConverters(Converters.class)
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double totalOfBillMoney;
    private Date orderTime;
    private Double moneyOfShip;
    private String statusOfOrder;
    private int addressId;

    public Order() {
    }

    public String getStatusOfOrder() {
        return statusOfOrder;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setStatusOfOrder(String statusOfOrder) {
        this.statusOfOrder = statusOfOrder;
    }

    public Double getMoneyOfShip() {
        return moneyOfShip;
    }

    public void setMoneyOfShip(Double moneyOfShip) {
        this.moneyOfShip = moneyOfShip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getTotalOfBillMoney() {
        return totalOfBillMoney;
    }

    public void setTotalOfBillMoney(Double totalOfBillMoney) {
        this.totalOfBillMoney = totalOfBillMoney;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
