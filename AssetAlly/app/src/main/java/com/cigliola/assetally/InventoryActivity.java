package com.cigliola.assetally;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cigliola.assetally.model.StockItem;
import com.cigliola.assetally.model.StockItemAdapter;
import com.cigliola.assetally.repo.AssetAllyDatabase;
import com.cigliola.assetally.viewmodel.InventoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Activity for viewing and managing inventory.
public class InventoryActivity extends AppCompatActivity implements QuantityDialogFragment.QuantityEditListener{
    private InventoryViewModel viewModel;
    private RecyclerView recyclerView;
    private StockItemAdapter adapter;
    private StockItem currentStockItem;

    // Initializes viewModel and view components, sets listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(InventoryViewModel.class);
        recyclerView = findViewById(R.id.inventoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StockItemAdapter(this, item -> openItemDetailFragmentById(item.getId()),  item -> showQuantityDialogBox(item));
        recyclerView.setAdapter(adapter);

        viewModel.getAllItems().observe(this, stockItems -> {
            adapter.submitList(stockItems);
        });
        FloatingActionButton addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemDetailFragment();
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                openItemDetailFragmentByName(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // Ensures the addItemButton Floating Action Button is visible
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() ==0) {
                addItemButton.show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Opens a blank ItemDetailFragment when the addItemButton is clicked.
    private void openItemDetailFragment() {
        FloatingActionButton addItemButton = findViewById(R.id.addItemButton);
        addItemButton.hide();

        ItemDetailFragment fragment = new ItemDetailFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
    }

    // Opens the ItemDetailFragment of a specific item found by name.  Observes LiveData for that item
    private void openItemDetailFragmentByName(String itemName) {
        // Hides FloatingActionButton when changing to fragment
        FloatingActionButton addItemButton = findViewById(R.id.addItemButton);
        addItemButton.hide();
        LiveData<StockItem> liveData = viewModel.getItemByName(itemName);
        liveData.observe(this, new Observer<StockItem>() {
            @Override
            public void onChanged(StockItem item) {
                if (item != null) {
                    ItemDetailFragment fragment = ItemDetailFragment.newInstance(item.getId());
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
                } else {
                    Toast.makeText(InventoryActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                }
                //liveData.removeObserver(this);
            }
        });
    }

    // Opens the ItemDetailFragment of a specific item found by ID.  Observes LiveData for that item
    private void openItemDetailFragmentById(long itemId) {
        // Hides FloatingActionButton when changing to fragment
        FloatingActionButton addItemButton = findViewById(R.id.addItemButton);
        addItemButton.hide();
        LiveData<StockItem> liveData = viewModel.getItemById(itemId);
        liveData.observe(this, new Observer<StockItem>() {
            @Override
            public void onChanged(StockItem item) {
                if (item != null) {
                    ItemDetailFragment fragment = ItemDetailFragment.newInstance(item.getId());
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
                } else {
                    Toast.makeText(InventoryActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                }
                //liveData.removeObserver(this);
            }
        });
    }

    // Opens a dialog box for updating an item's quantity.
    private void showQuantityDialogBox(StockItem item) {
        currentStockItem = item;
        int currentQuantity = item.getQuantity();
        QuantityDialogFragment dialog = QuantityDialogFragment.newInstance(currentQuantity);
        dialog.show(getSupportFragmentManager(), "QuantityDialogFragment");
    }

    // Saves quantity update to the database
    @Override
    public void onSaveQuantity(int quantity) {
        if (currentStockItem != null) {
            currentStockItem.setQuantity(quantity);
            viewModel.updateItem(currentStockItem);
        }
    }


}