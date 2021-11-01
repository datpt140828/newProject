package com.example.foodsellappproject.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.foodsellappproject.common.Converters;

import java.util.Date;

@Entity(tableName = "tbl_comment")
@TypeConverters(Converters.class)
public class Comment {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String commentContent;
    private Date commentTime;
    @ForeignKey(entity = Food.class, parentColumns = {"id"},
            childColumns = {"food_id"}, onDelete = ForeignKey.CASCADE)
    private int food_id;

    //is foreign key
    private String userId;

    public Comment() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }
}
