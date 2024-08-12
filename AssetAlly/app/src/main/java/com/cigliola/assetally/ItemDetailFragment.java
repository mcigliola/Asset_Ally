package com.cigliola.assetally;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cigliola.assetally.model.StockItem;
import com.cigliola.assetally.repo.AssetAllyDatabase;
import com.cigliola.assetally.viewmodel.InventoryViewModel;

import java.util.Objects;

// Fragment to display and manage details of a stock item.
// Allows for editing item properties and deleting the item
public class ItemDetailFragment extends Fragment  implements ButtonClickListener {
    private static final String ARG_ITEM_ID = "itemId";
    private InventoryViewModel viewModel;
    public long itemId = -1; // The ID of the item being displayed. -1 means the object is null
    private StockItem currentItem; // Current item being edited
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ToggleButton alertsToggleButton;
    private EditText itemNameEditText;
    private EditText itemQuantityEditText;
    private EditText itemDescriptionEditText;
    private EditText alertQuantityEditText;

    public ItemDetailFragment() {
        // Required empty public constructor
    }

    // Uses the item ID to create a new instance of the fragment displaying details for that item.
    public static ItemDetailFragment newInstance(long itemId) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);
        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            itemId = getArguments().getLong(ARG_ITEM_ID);
        }
        // Set up the request permission launcher for sending SMS alerts on low inventory
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) {
                alertsToggleButton.setChecked(false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }

    // Sets up UI components and defines behavior
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemNameEditText = view.findViewById(R.id.itemNameEditText);
        itemQuantityEditText = view.findViewById(R.id.itemQuantityEditText);
        itemDescriptionEditText = view.findViewById(R.id.itemDescriptionEditText);
        alertQuantityEditText = view.findViewById(R.id.alertQuantityEditText);
        alertsToggleButton = view.findViewById(R.id.alertsToggleButton);
        Button saveItemButton = view.findViewById(R.id.saveItemButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button deleteItemButton = view.findViewById(R.id.deleteButton);

        // If the item ID is set (item exists), load the details into the appropriate fields
        if (itemId != -1) {
            viewModel.getItemById(itemId).observe(getViewLifecycleOwner(), item -> {
                if (item != null) {
                    currentItem = item;
                    itemNameEditText.setText(item.getName());
                    itemQuantityEditText.setText(String.valueOf(item.getQuantity()));
                    itemDescriptionEditText.setText(item.getDescription());
                    alertQuantityEditText.setText(String.valueOf(item.getAlertQuantity()));
                }
            });
        }
        // If item ID is not set (item doesn't exist), load empty strings
        else {
            itemNameEditText.setText("");
            itemQuantityEditText.setText("");
            itemDescriptionEditText.setText("");
            alertQuantityEditText.setText("0");
        }

        // Set listeners
        saveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });

        alertsToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    requestSmsPermission();
                }
            }
        });
    }

    // Request permission to send SMS messages as low inventory alerts
    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                showRationaleDialog();
            }
            else {
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            }
        }
    }

    // Show dialog to explain why SMS permission is needed
    private void showRationaleDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("SMS Permission Needed")
                .setMessage("This permission is needed to send low inventory notifications via text.  Without it, the app cannot notify you.")
                .setPositiveButton("OK", ((dialog, which) -> requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)))
                .setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()))
                .create()
                .show();
    }

    // Handles logic for saving a new item to the database or updating an existing one
    public void onSave() {
        try {
            // Get information from UI fields
            String itemName = itemNameEditText.getText().toString();
            String itemDescription = itemDescriptionEditText.getText().toString();
            int itemQuantity = Integer.parseInt(itemQuantityEditText.getText().toString());
            int alertQuantity = Integer.parseInt(alertQuantityEditText.getText().toString());

            if (currentItem == null) {
                // If the item doesn't yet exist, add it
                viewModel.getItemByName(itemName).observe(getViewLifecycleOwner(), existingItem -> {
                    if (existingItem != null) {
                        Toast.makeText(getContext(), "An item with the same name already exists.", Toast.LENGTH_SHORT).show();
                    } else {
                        StockItem newItem = new StockItem(itemName, itemDescription, itemQuantity, alertQuantity);
                        viewModel.addItem(newItem);
                        Toast.makeText(getContext(), "New item added.", Toast.LENGTH_SHORT).show();
                        // Return to InventoryActivity
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                });
            } else {
                // If the item already exists, update it
                currentItem.setName(itemName);
                currentItem.setDescription(itemDescription);
                currentItem.setQuantity(itemQuantity);
                currentItem.setAlertQuantity(alertQuantity);

                viewModel.updateItem(currentItem);
                Toast.makeText(getContext(), "Item updated.", Toast.LENGTH_SHORT).show();
                // Return to InventoryActivity
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid quantities.", Toast.LENGTH_SHORT).show();
        }

    }

    // Handles logic for the delete button.  Prompts dialog for confirmation.
    public void onDelete() {
        if (currentItem != null) {
            showDeleteConfirmationDialog(currentItem);
        } else {
            Toast.makeText(getContext(), "No item selected for deletion", Toast.LENGTH_SHORT).show();
        }
    }

    // Handles logic for cancel button.  Returns to InventoryActivity
    @Override
    public void onCancel() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    // Dialog box to confirm that the user wants to delete the item.
    private void showDeleteConfirmationDialog(final StockItem itemToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Are you sure you want to delete this item? It cannot be undone.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.deleteItem(itemToDelete);
                Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                // Close dialog and fragment.  Return to InventoryActivity
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }

        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            // Close dialog and return to fragment.
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