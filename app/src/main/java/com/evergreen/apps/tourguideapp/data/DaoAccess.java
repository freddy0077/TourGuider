package com.evergreen.apps.tourguideapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import android.database.Cursor;

import com.evergreen.apps.tourguideapp.models.Location;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    long insert(Location location);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnlySingleLocation (Location location);

    @Insert
    void insertMultipleLocations (List<Location> locationList);

    @Query("SELECT * FROM locations WHERE id = :locationId")
    Location fetchOneLocationByLocationId (int locationId);

    @Query("SELECT * FROM locations ORDER BY RANDOM() LIMIT 1")
    Cursor fetchRandomLocation ();

    @Query("SELECT * FROM locations ORDER BY RANDOM()")
    Cursor fetchRandomLocations();

    @Query("SELECT * FROM locations WHERE id = :locationId")
    Cursor fetchOneLocationById (long locationId);

    @Query("SELECT * FROM locations")
    LiveData<List<Location>> fetchAllLocations ();

    @Query("SELECT * FROM locations")
    Cursor  fetchLocations ();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateLocation (Location location);

    @Delete
    void deleteLocation (Location location);

    @Query("DELETE FROM locations")
    public void deleteAllLocations ();

    @Query("DELETE FROM locations WHERE id = :locationId")
    int deleteLocationById (long locationId);



}
