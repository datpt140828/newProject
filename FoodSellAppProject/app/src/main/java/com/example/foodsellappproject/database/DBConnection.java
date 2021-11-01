package com.example.foodsellappproject.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.foodsellappproject.dao.Address_DAO;
import com.example.foodsellappproject.dao.Cart_DAO;
import com.example.foodsellappproject.dao.Comment_DAO;
import com.example.foodsellappproject.dao.Food_Category_DAO;
import com.example.foodsellappproject.dao.Food_DAO;
import com.example.foodsellappproject.dao.OrderDetail_DAO;
import com.example.foodsellappproject.dao.Order_DAO;
import com.example.foodsellappproject.dao.User_DAO;
import com.example.foodsellappproject.entity.Address;
import com.example.foodsellappproject.entity.Admin;
import com.example.foodsellappproject.entity.Cart;
import com.example.foodsellappproject.entity.Comment;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.Food_Category;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;
import com.example.foodsellappproject.entity.User;

@Database(entities = {User.class, Food.class, Food_Category.class,
        Cart.class, Admin.class, Order.class, OrderDetail.class, Comment.class,
        Address.class}, version = 12)
//,exportSchema = false
public abstract class DBConnection extends RoomDatabase {//implements RoomOpenHelper

    public abstract User_DAO createUserDAO();

    public abstract Food_DAO createFoodDAO();

    public abstract Food_Category_DAO createFoodCategoryDAO();

    public abstract Cart_DAO createCartDao();

    public abstract Comment_DAO createCommentDao();

    public abstract Address_DAO createAddressDao();

    public abstract OrderDetail_DAO createOrderDetailDao();

    public abstract Order_DAO createOrderDao();

    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_order_detail "
                    + " ADD COLUMN quantity INTEGER NOT NULL DEFAULT(1)");//DEFAULT(1)
        }
    };
    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_order "
                    + " ADD COLUMN moneyOfShip REAL ");

        }
    };
    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_address "
                    + " ADD COLUMN name TEXT ");
            database.execSQL("ALTER TABLE tbl_address "
                    + " ADD COLUMN phone TEXT ");
        }
    };
    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_order "
                    + " ADD COLUMN statusOfOrder TEXT ");
        }
    };
    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_order "
                    + " ADD COLUMN addressId INTEGER NOT NULL DEFAULT(1)");
        }
    };
    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_address "
                    + " ADD COLUMN statusOfDelete TEXT");
        }
    };
    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_comment "
                    + " ADD COLUMN userId TEXT");
        }
    };
    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION9 = new Migration(8, 9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_user "
                    + " ADD COLUMN avtImageId INTEGER NOT NULL DEFAULT(1)");
        }
    };
    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION10 = new Migration(9, 10) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_user "
                    + " ADD COLUMN role INTEGER NOT NULL DEFAULT(1)");
        }
    };

    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION11 = new Migration(10, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_food "
                    + " ADD COLUMN statusOfFood TEXT ");
        }
    };

    public static final Migration MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION12 = new Migration(11, 12) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE tbl_user "
                    + " ADD COLUMN statusOfUser TEXT ");
        }
    };

    public static DBConnection getDBConnection(Context context) {
        return Room.databaseBuilder(context, DBConnection.class, "project.db")
                //.fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION11,MIGRATION_PREVIOUS_VERSION_TO_NEXT_VERSION12)
                .allowMainThreadQueries()
                .build();
    }

}
