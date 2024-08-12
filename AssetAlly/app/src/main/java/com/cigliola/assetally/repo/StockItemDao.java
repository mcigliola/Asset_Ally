package com.cigliola.assetally.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cigliola.assetally.model.StockItem;

import java.util.List;

// Data access object from managing StockItem entities in the database
@Dao
public interface StockItemDao {
    // Retrieves all items from the database
    @Query("SELECT * FROM stock_items")
    LiveData<List<StockItem>> getAllItems();

    // Retrieves a single item by name
    @Query("SELECT * FROM stock_items WHERE name = :itemName")
    LiveData<StockItem> getItemByName(String itemName);

    // Retrieves a single item by ID
    @Query("SELECT * FROM stock_items WHERE mId = :itemId")
    LiveData<StockItem> getItemById(long itemId);

    // Retrieves the quantity of a single item by name
    @Query("SELECT quantity FROM stock_items WHERE name = :itemName")
    LiveData<Integer> getQuantityByName(String itemName);

    // Adds a new item to the database
    @Insert
    void insert(StockItem stockItem);

    // Updates an existing item in the database
    @Update
    void update(StockItem stockItem);

    // Deletes an item from the database
    @Delete
    void delete(StockItem stockItem);

}
