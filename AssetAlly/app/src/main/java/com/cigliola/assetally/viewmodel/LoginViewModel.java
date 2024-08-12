package com.cigliola.assetally.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cigliola.assetally.model.User;
import com.cigliola.assetally.model.User;
import com.cigliola.assetally.repo.UserRepository;

import java.util.List;

// ViewModel for managing user authentication and user data interactions
public class LoginViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<User>> allUsers;

    // Constructor
    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = UserRepository.getInstance(application);
        allUsers = repository.getAllUsers();
    }

    // Returns all users as LiveData
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    // Adds a user to the database
    public void addUser(User user) {
        repository.addUser(user);
    }

    // Retrieves a user by their ID and returns LiveData
    public LiveData<User> getUserById(Long userId) {
        return repository.getUserById(userId);
    }

    // Retrieves a user by their email address and returns LiveData
    public LiveData<User> getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    // Updates an existing user in the database
    public void updateUser(User currentUser) {
        repository.updateUser(currentUser);
    }

    // Deletes a user from the database
    public void deleteUser(User user) {
        repository.deleteUser(user);
    }


}
