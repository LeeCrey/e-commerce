package org.ethio.gpro.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.ethio.gpro.async_tasks.CartAsyncClear;
import org.ethio.gpro.async_tasks.CartAsyncDelete;
import org.ethio.gpro.data.base.AppDataBase;
import org.ethio.gpro.data.dao.CartDao;
import org.ethio.gpro.models.Cart;

import java.util.List;

public class CartRepository {
    private final CartDao cartDao;
    private final LiveData<List<Cart>> oCarts;

    public CartRepository(Application application) {
        cartDao = AppDataBase.getInstance(application).cartDao();
        oCarts = cartDao.getAll();
    }

    public LiveData<List<Cart>> getCarts() {
        return oCarts;
    }

    public void deleteCart(int adapterPosition) {
        final List<Cart> list = oCarts.getValue();
        if (list != null) {
            Cart cart = list.get(adapterPosition);
            new CartAsyncDelete(cartDao).execute(cart);
        }
    }

    public void removeAllCarts() {
        List<Cart> list = oCarts.getValue();
        if (list != null) {

            new CartAsyncClear(cartDao).execute(list);
        }
    }
}
