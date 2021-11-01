package com.example.foodsellappproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodsellappproject.entity.Comment;
import com.example.foodsellappproject.entity.User;

import java.util.List;

@Dao
public interface Comment_DAO {
    @Insert
    void insert(Comment comment);

    @Update
    void update(Comment comment);

    @Delete
    void delete(Comment comment);

    @Query("SELECT * FROM tbl_comment ORDER BY commentTime DESC")
    List<Comment> listAll();

    @Query("SELECT * FROM tbl_comment WHERE food_id=:food_id ORDER BY commentTime DESC")
    List<Comment> getCommentsByFoodId(int food_id);
}
