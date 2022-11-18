package org.ethio.gpro.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.ethio.gpro.models.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insertProduct(Product product);

    @Insert
    void insertAllProducts(List<Product> products);

    @Query("SELECT * FROM products_tbl")
    LiveData<List<Product>> getAll();
}
