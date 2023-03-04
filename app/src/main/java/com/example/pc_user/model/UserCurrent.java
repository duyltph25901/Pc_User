package com.example.pc_user.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserCurrent")
public class UserCurrent {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id; // pk
    private String email, pass;

    public UserCurrent(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
