package com.evergreen.apps.tourguideapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import android.database.Cursor;

import com.evergreen.apps.tourguideapp.models.Category;

import java.util.List;

@Dao
public interface CategoryDaoAccess {


    @Insert
    long insert(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnlySingleCategory(Category category);

    @Insert
    void insertMultipleCategories(List<Category> categoryList);

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    Category fetchOneCategoryById(int categoryId);

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    Cursor fetchOneCategoryByIdCursor(long categoryId);

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> fetchAllCategories();

    @Query("SELECT * FROM categories")
    Cursor fetchCategories();

    @Query("UPDATE categories SET category_downloaded = :categoryDownloaded  WHERE id = :categoryId")
    int setCategoryDownloadedById (long categoryId, boolean categoryDownloaded);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("DELETE FROM categories")
    public void deleteAllCategories();

    @Query("DELETE FROM categories WHERE id = :categoryId")
    void deleteCategoryById(int categoryId);

}
