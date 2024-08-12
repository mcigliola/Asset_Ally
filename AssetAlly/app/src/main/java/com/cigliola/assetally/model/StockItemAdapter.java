package com.cigliola.assetally.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cigliola.assetally.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// Adapter class for displaying stock items in a RecyclerView
public class StockItemAdapter extends ListAdapter<StockItem, StockItemAdapter.StockItemViewHolder> {

    private Context context;
    private OnNameClickListener nameClickListener;
    private OnQuantityClickListener quantityClickListener;

    // Interface for handling clicks on item names.
    public interface OnNameClickListener {
        void onNameClick(StockItem item);
    }

    // Interface for handling clicks on item quantities
    public interface OnQuantityClickListener {
        void onQuantityClick(StockItem item);
    }

    // Constructor for the adapter.  Uses DiffUtil to strategically update the list in the recycler view
    public StockItemAdapter(Context context, OnNameClickListener nameClickListener, OnQuantityClickListener quantityClickListener) {
        super(new DiffUtil.ItemCallback<StockItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull StockItem oldItem, @NonNull StockItem newItem) {
                return oldItem.getId() == newItem.getId();
            }
            @Override
            public boolean areContentsTheSame(@NonNull StockItem oldItem, @NonNull StockItem newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.context = context;
        this.nameClickListener = nameClickListener;
        this.quantityClickListener = quantityClickListener;
    }

    @NonNull
    @Override
    public StockItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new StockItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockItemViewHolder holder, int position) {
        // Bind data to the views in the ViewHOlder
        StockItem item = getItem(position);
        holder.idTextView.setText(String.valueOf(item.getId()));
        holder.nameTextView.setText(item.getName());
        holder.nameTextView.setOnClickListener(v -> {
            if (nameClickListener != null) {
                nameClickListener.onNameClick(item);
            }
        });
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
        holder.quantityTextView.setOnClickListener(v -> {
            if (quantityClickListener != null) {
                quantityClickListener.onQuantityClick(item);
            }
        });
    }

    //ViewHolder class for RecyclerView items
    static class StockItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView idTextView;
        TextView quantityTextView;

        public StockItemViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);


        }
    }
}
