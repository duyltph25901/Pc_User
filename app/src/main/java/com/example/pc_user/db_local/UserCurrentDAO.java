package com.example.pc_user.db_local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pc_user.model.UserCurrent;

@Dao
public interface UserCurrentDAO {
    @Insert
    void insert(UserCurrent user);

    @Query("SELECT * FROM UserCurrent ORDER BY `id` DESC LIMIT 1")
    UserCurrent getUser();

    @Query("DELETE FROM UserCurrent")
    void clearTable();
}
