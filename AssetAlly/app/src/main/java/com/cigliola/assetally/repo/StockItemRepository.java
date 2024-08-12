package com.cigliola.assetally.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.cigliola.assetally.model.StockItem;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


// Class to handle the data operations from StockItem entities
public class StockItemRepository {
    private static StockItemRepository stockItemRepository;
    private final StockItemDao stockItemDao;
    private Executor ioExecutor; // For running database operations asynchronously

    // Returns a singleton instance of the repository
    public static StockItemRepository getInstance(Context context) {
        if (stockItemRepository == null) {
            stockItemRepository = new StockItemRepository(context);
        }
        return stockItemRepository;
    }

    // Private constructor to initialize DAO and executor
    private StockItemRepository(Context context) {
        AssetAllyDatabase database = AssetAllyDatabase.getDatabase(context);
        stockItemDao = database.stockItemDao();
        ioExecutor = Executors.newSingleThreadExecutor();
    }

    // Returns all items as LiveData
    public LiveData<List<StockItem>> getAllItems() {
        return stockItemDao.getAllItems();
    }

    // Adds items to the database asynchronously
    public void addItem(StockItem item) {
       ioExecutor.execute(() -> stockItemDao.insert(item));
    }

    // Retrieves the quantity of a specific item by name
    public LiveData<Integer> getItemQuantity(String itemName) {
        return stockItemDao.getQuantityByName(itemName);
    }

    // Updates an existing item in the database
    public void updateItem(StockItem currentStockItem) {
        ioExecutor.execute(() -> stockItemDao.update(currentStockItem));
    }

    // Retrieves an item by ID
    public LiveData<StockItem> getItemById(Long itemId) {
        return stockItemDao.getItemById(itemId);
    }

    // Retrieves an item by name
    public LiveData<StockItem> getItemByName(String itemName) {
        return stockItemDao.getItemByName(itemName);
    }

    // Deletes an item from the database
    public void deleteItem(StockItem item) {
        ioExecutor.execute(() -> stockItemDao.delete(item));
    }
}
