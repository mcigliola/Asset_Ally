package com.cigliola.assetally.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cigliola.assetally.repo.StockItemRepository;
import com.cigliola.assetally.model.StockItem;

import java.util.List;

// ViewModel for managing inventory data. Provides methods to interact with data layer and contains
// LiveData observables that can be observed by UI components.
public class InventoryViewModel extends AndroidViewModel {
    private StockItemRepository repository;
    private LiveData<List<StockItem>> allItems;

    // Constructor
    public InventoryViewModel(Application application) {
        super(application);
        repository = StockItemRepository.getInstance(application);
        allItems = repository.getAllItems();
    }

    // Returns all stock items as LiveData
    public LiveData<List<StockItem>> getAllItems() {
        return allItems;
    }

    // Adds an item to the database
    public void addItem(StockItem item) {
        repository.addItem(item);
    }

    // Retrieves an item by its ID and returns LiveData
    public LiveData<StockItem> getItemById(Long itemId) {
        return repository.getItemById(itemId);
    }

    // Retrieves an item by its name and returns LiveData
    public LiveData<StockItem> getItemByName(String itemName) {
        return repository.getItemByName(itemName);
    }

    // Retrieves an item's quantity as LiveData
    public LiveData<Integer> getItemQuantity(StockItem item) {
        String itemName = item.getName();
        return repository.getItemQuantity(itemName);
    }

    // Updates an existing item in the database
    public void updateItem(StockItem currentStockItem) {
        repository.updateItem(currentStockItem);
    }

    // Deletes an item from the database
    public void deleteItem(StockItem item) {
        repository.deleteItem(item);
    }

}
