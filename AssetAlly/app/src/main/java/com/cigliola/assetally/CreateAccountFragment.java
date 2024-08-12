package com.cigliola.assetally;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cigliola.assetally.model.User;
import com.cigliola.assetally.viewmodel.LoginViewModel;

// Fragment for creating a new user account
public class CreateAccountFragment extends Fragment {

    private LoginViewModel viewModel;
    private EditText emailEditText;
    private EditText passwordEditText;


    public CreateAccountFragment() {
        // Required empty public constructor
    }

    public static CreateAccountFragment newInstance() {
        CreateAccountFragment fragment = new CreateAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the LoginViewModel from the host activity to share
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEditText = view.findViewById(R.id.newEmailEditText);
        passwordEditText = view.findViewById(R.id.newPasswordEditText);
        Button createAccountButton = view.findViewById(R.id.createNewAccountButton);
        Button cancelButton = view.findViewById(R.id.cancelCreateAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            // Account creation
            @Override
            public void onClick(View v) {
                if (emailEditText.getText() != null) {
                    // Validate email address for new account
                    String emailToValidate = String.valueOf(emailEditText.getText());
                    if (!validateEmail(emailToValidate)) {
                        Toast.makeText(getContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    } else {
                        // If email is valid and a password was entered, continue account creation
                        if (passwordEditText.getText() != null) {
                            String newPassword = String.valueOf(passwordEditText.getText());
                            User newUser = new User(emailToValidate, newPassword);
                            // Add user to the database
                            viewModel.addUser(newUser);
                            Toast.makeText(getContext(), "Account created.", Toast.LENGTH_SHORT).show();
                            // Close the fragment and return to LoginActivity
                            requireActivity().getSupportFragmentManager().popBackStack();
                        }
                    }

                } else {
                    Toast.makeText(getContext(), "Please enter an email address and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
    }

    // Validates an email address using Android's Patterns utility
    private boolean validateEmail(String emailToValidate) {
        return (emailToValidate != null && Patterns.EMAIL_ADDRESS.matcher(emailToValidate).matches());
    }

    // Handles action for the cancel button. Pops back stack to return to LoginActivity
    public void onCancel() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

}