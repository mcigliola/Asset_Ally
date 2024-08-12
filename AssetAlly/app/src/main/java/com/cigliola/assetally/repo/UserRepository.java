package com.cigliola.assetally.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.cigliola.assetally.model.User;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Class to handle the data operations for User entities
public class UserRepository {
    private static UserRepository userRepository;
    private final UserDao userDao;
    private Executor ioExecutor; // For running database operations asynchronously

    // Returns a singleton instance of the repository
    public static UserRepository getInstance(Context context) {
        if (userRepository == null) {
            userRepository = new UserRepository(context);
        }
        return userRepository;
    }

    // Private constructor to initialize DAOs and executor
    private UserRepository(Context context) {
        AssetAllyDatabase database = AssetAllyDatabase.getDatabase(context);
        userDao = database.userDao();
        ioExecutor = Executors.newSingleThreadExecutor();
    }

    // Returns list of all users as live data
    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    // Adds a new user to the database asynchronously
    public void addUser(User user) {
        ioExecutor.execute(() -> userDao.insert(user));
    }

    // Updates an existing user in the database
    public void updateUser(User currentUser) {
        ioExecutor.execute(() -> userDao.update(currentUser));
    }

    // Retrieves a single user by their ID
    public LiveData<User> getUserById(Long userId) {
        return userDao.getUserById(userId);
    }

    // Retrieves a single user by their email address
    public LiveData<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    // Deletes a user from the database asynchronously
    public void deleteUser(User user) {
        ioExecutor.execute(() -> userDao.delete(user));
    }
}
