package com.example.foodsellappproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodsellappproject.entity.Address;
import com.example.foodsellappproject.entity.User;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface Address_DAO {
    @Insert
    void insert(Address address);

    @Update
    void update(Address address);

    @Query("UPDATE tbl_address SET name=:name, detail=:detail, phone=:phone WHERE id=:id ")
    void updateById(String name, String detail, String phone, int id);

    @Delete
    void delete(Address address);

    @Query("UPDATE tbl_address SET statusOfDelete=:statusOfDelete WHERE id=:id")
    void updateStatusOfDelete(String statusOfDelete,int id);

    @Query("SELECT * FROM tbl_address WHERE statusOfDelete=:statusOfDelete")
    List<Address> listAll(String statusOfDelete);

    @Query("SELECT * FROM tbl_address WHERE user_id=:user_id")
    List<Address> listAllByUserId(String user_id);

    @Query("SELECT * FROM tbl_address WHERE user_id=:user_id AND statusOfDelete=:statusOfDelete")
    List<Address> listAllByUserId1(String user_id,String statusOfDelete);

    @Query("SELECT * FROM tbl_address WHERE id=:addressId")
    List<Address> listAddressById(int addressId);
}
