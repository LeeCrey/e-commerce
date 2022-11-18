package org.ethio.gpro.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.ethio.gpro.models.Cart;

import java.util.List;

@Dao
public interface CartDao {
    @Query("SELECT * FROM carts_tbl")
    LiveData<List<Cart>> getAll();

    @Query("SELECT * FROM carts_tbl WHERE id = :id LIMIT 1")
    Cart findById(int id);

    @Insert
    void insertCart(Cart cart);

    @Insert
    void insertAllCarts(List<Cart> carts);

    @Delete
    void removeCart(Cart cart);

    @Update
    void updateCart(Cart cart);

    @Delete
    void removeAll(List<Cart> list);

    @Delete
    void removeAll(Cart... list);
}
