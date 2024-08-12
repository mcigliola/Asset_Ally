package com.cigliola.assetally;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cigliola.assetally.model.StockItem;

// Dialog fragment to update the quantity of an inventory item.
public class QuantityDialogFragment extends DialogFragment {
    public interface QuantityEditListener {
        void onSaveQuantity(int quantity);
    }

    private QuantityEditListener listener;

    public QuantityDialogFragment() {
        // Required empty public constructor
    }

    // Creates a new instance of QuantityDialogFragment with the current quantity preset
    public static QuantityDialogFragment newInstance(int currentQuantity) {
        QuantityDialogFragment fragment = new QuantityDialogFragment();
        Bundle args = new Bundle();
        args.putInt("quantity", currentQuantity);
        fragment.setArguments(args);
        return fragment;
    }

    // Implements the listener interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (QuantityEditListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_quantity_dialog, null);

        final EditText editTextQuantity = view.findViewById(R.id.edit_quantity);
        if (getArguments() != null) {
            editTextQuantity.setText(String.valueOf(getArguments().getInt("quantity")));
        }

        builder.setView(view)
                .setTitle("Edit Quantity")
                .setPositiveButton("Save", (dialog, id) -> {
                    int newQuantity = Integer.parseInt(editTextQuantity.getText().toString());
                    listener.onSaveQuantity(newQuantity);
                })
                .setNegativeButton("Cancel", ((dialog, id) ->  dialog.dismiss()));
        return builder.create();
    }

}