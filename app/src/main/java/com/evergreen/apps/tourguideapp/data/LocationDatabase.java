package com.evergreen.apps.tourguideapp.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.evergreen.apps.tourguideapp.models.Category;
import com.evergreen.apps.tourguideapp.models.Location;

@Database(entities = {Location.class, Category.class}, version = 10, exportSchema = false)

public abstract class LocationDatabase extends RoomDatabase {

    private static final String TAG = LocationDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "location_db";
    private static  LocationDatabase sInstance;

    public static LocationDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(TAG, "Creating a new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        LocationDatabase.class, LocationDatabase.DATABASE_NAME)
//                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
//
//        Log.d(TAG, "Getting the Location database instance");

        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param
     */
//    @VisibleForTesting
//    public static void switchToInMemory(Context context) {
//        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
//                LocationDatabase.class).build();
//    }


    public abstract DaoAccess daoAccess();

    public abstract CategoryDaoAccess categoryDaoAccess();
}
