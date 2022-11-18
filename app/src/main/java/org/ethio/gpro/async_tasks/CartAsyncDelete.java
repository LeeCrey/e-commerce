package org.ethio.gpro.async_tasks;

import android.os.AsyncTask;

import org.ethio.gpro.data.dao.CartDao;
import org.ethio.gpro.models.Cart;

public class CartAsyncDelete extends AsyncTask<Cart, Void, Void> {
    private final CartDao cartDao;

    public CartAsyncDelete(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    @Override
    protected Void doInBackground(Cart... carts) {
        Cart cart = carts[0];
        cartDao.removeCart(cart);

        return null;
    }
}
