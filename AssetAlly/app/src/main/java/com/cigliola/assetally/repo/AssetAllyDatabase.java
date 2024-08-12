package com.cigliola.assetally.repo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cigliola.assetally.model.StockItem;
import com.cigliola.assetally.model.User;

// Defines the database for the application with StockItem and User entities.
@Database(entities = {StockItem.class, User.class}, version = 2, exportSchema = false)
public abstract class AssetAllyDatabase extends RoomDatabase {
    // Database access objects for the database
    public abstract StockItemDao stockItemDao();
    public abstract UserDao userDao();

    // Singleton instance of the database
    private static volatile AssetAllyDatabase instance;

    //Returns the single instance of the database, creating one if necessary.
    static AssetAllyDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (AssetAllyDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AssetAllyDatabase.class, "AssetAllyDatabase")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }

}
