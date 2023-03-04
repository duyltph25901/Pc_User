package com.example.pc_user.functions.interact_db_local;

import android.app.Activity;
import android.util.Log;

import com.example.pc_user.db_local.UserCurrentDatabase;
import com.example.pc_user.db_local.bill.BillDatabase;
import com.example.pc_user.model.UserCurrent;
import com.example.pc_user.model.view_model.Bill;

public class DBLocal {
    // interact with user
    public static void saveUserCurrent(Activity activity, String email, String pass) {
        UserCurrentDatabase.getInstance(activity).userDAO().clearTable();
        Log.d("Clear_Table_Sqlite", "Clear table successful!");

        UserCurrent user = new UserCurrent(email, pass);
        UserCurrentDatabase.getInstance(activity).userDAO().insert(user);
        Log.d("Save_To_DB_Local", "Insert new user successful!");
    }

    // interact with bill
    public static void saveBillCurrent(Activity activity, String idOrder) {
        BillDatabase.getInstance(activity).BillDao().clearData();
        Log.d("Clear_Table_Bill_SQLite", "Clear table success");

        Bill bill = new Bill(idOrder);
        BillDatabase.getInstance(activity).BillDao().insert(bill);
        Log.d("Save_To_DB_Local", "Insert new bill successful!");
    }
}
