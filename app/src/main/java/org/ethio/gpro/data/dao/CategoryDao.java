package org.ethio.gpro.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.ethio.gpro.models.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);

    @Insert
    void insertAll(List<Category> categoryList);

    @Update
    void updateCategory(Category category);

    @Query("SELECT * FROM categories_tbl")
    LiveData<List<Category>> getAll();
}
