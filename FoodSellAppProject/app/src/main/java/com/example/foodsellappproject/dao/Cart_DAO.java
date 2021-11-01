package com.example.foodsellappproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodsellappproject.entity.Cart;
import com.example.foodsellappproject.entity.User;

import java.util.List;

@Dao
public interface Cart_DAO {
    @Insert
    void insert(Cart cart);

    @Update
    void update(Cart cart);

    @Query(" UPDATE tbl_cart SET quantity =:quantity WHERE user_id =:user_id AND food_id =:food_id")
    void updateCart(int quantity, String user_id, int food_id);

    @Delete
    void delete(Cart cart);

    @Query("DELETE FROM tbl_cart WHERE user_id =:user_id AND food_id =:food_id")
    void deleteCart(String user_id, int food_id);

    @Query("SELECT * FROM tbl_cart")
    List<Cart> listAll();

}
