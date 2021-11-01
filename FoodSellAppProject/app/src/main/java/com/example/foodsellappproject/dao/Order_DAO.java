package com.example.foodsellappproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;
import com.example.foodsellappproject.entity.User;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface Order_DAO {
    @Insert
    void insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("SELECT id FROM tbl_order ORDER BY id DESC LIMIT 1")
    Integer getLastId();

    @Query("SELECT * FROM tbl_order")
    List<Order> listAll();

    @Query("SELECT * FROM tbl_order WHERE statusOfOrder=:statusOfOrder")
    List<Order> listAllByStatusOfOrder(String statusOfOrder);

    @Query("SELECT * FROM tbl_order WHERE statusOfOrder=:statusOfOrder OR statusOfOrder=:statusOfOrder1")
    List<Order> listAllByStatusOfOrderNotCanceled(String statusOfOrder,String statusOfOrder1);

    @Query("SELECT * FROM tbl_order WHERE id=:orderId")
    List<Order> getOrderById(int orderId);

}
