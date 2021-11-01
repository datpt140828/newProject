package com.example.foodsellappproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodsellappproject.entity.User;

import java.util.List;

@Dao
public interface User_DAO {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM tbl_user")
    List<User> listAll();

    @Query("SELECT * FROM tbl_user WHERE username=:username")
    User getUserById(String username);

    @Query("SELECT * FROM tbl_user WHERE role=1")
    List<User> listUserByRole();
}
