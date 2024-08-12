package com.cigliola.assetally.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cigliola.assetally.model.User;

import java.util.List;

// Data Access Object for managing User entities
@Dao
public interface UserDao {
    // Retrieves all users from the database
    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    // Retrieves a single user by their email address
    @Query("SELECT * FROM users WHERE email = :email")
    LiveData<User> getUserByEmail(String email);

    // Retrieves a single user by their ID
    @Query("SELECT * FROM users WHERE mId = :userId")
    LiveData<User> getUserById(long userId);

    // Adds a user to the database
    @Insert
    void insert(User user);

    // Updates an existing user in the database
    @Update
    void update(User user);

    // Deletes a user from the database
    @Delete
    void delete(User user);

}
