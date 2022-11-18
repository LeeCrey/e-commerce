package org.ethio.gpro.async_tasks;

import android.os.AsyncTask;

import org.ethio.gpro.data.dao.CartDao;
import org.ethio.gpro.models.Cart;

public class CartInsertAsyncTask extends AsyncTask<Cart, Void, Void> {
    private final CartDao cartDao;

    public CartInsertAsyncTask(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    @Override
    protected Void doInBackground(Cart... carts) {
        Cart cart = carts[0];
        cartDao.insertCart(cart);

        return null;
    }
}
