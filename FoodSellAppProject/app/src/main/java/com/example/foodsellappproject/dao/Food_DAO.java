package com.example.foodsellappproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodsellappproject.entity.Food;

import java.util.List;

@Dao
public interface Food_DAO {
    @Insert
    void insert(Food food);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM tbl_food")
    List<Food> listAll();

    @Query("SELECT * FROM tbl_food WHERE id=:id")
    List<Food> listFoodById(int id);

//    @Query("SELECT * FROM tbl_food WHERE statusOfFood=:statusOfFood")
//    List<Food> listAllByStatusOfFood(String statusOfFood);

    @Query("SELECT * FROM tbl_food as f JOIN tbl_food_category as c ON f.id_type = c.id ")
    List<Food> listAddedFoodsToCart();
}
