package com.example.pc_user.db_local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pc_user.model.UserCurrent;

@Database(entities = {UserCurrent.class}, version = 1, exportSchema = false)
public abstract class UserCurrentDatabase extends RoomDatabase {
    private static final String tableName = "UserCurrent.db";
    public static UserCurrentDatabase instance;

    public static synchronized UserCurrentDatabase getInstance(Context context) {
        return (instance == null)
                ? Room.databaseBuilder(context.getApplicationContext(), UserCurrentDatabase.class, tableName)
                .allowMainThreadQueries().build()
                : instance;
    }

    public abstract UserCurrentDAO userDAO();
}
