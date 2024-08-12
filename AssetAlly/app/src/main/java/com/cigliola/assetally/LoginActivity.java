package com.cigliola.assetally;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.cigliola.assetally.model.StockItem;
import com.cigliola.assetally.model.User;
import com.cigliola.assetally.viewmodel.InventoryViewModel;
import com.cigliola.assetally.viewmodel.LoginViewModel;

// LoginActivity manages user login and navigation to account creation
public class LoginActivity extends AppCompatActivity {
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize the viewModel for handling login operations
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(LoginViewModel.class);

        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Observe the LiveData for user data and handle login logic
                viewModel.getUserByEmail(String.valueOf(emailEditText.getText())).observe(LoginActivity.this, currentUser -> {
                    if (currentUser != null && String.valueOf(passwordEditText.getText()).equals(currentUser.getPassword())) {
                        Intent intent = new Intent(LoginActivity.this, InventoryActivity.class);
                        startActivity(intent);
                        finish();  // Finish activity after successful login
                    } else {
                        showLoginFailedDialog();  // Show error dialog if login fails
                    }
                });
            }
        });
        Button createAccountButton = findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateAccountFragment();
            } // Open fragment for creating a new account
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Opens the fragment for creating a new user account
    private void openCreateAccountFragment() {
        CreateAccountFragment fragment = new CreateAccountFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
    }

    // Displays a dialog when the user login fails
    private void showLoginFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Email address and password do not match records. Verify information or create a new account.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}