package com.example.pc_user.db_local.bill;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pc_user.model.view_model.Bill;

@Database(entities = {Bill.class}, version = 1, exportSchema = false)
public abstract class BillDatabase extends RoomDatabase {
    private static final String TABLE_NAME = "BillDatabase.db";
    public static BillDatabase instance;

    public static synchronized BillDatabase getInstance(Context context) {
        return (instance == null)
                ? Room.databaseBuilder(context.getApplicationContext(), BillDatabase.class, TABLE_NAME)
                .allowMainThreadQueries().build()
                : instance;
    }

    public abstract BillDAO BillDao();
}
