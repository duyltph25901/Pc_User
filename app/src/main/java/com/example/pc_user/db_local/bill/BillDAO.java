package com.example.pc_user.db_local.bill;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pc_user.model.view_model.Bill;

@Dao
public interface BillDAO {
    @Insert
    void insert(Bill bill);

    @Query("SELECT * FROM BILL ORDER BY `id` DESC LIMIT 1")
    Bill getBill();

    @Query("DELETE FROM BILL")
    void clearData();
}
