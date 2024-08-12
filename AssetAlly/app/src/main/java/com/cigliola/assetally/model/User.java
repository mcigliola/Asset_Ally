package com.cigliola.assetally.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entity representing a user in the database
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private long mId;
    @ColumnInfo(name = "email")
    private String mEmail;
    @ColumnInfo(name = "password")
    private String mPassword;

    // Role attribute as placeholder for future role-based permission schema
    @ColumnInfo(name = "role")
    private String mRole = "user"; // Admin to assign roles based on data security needs

    // Default constructor required for Room
    public User() {}

    // Constructor with basic information
    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    // Getters and setters of attributes
    public long getId() {
        return mId;
    }

    public void setId(long id) {this.mId = id;}

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getPassword() { return mPassword;}

    public void setPassword(String password) { this.mPassword = password;}

    public String getRole() {return mRole;}

    public void setRole(String role) {this.mRole = role;}
}




