package com.example.foodsellappproject.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "tbl_food")
public class Food {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String price;
    @ColumnInfo
    private String description;
    @ForeignKey(entity = Food_Category.class, parentColumns = {"id"},
            childColumns = {"id_type"},
            onDelete = ForeignKey.CASCADE)
    @ColumnInfo
    private int id_type;
    @ColumnInfo
    private int imageId;
    private String statusOfFood;

    public Food() {
    }

    public String getStatusOfFood() {
        return statusOfFood;
    }

    public void setStatusOfFood(String statusOfFood) {
        this.statusOfFood = statusOfFood;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return name;
    }
}
