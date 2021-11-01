package com.example.foodsellappproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodsellappproject.entity.OrderDetail;
import com.example.foodsellappproject.entity.User;

import java.util.List;

@Dao
public interface OrderDetail_DAO {
    @Insert
    void insert(OrderDetail orderDetail);

    @Update
    void update(OrderDetail orderDetail);

    @Query(" UPDATE tbl_order_detail SET quantity =:quantity WHERE user_id =:user_id AND food_id =:food_id")
    void updateOrderDetail(int quantity, String user_id, int food_id);

    @Query("UPDATE tbl_order_detail SET order_id=:order_id WHERE user_id=:user_id AND order_id = 0")
    void updateOrderId(int order_id, String user_id);

    @Delete
    void delete(OrderDetail orderDetail);

    @Query(" DELETE FROM tbl_order_detail")
    void deleteAll();

    @Query("SELECT * FROM tbl_order_detail")
    List<OrderDetail> listAll();

    @Query("SELECT * FROM tbl_order_detail WHERE user_id=:user_id")
    List<OrderDetail> listAllByUserId(String user_id);

    @Query("SELECT order_id FROM tbl_order_detail\n" +
            "WHERE user_id =:user_id\n" +
            "GROUP BY order_id")
    List<Integer> listOrderIdByUserId(String user_id);

    @Query("SELECT * FROM tbl_order_detail WHERE order_id=:order_id")
    List<OrderDetail> getOrderDetailsByOrderId(int order_id);
}
