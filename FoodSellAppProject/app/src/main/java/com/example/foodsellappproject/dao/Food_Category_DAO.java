package com.example.foodsellappproject.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.foodsellappproject.entity.Food_Category;

import java.util.List;

@Dao
public interface Food_Category_DAO {

    @Query("SELECT * FROM tbl_food_category")
    List<Food_Category> getFoodCategories();
}
