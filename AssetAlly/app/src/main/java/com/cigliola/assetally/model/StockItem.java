package com.cigliola.assetally.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

// Entity representing a stock item in the database
@Entity(tableName = "stock_items")
public class StockItem {
    @PrimaryKey(autoGenerate = true)
    private long mId = 0;
    @ColumnInfo(name = "name")
    private String mName;
    @ColumnInfo(name = "description")
    private String mDescription;
    @ColumnInfo(name = "quantity")
    private int mQuantity;
    @ColumnInfo(name = "alert_quantity")
    private int mAlertQuantity = 0;

    // Default constructor required for Room
    public StockItem() {}

    // Constructor for initializing basic information
    public StockItem(String name, String description, int quantity) {
        mName = name;
        mDescription = description;
        mQuantity = quantity;
    }

    // Constructor including optional alertQuantity, else defaults to 0
    public StockItem(String name, String description, int quantity, int alertQuantity) {
        mName = name;
        mDescription = description;
        mQuantity = quantity;
        mAlertQuantity = alertQuantity;
    }

    // Safely compares two stock items to check for matches across all fields
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockItem stockItem = (StockItem) o;
        return mId == stockItem.mId &&
                mQuantity == stockItem.mQuantity &&
                Objects.equals(mName, stockItem.mName) &&
                Objects.equals(mDescription, stockItem.mDescription);
    }

    // Getters and setters for attributes
    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int amount) {
        this.mQuantity = amount;
    }

    public int getAlertQuantity() {return mAlertQuantity;}

    public void setAlertQuantity(int alertQuantity) {this.mAlertQuantity = alertQuantity;}
}




